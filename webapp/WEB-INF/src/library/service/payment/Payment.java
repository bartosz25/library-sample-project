package library.service.payment;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import library.model.entity.Subscription;

public abstract class Payment {
    protected Map<String, Object> urlConfig;

    public void setUrlConfig(Map<String, Object> urlConfig) throws Exception {
        if (!urlConfig.containsKey("order")) throw new Exception("An order parameter is obligatory");
        this.urlConfig = urlConfig;    
    }

    public String getUrlOrderAfter() {
        return "/subscription/confirmation/"+urlConfig.get("order");
    }

    public String getUrlPenaltyAfter() {
        return "/penalty/confirmation/"+urlConfig.get("order");
    }

    public String getUrlOrderNotification() {
        return "";
    }

    public String getUrlPenaltyNotification() {
        return "";
    }

    public String getUrlOrderOk() {
        return "";
    }

    public String getUrlPenaltyOk() {
        return "";
    }

    public String getUrlOrderError() {
        return "";
    }

    public String getUrlPenaltyError() {
        return "";
    }

    public String getUrlOrderCancel() {
        return "";
    }

    public String getUrlPenaltyCancel() {
        return "";
    }

    public String renderPaymentTemplate() {
        return "";
    }
    
    public library.model.entity.Payment getPaymentInst(Subscription subscription, HttpServletRequest request) throws Exception {
        return null;
    }
}