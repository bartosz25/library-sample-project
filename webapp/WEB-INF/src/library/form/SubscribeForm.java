package library.form;

import static javax.persistence.TemporalType.DATE;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Temporal;

import library.model.entity.PaymentMethod;
import library.model.entity.Subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * TODO : 
 * - dans la vue remplacer la génération manuelle du <select /> par une génération automatique
 */
public class SubscribeForm  {
    final Logger logger = LoggerFactory.getLogger(SubscribeForm.class);
    private List<PaymentMethod> modes = new ArrayList<PaymentMethod>();
    // private long modeChecked;    
    private PaymentMethod modeChecked;    
    private Map<Integer, HashMap<String, Object>> types;
    private int typeChecked;
    private Subscriber subscriber;
    private Date startDate;
    
    public void setModes(List<PaymentMethod> modes) {
        this.modes = modes;
    }

    // public void setModeChecked(long modeChecked) {
    public void setModeChecked(PaymentMethod modeChecked) {
        this.modeChecked = modeChecked;
    }

    public void setTypes(Map<Integer, HashMap<String, Object>> types) {
        this.types = types;
    }

    public void setTypeChecked(int typeChecked) {
        this.typeChecked = typeChecked;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public List<PaymentMethod> getModes() {
        return modes;
    }

    // public long getModeChecked() {
    public PaymentMethod getModeChecked() {
        return modeChecked;
    }

    public Map<Integer, HashMap<String, Object>> getTypes() {
        return types;
    }

    public int getTypeChecked() {
        return typeChecked;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    @Temporal(DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd") public Date getStartDate()
    {
        return startDate;
    }

    public String toString() {
        return "SubscribeForm [modeChecked : "+modeChecked+", typeChecked : "+typeChecked+", subscriber : "+subscriber+", startDate "+startDate+", modes : " + modes+", types : " + types+"]";
    }
}