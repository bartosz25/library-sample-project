package com.example.library.service.impl;
 
import java.util.Date;

import com.example.library.model.entity.Newsletter;
import com.example.library.model.entity.NewsletterSend;
import com.example.library.model.entity.NewsletterSubscriber;
import com.example.library.model.repository.NewsletterSendRepository;
import com.example.library.service.NewsletterSendService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

@Service("newsletterSendService")
public class NewsletterSendServiceImpl implements NewsletterSendService {
    final Logger logger = LoggerFactory.getLogger(NewsletterSendServiceImpl.class);
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private NewsletterSendRepository newsletterSendRepository;
    
    @Override
    public NewsletterSend addToSendList(NewsletterSubscriber newsletterSubscriber, Newsletter newsletter) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
	try {
        NewsletterSend newsletterSend = new NewsletterSend();
        newsletterSend.setNewsletterSendPK(newsletter.getId(), newsletterSubscriber.getId());
        newsletterSend.setSendDate(new Date());
        newsletterSend.setState(NewsletterSend.STATE_TO_SEND);
        logger.info("RESULT : Adding " + newsletterSubscriber.getId());
		newsletterSend =  newsletterSendRepository.save(newsletterSend);
		transactionManager.commit(status);
		return newsletterSend;
    } catch (Exception e) {
            transactionManager.rollback(status);
            logger.error("An exception occured on adding newsletterSend", e);
	    
	     
	} finally {
	    if(!status.isCompleted())
	    {
	    transactionManager.rollback(status);
	    }
	}
	return null;
    }

    @Override
    public Page<Object[]> getToSend(int offset, int limit) {
        Pageable pageable = new PageRequest(offset, limit);
        logger.info("Get newsletters to send with offset and limit : " + offset + ", " + limit);
        return newsletterSendRepository.getReceiversToSend(NewsletterSend.STATE_TO_SEND, pageable);
    }            
            // // TODO : implémenter le suivi des clicks (avec ?ifOpenedKey=ifOpenedKey et un filtre/listener correspondant)
            // // TODO : dans body il faut mettre ifOpenedKey dans une image pour vérifier le taux d'ouverture
	        // HashMap<String, Object> mailData = new HashMap<String, Object>();
            // mailData.put("config", TPL_NEWSLETTER);
            // mailData.put("to", newsletterSubscriber.getEmail());
            // Map<String, Object> tplVars = new HashMap<String, Object>();
            // tplVars.put("ifOpenedKey", cryptograph.encrypt(newsletterSend.getNewsletterSendPK().toString()));
            // tplVars.put("content", newsletter.getText());
            // mailerTool.setMailData(mailData);
            // mailerTool.setVars(tplVars);
            // mailerTool.send();
            // transactionManager.commit(status);
        // } catch(Exception e) {
            // logger.error("An exception occured on sending newsletter to "+newsletterSubscriber, e);
            // transactionManager.rollback(status);
            // result = false;
        // }        
        // return result;
    // }
    
    // private void savePreferenciesFromArray(String[] preferencies, NewsletterSubscriber newsletterSubscriber) {
        // // TODO : gérer Lang dynamiquement
        // Lang lang = new Lang();
        // lang.setId(2l);
        // // TODO : gérer la recherche des valeurs dynamiquement et non pas avec les boucles if else
        // for (String preferency : preferencies) {
            // NewsletterSubscriberPreferency newsletterSubscriberPreferency = new NewsletterSubscriberPreferency();
            // // codes[0] => id_npc from newsletter_preferency_category
            // // codes[1] => id of newsletter preferency
            // // codes[2] => if presents, report that the "String preferency" belongs to newsletter_preferency table
            // String[] codes = preferency.split("-");
            // NewsletterPreferencyCategory newsletterPreferencyCategory =       newsletterPreferencyCategoryRepository.getByCode(codes[0].trim());
            // String label = "";
            // long idPref = Long.parseLong(codes[1].trim());
            // if (codes[0].equals("BOOKCATE")) {
                // Category categoryEnt = new Category();
                // categoryEnt.setId(idPref);
                // CategoryLang category = categoryLangRepository.getByCatLang(categoryEnt, lang);
                // logger.info("Found book" + category);
                // label = category.getName();
            // } else if (codes[0].equals("WRITERFA")) {
                // Writer writer = writerRepository.findOne(idPref);
                // logger.info("Found writer " + writer);
                // label = writer.getFullname();
            // } else {
                // NewsletterPreferencyLang newsletterPreferencyLang = newsletterPreferencyLangRepository.getByPrefAndLang(idPref, lang.getId());
                // logger.info("Found newsletterPreferencyLang " + newsletterPreferencyLang);
                // label = newsletterPreferencyLang.getLabel();
            // }
            // newsletterSubscriberPreferency.setPreferency(idPref);
            // newsletterSubscriberPreferency.setNewsletterSubscriberPreferencyPK(newsletterPreferencyCategory.getId(), newsletterSubscriber.getId());
            // newsletterSubscriberPreferency.setValue(label);
            // newsletterSubscriberPreferencyRepository.save(newsletterSubscriberPreferency);
        // }
    // }
    
    // private void sendSubscriptionEmail(NewsletterSubscriber newsletterSubscriber) throws Exception
    // {
        // HashMap<String, Object> mailData = new HashMap<String, Object>();
        // mailData.put("config", TPL_CONFIRM);
        // mailData.put("to", newsletterSubscriber.getEmail());
        // Map<String, Object> tplVars = new HashMap<String, Object>();
        // tplVars.put("login", newsletterSubscriber.getEmail());
        // tplVars.put("idCrypted", cryptograph.encrypt(Long.toString(newsletterSubscriber.getId())));
        // tplVars.put("idDecrypted", cryptograph.decrypt((String)tplVars.get("idCrypted")));
        // mailerTool.setMailData(mailData);
        // mailerTool.setVars(tplVars);
        // mailerTool.send();
    // }
}