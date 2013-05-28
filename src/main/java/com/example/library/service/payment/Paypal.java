package com.example.library.service.payment;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.example.library.model.entity.Subscription;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 * TODOS : 
 * - charger les informations de paiement à partir d'un fichier .properties ou bien de la base
 */
public class Paypal extends Payment {
    final Logger logger = LoggerFactory.getLogger(Paypal.class);
    private Map<String, Object> urlConfig;
    private final static String TEMPLATE_PAY = "paymentPaypal.vtl";
    private String business = "bartk3_1331237082_biz@gmail.com";
    private String url = "https://www.sandbox.paypal.com/cgi-bin/webscr";
    private com.example.library.model.entity.Payment payment;
    
    @Override
    public void setUrlConfig(Map<String, Object> urlConfig) throws Exception {
        if (!urlConfig.containsKey("order")) throw new Exception("An order parameter is obligatory");
        urlConfig.put("business", business);
        urlConfig.put("url", url);
        this.urlConfig = urlConfig;
    }
    
    @Override
    public String getUrlOrderAfter() {
        return "/payment/confirmation/"+urlConfig.get("order");
    }

    @Override
    public String getUrlPenaltyAfter() {
        return "/payment/penalty/confirmation/"+urlConfig.get("order");
    }

    @Override
    public String getUrlOrderNotification()
    {
        return "/payment/notify/order/"+urlConfig.get("order");
    }

    @Override
    public String getUrlPenaltyNotification() {
        return "/payment/notify/penalty/"+urlConfig.get("order");
    }

    @Override
    public String getUrlOrderOk() {
        return "/subscription/confirmation/"+urlConfig.get("order");
    }

    @Override
    public String getUrlPenaltyOk() {
        return "/penalty/confirmation/"+urlConfig.get("order");
    }

    @Override
    public String getUrlOrderError() {
        return "/subscription/error/"+urlConfig.get("order");
    }

    @Override
    public String getUrlPenaltyError() {
        return "/penalty/confirmation/"+urlConfig.get("order");
    }

    @Override
    public String getUrlOrderCancel() {
        return "/subscription/cancel/"+urlConfig.get("order");
    }

    @Override
    public String getUrlPenaltyCancel() {
        return "/penalty/cancel/"+urlConfig.get("order");
    }

    @Override
    public String renderPaymentTemplate() {   
        logger.info("VelocityEngine is " + urlConfig.get("engine"));
        return VelocityEngineUtils.mergeTemplateIntoString(((VelocityEngine)urlConfig.get("engine")), TEMPLATE_PAY , urlConfig);
    }
    
    @Override
    public com.example.library.model.entity.Payment getPaymentInst(Subscription subscription, HttpServletRequest request) throws Exception {
        // prevent agains make it twice
        if (this.payment != null) return payment;
        com.example.library.model.entity.Payment payment = new com.example.library.model.entity.Payment();
        double stringDouble;
        try {
            stringDouble = Double.parseDouble(request.getParameter("mc_gross").trim());
            if (stringDouble != subscription.getAmount()) throw new Exception("Paypal amount ("+stringDouble+") is not the same as subscription amount("+subscription.getAmount()+")");
            payment.setReference(request.getParameter("txn_id"));
            payment.setDate(new Date());
            payment.setPaymentMethod(subscription.getPaymentMethod());
            payment.setType(payment.getSubscriptionType());
            payment.setAmount(stringDouble);
            this.payment = payment;
            return this.payment;
        } catch (NumberFormatException e) {
            logger.error("Exception when trying to convert String ("+request.getParameter("mc_gross")+") to double", e);
        }
        return null;
    }
}