package library.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import library.model.entity.Book;
import library.model.entity.BookCopy;
import library.model.entity.BookLang;
import library.model.entity.Borrowing;
import library.model.entity.Lang;
import library.model.entity.Penalty;
import library.model.entity.Subscriber;
import library.model.repository.BookCopyRepository;
import library.model.repository.BookRepository;
import library.model.repository.BorrowingRepository;
import library.model.repository.PenaltyRepository;
import library.model.repository.SubscriberRepository;
import library.security.AuthenticationFrontendUserDetails;
import library.service.BorrowingService;
import library.service.SubscriberService;
import library.tools.MailerTool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * TODO : 
 * - dans la sauvegarde de l'emprunt il faut rajouter le titre du livre concérné ainsi que les informations sur BookCopy
 * - gestion des pénalités en cas d'un retard dans returnBookCopy
 * - utiliser JodaTime au lieu de SDK
 */
@Service("borrowingService")
public class BorrowingServiceImpl implements BorrowingService {
    private static final String TPL_BORROWING_CONFIRM = "confirmBorrowing";
    private static final String TPL_BORROWING_ALERT = "alertBorrowing";
    final Logger logger = LoggerFactory.getLogger(BorrowingServiceImpl.class);
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private SubscriberService subscriberService;
    @Autowired
    private PenaltyRepository penaltyRepository;
    @Autowired
    private BorrowingRepository borrowingRepository;
    @Autowired
    private BookCopyRepository bookCopyRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private SubscriberRepository subscriberRepository;
    @Autowired
    private MailerTool mailerTool;

    @Override
    @PreAuthorize("hasRole('ROLE_USER') and principal.bookingNb > 0")
    public Borrowing borrowBookCopy(Borrowing borrowing, BookCopy bookCopy, User user) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        if (borrowing == null) borrowing = new Borrowing();
        try {
            Subscriber subscriber = subscriberService.getById(((AuthenticationFrontendUserDetails)user).getId());
            // inert new borrowing line
            borrowing.setBookCopy(bookCopy);
            borrowing.setSubscriber(subscriber);
            borrowing.setAlert(Borrowing.ALERT_NO);
            if (borrowing.getDateFrom() == null) {
                borrowing.setDateFrom(new Date());
            }
            if (borrowing.getDateTo() == null) {
                borrowing.setDateTo(borrowing.getDateFrom(), true);
            }
            borrowingRepository.save(borrowing);
            // update BookCopy
            bookCopy.setState(BookCopy.getBookedStateId());
            bookCopyRepository.save(bookCopy);
            // decrement subscriber's and user's bookingNb
            subscriber.setBookingNb((subscriber.getBookingNb()-1));
            subscriberRepository.save(subscriber);
            // send confirmation e-mail
            // TEMPORARY : remettre dynamique
            Lang lang = new Lang();
            lang.setId(1l);
            List<Book> book = bookRepository.getByIdAndLang(bookCopy.getBook().getId(), lang); 
            HashMap<String, Object> mailData = new HashMap<String, Object>();
            mailData.put("config", TPL_BORROWING_CONFIRM);
            mailData.put("to", borrowing.getSubscriber().getEmail());
            Map<String, Object> tplVars = new HashMap<String, Object>();
            tplVars.put("from", borrowing.getDateFrom());
            tplVars.put("to", borrowing.getDateTo());
            tplVars.put("book", book);
            mailerTool.setMailData(mailData);
            mailerTool.setVars(tplVars);
            mailerTool.send();
            transactionManager.commit(status);
            ((AuthenticationFrontendUserDetails) user).setBookingNb(subscriber.getBookingNb());
        } catch (Exception e) {
            logger.error("An exception occured on saving Borrowing", e);
            borrowing = null;
            transactionManager.rollback(status);
        }
        return borrowing;
    }
    
    @Override
    public Borrowing getBorrowingByIdAndUser(long borrowingId, Subscriber subscriber) {
        return borrowingRepository.getBorrowingByIdAndUser(borrowingId, subscriber);
    }
    
    @Override
    @PreAuthorize("hasRole('ROLE_USER') and principal.id == subscriber.id")
    public boolean returnBookCopy(Borrowing borrowing, Subscriber subscriber, User user) {
        boolean success = false;
        if (borrowing != null) {
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            TransactionStatus status = transactionManager.getTransaction(def);
            try {
                // Calendar today = Calendar.getInstance();
                // increment bookingNb() for Subscriber
                subscriber.setBookingNb((subscriber.getBookingNb()+1));
                subscriberRepository.save(subscriber);
                // TODO : si une alerte pas dététectée mais le livre retourné après la date normale, appliquer une pénalité
                // if(!borrowing.hasAlert() && )
                // {
                    // logger.info("=========> Borrowing has penalty " + borrowing);
                    
                    
                // }
                // put BookCopy state into "available" for booking
                BookCopy bookCopy = borrowing.getBookCopy();
                bookCopy.setState(BookCopy.getStateAvailableId());
                bookCopyRepository.save(bookCopy);
                // delete entry from borrowing table
                borrowingRepository.delete(borrowing);
                transactionManager.commit(status);
                // increment bookingNb() for user
                ((AuthenticationFrontendUserDetails) user).setBookingNb(subscriber.getBookingNb());
                success = true;
            } catch (Exception e) {
                logger.error("An exception occured on returning Borrowing", e);
                transactionManager.rollback(status);
            }
        }
        return success;
    }
    
    @Override
    public List<Object[]> getDelayedBooks() {
        return borrowingRepository.getDelayedBooks(BookCopy.getBookedStateId(), Borrowing.PENALIZED_NO);
    }
    
    @Override
    public void checkDelayed() {
	    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        List<Object[]> borrowings = getDelayedBooks();
        Map<Long, List<Borrowing>> penalties = new HashMap<Long, List<Borrowing>>();
        Map<Long, List<Borrowing>> alerts = new HashMap<Long, List<Borrowing>>();
        logger.info("============> Found delayed books " + borrowings.size());
        for (Object[] obj : borrowings) {
            Borrowing borrowing = (Borrowing)obj[0];
            // set new borrowing state (alert was send)
            borrowing.setAlert((borrowing.getAlert()+1));
            borrowing.setLastActionDate(new Date());
            borrowing.setInsertPenalty(borrowing.canInsertPenalty());
            Subscriber subscriber = (Subscriber)obj[2];
            // if alert state is too high, insert new penalty line
            if( borrowing.getInsertPenalty()) {
                borrowing.setPenalized(Borrowing.PENALIZED_YES);
                List<Borrowing> borrowingList = new ArrayList<Borrowing>();
                if (penalties.containsKey(subscriber.getId())) {
                    borrowingList = penalties.get(subscriber.getId()); 
                }
                borrowingList.add(borrowing);
                penalties.put(subscriber.getId(), borrowingList);
            }
            List<Borrowing> alertsList = new ArrayList<Borrowing>();
            if (alerts.containsKey(subscriber.getId())) {
                alertsList = alerts.get(subscriber.getId());
            }
            alertsList.add(borrowing);
            alerts.put(subscriber.getId(), alertsList);
        }
        try {
            for (Map.Entry<Long, List<Borrowing>> entry : alerts.entrySet()) {
                // set new borrowing state (alert was send)
                for (Borrowing borrowing : entry.getValue()) {
                    borrowing.setAlert((borrowing.getAlert()+1));
                    borrowing.setLastActionDate(new Date());
                    borrowing = borrowingRepository.save(borrowing);
                }
                // look for sending alerts
                if (penalties.containsKey(entry.getKey())) {
                    Penalty penalty = new Penalty();
                    penalty.setAmount(0.0d);
                    StringBuilder alertTxt = new StringBuilder();
                    for (Borrowing alert : penalties.get(entry.getKey())) {
                        penalty.setSubscriber(alert.getSubscriber());
                        double amount = penalty.getPenaltyAmount(alert.getAlert());
                        penalty.setAmount((amount+penalty.getAmount()));
                        alertTxt.append("Borrowing " + alert.getId() + ", amount : " + amount);
                    }
                    penalty.setAbout(alertTxt.toString());
                    penalty.setState(Penalty.STATE_WAITING);
                    penalty.setPenaltyPK(new Date().getTime(), penalty.getSubscriber().getId());
                    logger.info("Saving penalty " + penalty);
                    penalty = penaltyRepository.save(penalty);
                }
            }
            transactionManager.commit(status);
            logger.info("Inserted penalties " + penalties);
            logger.info("Inserted alerts " + alerts);
        } catch (Exception e) {
            logger.error("An exception occured on saving Borrowing delay", e);
            transactionManager.rollback(status);
        }
        // sending alerts after all operations
        for (Map.Entry<Long, List<Borrowing>> entry : alerts.entrySet()) {
            Borrowing borrowing = entry.getValue().get(0);
            HashMap<String, Object> mailData = new HashMap<String, Object>();
            mailData.put("config", TPL_BORROWING_ALERT);
            mailData.put("to", borrowing.getSubscriber().getEmail());
            Map<String, Object> tplVars = new HashMap<String, Object>();
            tplVars.put("alerts", entry.getValue());
            tplVars.put("penalties", penalties.get(entry.getKey()));
            try {
                mailerTool.setMailData(mailData);
                mailerTool.setVars(tplVars);
                mailerTool.send();
            } catch (Exception e) {
                logger.info("An exception occured on sending borrowing alert e-mail to subscriber ("+borrowing.getSubscriber()+") ", e);
            }
        }
    }
    
    @Override
    public Map<Long, Map<String, Object>> getReportByLangId(long langId) {
        Map<Long, Map<String, Object>> result = new TreeMap<Long, Map<String, Object>>();
        List<Object[]> borrowingsList = borrowingRepository.getReportByLangId(langId);
        for (Object[] borrowingRow : borrowingsList) {
            // Object[] is composed by : 0 - Borrowing, 1 - BookCopy, 2 - Subscriber, 3 - BookLang
            Borrowing borrowing = (Borrowing)borrowingRow[0];
            BookCopy bookCopy = (BookCopy)borrowingRow[1];
            Subscriber subscriber = (Subscriber)borrowingRow[2];
            BookLang bookLang = (BookLang)borrowingRow[3];

            Map<String, Object> localMap = new HashMap<String, Object>();
            Map<String, String> translations = new HashMap<String, String>();
            if (result.containsKey(borrowing.getId())) {
                localMap = result.get(borrowing.getId());
                translations = (Map<String, String>)localMap.get("translations");
            } else {
                localMap.put("borrowing", borrowing);
                localMap.put("bookCopy", bookCopy);
                localMap.put("subscriber", subscriber);
            }
            // add translations firstly
            translations.put(bookLang.getBookLangPK().getType(), bookLang.getValue());
            localMap.put("translations", translations);
            result.put(borrowing.getId(), localMap);
        }
        return result;
    }
    
    @Override
    public long countBorrowedBySubscriber(Subscriber subscriber, Date from, Date to) {
        if (from == null || to == null) return borrowingRepository.countBySubscriber(subscriber);
        else return borrowingRepository.countBySubscriberAndDates(subscriber, from, to);
    }
}