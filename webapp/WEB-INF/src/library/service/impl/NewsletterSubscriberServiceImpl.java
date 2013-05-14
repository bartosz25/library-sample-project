package library.service.impl;
 
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import library.form.NewsPreferenciesCredentialsForm;
import library.model.entity.Category;
import library.model.entity.CategoryLang;
import library.model.entity.Lang;
import library.model.entity.Newsletter;
import library.model.entity.NewsletterPreferencyCategory;
import library.model.entity.NewsletterPreferencyLang;
import library.model.entity.NewsletterSend;
import library.model.entity.NewsletterSubscriber;
import library.model.entity.NewsletterSubscriberPreferency;
import library.model.entity.Subscriber;
import library.model.entity.Writer;
import library.model.repository.CategoryLangRepository;
import library.model.repository.NewsletterPreferencyCategoryRepository;
import library.model.repository.NewsletterPreferencyLangRepository;
import library.model.repository.NewsletterSendRepository;
import library.model.repository.NewsletterSubscriberPreferencyRepository;
import library.model.repository.NewsletterSubscriberRepository;
import library.model.repository.WriterRepository;
import library.security.AuthenticationFrontendUserDetails;
import library.security.Cryptograph;
import library.security.SaltCellar;
import library.service.NewsletterPreferencyCategoryService;
import library.service.NewsletterSubscriberService;
import library.service.SubscriberService;
import library.tools.MailerTool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.transaction.annotation.Transactional;

@Service("newsletterSubscriberService")
public class NewsletterSubscriberServiceImpl implements NewsletterSubscriberService {
    private static final String TPL_CONFIRM = "confirmNewsletter";
    private static final String TPL_NEWSLETTER = "sendNewsletter";
    final Logger logger = LoggerFactory.getLogger(NewsletterSubscriberServiceImpl.class);
    @Autowired
    private CategoryLangRepository categoryLangRepository;
    @Autowired
    private WriterRepository writerRepository;
    @Autowired
    private NewsletterSubscriberRepository newsletterSubscriberRepository;
    @Autowired
    private NewsletterSubscriberPreferencyRepository newsletterSubscriberPreferencyRepository;
    @Autowired
    private NewsletterPreferencyLangRepository newsletterPreferencyLangRepository;
    @Autowired
    private NewsletterPreferencyCategoryRepository newsletterPreferencyCategoryRepository;
    @Autowired
    private NewsletterSendRepository newsletterSendRepository;
    @Autowired
    private SubscriberService subscriberService;
    @Autowired
    private NewsletterPreferencyCategoryService newsletterPreferencyCategoryService;
    @Autowired
    private ShaPasswordEncoder passwordEncoder;
    @Autowired
    private SaltCellar saltCellar;
    @Autowired
    private MailerTool mailerTool;
    @Autowired 
    private Cryptograph cryptograph;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private EntityManagerFactory emf;

    @Override
    public NewsletterSubscriber getByMail(String mail) {
        return newsletterSubscriberRepository.getByMail(mail);
    }

    // TODO : en cas d'une erreur, afficher le message d'erreur
    @Override
    public NewsletterSubscriber addFromFlow(LocalAttributeMap conversationScope, 
    UsernamePasswordAuthenticationToken user, String password) {
        logger.info("saving newsletter subscription with conversaction scope " + conversationScope);
        logger.info("saving newsletter subscription with conversaction scope email " + conversationScope.get("email"));
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        NewsletterSubscriber newsletterSubscriber = new NewsletterSubscriber();
        try {
            newsletterSubscriber.setEmail((String)conversationScope.get("email"));
            if (user != null) {
                AuthenticationFrontendUserDetails principal = (AuthenticationFrontendUserDetails)user.getPrincipal();
                Subscriber subscriber = subscriberService.loadByUsername(principal.getUsername());
                newsletterSubscriber.setSubscriber(subscriber);
                newsletterSubscriber.setPassword(subscriber.getPassword());
                newsletterSubscriber.setState(NewsletterSubscriber.STATE_CONFIRMED);
            } else {
                String passwordEncoded = passwordEncoder.encodePassword(password, saltCellar.getSaltFromString(newsletterSubscriber.getEmail()));
                newsletterSubscriber.setPassword(passwordEncoded);
                newsletterSubscriber.setState(NewsletterSubscriber.STATE_NOT_CONFIRMED);
            }
            newsletterSubscriber = newsletterSubscriberRepository.save(newsletterSubscriber);
            if (conversationScope.contains("preferencies") && conversationScope.get("preferencies") != null) {
                savePreferenciesFromArray((String[])conversationScope.get("preferencies"), newsletterSubscriber);
            }
            sendSubscriptionEmail(newsletterSubscriber);
            transactionManager.commit(status);
        } catch (Exception e) {
            logger.error("An exception occured on saving newsletter subscription", e);
            transactionManager.rollback(status);
            newsletterSubscriber = null;
        }
        return newsletterSubscriber;
    }
    
    @Override
    public NewsletterSubscriber getByMailAndPlainTextPassword(String email, String password) {
        String passwordEncoded = passwordEncoder.encodePassword(password, saltCellar.getSaltFromString(email));
        return newsletterSubscriberRepository.getByMailAndEncodedPassword(email, passwordEncoded, NewsletterSubscriber.STATE_CONFIRMED);
    }
    
    @Override
    public boolean modifySubscription(NewsPreferenciesCredentialsForm newsPreferenciesCredentialsForm, Subscriber
    subscriber) {
        boolean modified = true;
        logger.info("saving newsletter preferencies " + newsPreferenciesCredentialsForm);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            NewsletterSubscriber newsletterSubscriber = null;
            String credPassword = newsPreferenciesCredentialsForm.getCredPassword();
            int stateIfMailChanged = NewsletterSubscriber.STATE_NOT_CONFIRMED;
            if (subscriber != null) {
                newsletterSubscriber = newsletterSubscriberRepository.getByMailAndEncodedPassword(newsPreferenciesCredentialsForm.getCredEmail(), subscriber.getPassword(), NewsletterSubscriber.STATE_CONFIRMED);
                stateIfMailChanged = NewsletterSubscriber.STATE_CONFIRMED;
            } else {
                newsletterSubscriber = getByMailAndPlainTextPassword(newsPreferenciesCredentialsForm.getCredEmail(), newsPreferenciesCredentialsForm.getCredPassword());
            }
            if (newsletterSubscriber == null) throw new Exception("NewsletterSubscriber was not found");
            logger.info("============> Found newsletterSubscriber " + newsletterSubscriber);
            // remove all saved preferencies
            newsletterSubscriberPreferencyRepository.deleteBySubscriber(newsletterSubscriber.getId());
            // save preferencies
            savePreferenciesFromArray(newsPreferenciesCredentialsForm.getPreferenciesArray(), newsletterSubscriber);
            
            // if e-mail was modified, make subscriber state to non confirmed
            String passwordEncoded = "";
            if (newsPreferenciesCredentialsForm.getPassword() != null && !newsPreferenciesCredentialsForm.getPassword().equals("")) {
                passwordEncoded = passwordEncoder.encodePassword(newsPreferenciesCredentialsForm.getPassword(), saltCellar.getSaltFromString(newsPreferenciesCredentialsForm.getEmail()));
                newsletterSubscriber.setPassword(passwordEncoded);
            }
            if (newsPreferenciesCredentialsForm.getEmail() != null && !newsPreferenciesCredentialsForm.getEmail().equals("") && !newsletterSubscriber.getEmail().equals(newsPreferenciesCredentialsForm.getEmail())) {
                newsletterSubscriber.setEmail(newsPreferenciesCredentialsForm.getEmail());
                newsletterSubscriber.setState(stateIfMailChanged);
                if (passwordEncoded.equals("")) {
                    passwordEncoded = passwordEncoder.encodePassword(newsPreferenciesCredentialsForm.getCredPassword(), saltCellar.getSaltFromString(newsPreferenciesCredentialsForm.getEmail()));
                    newsletterSubscriber.setPassword(passwordEncoded);
                }
                sendSubscriptionEmail(newsletterSubscriber);
            }
            newsletterSubscriberRepository.save(newsletterSubscriber);
            transactionManager.commit(status);
        } catch(Exception e) {
            logger.error("An exception occured on modifying newsletter preferencies", e);
            transactionManager.rollback(status);
            modified = false;
        }
        return modified;
    }
    
    // TODO : rajoute la possibilité spécifier LIMIT OFFSET
    @Transactional(readOnly = true)
    @Override
    public List<Object[]> getByCriteria(List<String> preferencies, Newsletter newsletter, int limit) {
        // logger.info("==============> Getting preferencies for " + preferencies);
        EntityManager em = null;
        try {
            List<String> codes = new ArrayList<String>();
            String[] codesSplitted;
            for (String preferency : preferencies) {
                if (preferency.indexOf("-") > -1) {
                    codesSplitted = preferency.split("-");
                    codes.add(codesSplitted[0].trim());
                }
            }
            Map<String, NewsletterPreferencyCategory> categories = newsletterPreferencyCategoryService.constructFromCodes(codes);
            // logger.info("==========> FOund categories " + categories);
            List<String> queryCodes = new ArrayList<String>();
            for (String preferency : preferencies) {
                if (preferency.indexOf("-") > -1) {
                    codesSplitted = preferency.split("-");
                    queryCodes.add(categories.get(codesSplitted[0].trim()).getId()+"_"+codesSplitted[1].trim());
                }
            }
            String additionalClause = "";
            if (newsletter != null) {
            // additionalClause = " AND ns.lastNewsletter < :newsletter";
                additionalClause = " AND (SELECT nse.sendDate FROM NewsletterSend nse WHERE nse.newsletterSendPK.idNewsletter = :idNewsletter AND nse.newsletterSendPK.idSubscriber = ns.id) IS NULL";
                // logger.info("========> Additional clause for newsletter " + additionalClause);
            }
            String preferenciesClause = "";
            String groupByClause = "";
            String preferenciesJoin = "";
            if (queryCodes.size() > 0) {
                preferenciesJoin = "JOIN ns.preferencies nsp ";
                groupByClause = " GROUP BY ns.id HAVING COUNT(ns) = "+queryCodes.size();
                preferenciesClause = "CONCAT_WS('_', nsp.newsletterSubscriberPreferencyPK.idNewsletterPreferencyCategory, nsp.preferency) IN :codes AND";
                // logger.info("=============> Using query with preferencies " + queryCodes);
            }
            em = emf.createEntityManager();
        // Query query = em.createQuery("SELECT ns FROM NewsletterSubscriber ns JOIN ns.preferencies nsp WHERE CONCAT_WS('_', nsp.newsletterSubscriberPreferencyPK.idNewsletterPreferencyCategory, nsp.preferency) IN :codes AND ns.state = :state "+additionalClause+" GROUP BY ns.id HAVING COUNT(ns) = "+queryCodes.size());
            Query query = em.createQuery("SELECT ns, s FROM NewsletterSubscriber ns LEFT JOIN ns.subscriber s "+preferenciesJoin+" WHERE "+preferenciesClause+" ns.state = :state "+additionalClause+groupByClause).setFirstResult(0).setMaxResults(limit);
            query.setParameter("state", NewsletterSubscriber.STATE_CONFIRMED);
            if (newsletter != null) {
                // query.setParameter("newsletter", newsletter);
                query.setParameter("idNewsletter", newsletter.getId());
            }
            if (queryCodes.size() > 0) {
                query.setParameter("codes", queryCodes);
            }
// WHERE ns.state = :state").setFirstResult(0).setMaxResults(limit);//
/**
 * Important pour comprendre d'où vient NewsletterSubscriber_ : 
 * http://docs.oracle.com/javaee/6/tutorial/doc/gjiup.html
 */
// http://stackoverflow.com/questions/4378824/adding-in-clause-list-to-a-jpa-query Pour créer une requête qui prend en compte IN() avec List<String> ou quelque chose comme ça
// http://www.objectdb.com/java/jpa/query/jpql/where aussi un exemple pour IN
            return query.getResultList();
        } catch (Exception e) {
            logger.error("An exception occured on getting newsletters", e);
        } finally {
            // if EntityManager isn't close, max pool size is reached very quickly and deadlock occurs
            if (em != null) em.close();
        }
        return null;
    }

    // TODO : implémenter l'envoi des mails 
    @Override
    public NewsletterSend sendToSubcriber(NewsletterSend newsletterSend) throws Exception {
        boolean result = true;
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            
            newsletterSend.setSendDate(new Date());
            newsletterSend.setState(NewsletterSend.STATE_NOT_OPENED);
            newsletterSend = newsletterSendRepository.save(newsletterSend);
            
            // TODO : implémenter le suivi des clicks (avec ?ifOpenedKey=ifOpenedKey et un filtre/listener correspondant)
            // TODO : dans body il faut mettre ifOpenedKey dans une image pour vérifier le taux d'ouverture
	        HashMap<String, Object> mailData = new HashMap<String, Object>();
            mailData.put("config", TPL_NEWSLETTER);
            mailData.put("to", newsletterSend.getNewsletterSubscriber().getEmail());
            Map<String, Object> tplVars = new HashMap<String, Object>();
            tplVars.put("ifOpenedKey", cryptograph.encrypt(newsletterSend.getNewsletterSendPK().toString()));
            tplVars.put("content", newsletterSend.getNewsletter().getText());
            mailerTool.setMailData(mailData);
            mailerTool.setVars(tplVars);
            // mailerTool.send();
            logger.info("Sending newsletter to " + mailData);
            transactionManager.commit(status);
        } catch(Exception e) {
            logger.error("An exception occured on sending newsletter "+newsletterSend, e);
            transactionManager.rollback(status);
            newsletterSend = null;
            throw new Exception("An exception occured on sending newsletter "+newsletterSend, e);
        }        
        return newsletterSend;
    }
    
    private void savePreferenciesFromArray(String[] preferencies, NewsletterSubscriber newsletterSubscriber) {
        // TODO : gérer Lang dynamiquement
        Lang lang = new Lang();
        lang.setId(2l);
        // TODO : gérer la recherche des valeurs dynamiquement et non pas avec les boucles if else
        for (String preferency : preferencies) {
            NewsletterSubscriberPreferency newsletterSubscriberPreferency = new NewsletterSubscriberPreferency();
            // codes[0] => id_npc from newsletter_preferency_category
            // codes[1] => id of newsletter preferency
            // codes[2] => if presents, report that the "String preferency" belongs to newsletter_preferency table
            String[] codes = preferency.split("-");
            NewsletterPreferencyCategory newsletterPreferencyCategory =       newsletterPreferencyCategoryRepository.getByCode(codes[0].trim());
            String label = "";
            long idPref = Long.parseLong(codes[1].trim());
            if (codes[0].equals("BOOKCATE")) {
                Category categoryEnt = new Category();
                categoryEnt.setId(idPref);
                CategoryLang category = categoryLangRepository.getByCatLang(categoryEnt, lang);
                logger.info("Found book" + category);
                label = category.getName();
            } else if (codes[0].equals("WRITERFA")) {
                Writer writer = writerRepository.findOne(idPref);
                logger.info("Found writer " + writer);
                label = writer.getFullname();
            } else {
                NewsletterPreferencyLang newsletterPreferencyLang = newsletterPreferencyLangRepository.getByPrefAndLang(idPref, lang.getId());
                logger.info("Found newsletterPreferencyLang " + newsletterPreferencyLang);
                label = newsletterPreferencyLang.getLabel();
            }
            newsletterSubscriberPreferency.setPreferency(idPref);
            newsletterSubscriberPreferency.setNewsletterSubscriberPreferencyPK(newsletterPreferencyCategory.getId(), newsletterSubscriber.getId());
            newsletterSubscriberPreferency.setValue(label);
            newsletterSubscriberPreferencyRepository.save(newsletterSubscriberPreferency);
        }
    }
    
    private void sendSubscriptionEmail(NewsletterSubscriber newsletterSubscriber) throws Exception
    {
        HashMap<String, Object> mailData = new HashMap<String, Object>();
        mailData.put("config", TPL_CONFIRM);
        mailData.put("to", newsletterSubscriber.getEmail());
        Map<String, Object> tplVars = new HashMap<String, Object>();
        tplVars.put("login", newsletterSubscriber.getEmail());
        tplVars.put("idCrypted", cryptograph.encrypt(Long.toString(newsletterSubscriber.getId())));
        tplVars.put("idDecrypted", cryptograph.decrypt((String)tplVars.get("idCrypted")));
        mailerTool.setMailData(mailData);
        mailerTool.setVars(tplVars);
        mailerTool.send();
    }
}