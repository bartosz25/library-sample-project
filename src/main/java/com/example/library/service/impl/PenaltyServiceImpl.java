package com.example.library.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.example.library.form.PenaltyForm;
import com.example.library.model.entity.Payment;
import com.example.library.model.entity.PaymentMethod;
import com.example.library.model.entity.Penalty;
import com.example.library.model.entity.Subscriber;
import com.example.library.model.repository.PaymentMethodRepository;
import com.example.library.model.repository.PaymentRepository;
import com.example.library.model.repository.PenaltyRepository;
import com.example.library.service.PenaltyService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service("penaltyService")
public class PenaltyServiceImpl implements PenaltyService {
    final Logger logger = LoggerFactory.getLogger(PenaltyServiceImpl.class);
    @Autowired
    private PenaltyRepository penaltyRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Penalty> saveNotValid(PenaltyForm penaltyForm) throws Exception {
        List<Penalty> penalties = new ArrayList<Penalty>();
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            PaymentMethod paymentMethod = paymentMethodRepository.findOne(penaltyForm.getModeChecked());
            for (Penalty penalty : penaltyForm.getPenaltiesChecked()) {
                penalty.setState(Penalty.STATE_PENDING);
                penalty.setPaymentMethod(paymentMethod);
                penalty = penaltyRepository.save(penalty);
                penalties.add(penalty);
            }
            transactionManager.commit(status);
        } catch(Exception e) {
            logger.error("An exception occured on saving PenaltyRepository", e);
            penalties = null;
            transactionManager.rollback(status);
            throw new Exception(e);
        }
        return penalties;
    }
    
    @Override
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'PENALTY_ACCEPT')")
    public List<Penalty> addPayment(List<Penalty> penalties, Payment payment) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            logger.info("=============> Saving penalties ("+penalties.size()+") : " + penalties);
            payment = paymentRepository.save(payment);
            for (int i = 0; i < penalties.size(); i++) {
                Penalty penalty = penalties.get(i);
                penalty.setPayment(payment);
                penalty.setState(Penalty.STATE_PAYED);
                penalty.setPaymentMethod(payment.getPaymentMethod());
                logger.info("Saving penalty = " + penalty);
                logger.info("Saving penalty's payment = " + payment);
                // penalties.remove(i);
                penaltyRepository.save(penalty);
            }
            transactionManager.commit(status);
        } catch(Exception e) {
            logger.error("An exception occured on saving PenaltyRepository", e);
            penalties = null;
            transactionManager.rollback(status);
        }
        return penalties;
    }
    
    @Override
    public Penalty getPenaltyFromCode(String reference, Subscriber subscriber) {
        try {
            String[] composedKey = reference.split("-");
            long time = Long.parseLong(composedKey[0]);
            logger.info("Looking for penalty with " + composedKey + " decrypted from " + reference);
            return penaltyRepository.getPenaltyByUserAndId(time, subscriber.getId());
        } catch(NumberFormatException e) {
            logger.error("NumberFormatException catched on converting id", e);
        }
        return null;
    }
    
    @Override
    public double getSumBySubscriber(Subscriber subscriber) {
        List<Penalty> penalties = penaltyRepository.findBySubscriber(subscriber);
        if(penalties != null && penalties.size() > 0) {
            return penaltyRepository.getSumBySubscriberId(subscriber.getId());
        }
        return 0.0d;
    }
}