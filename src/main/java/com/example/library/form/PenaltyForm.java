package com.example.library.form;

import java.util.ArrayList;
import java.util.List;

import com.example.library.model.entity.ParentEntity;
import com.example.library.model.entity.PaymentMethod;
import com.example.library.model.entity.Penalty;
import com.example.library.model.entity.Subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PenaltyForm  extends ParentEntity {
    final Logger logger = LoggerFactory.getLogger(PenaltyForm.class);
    protected List<PaymentMethod> modes = new ArrayList<PaymentMethod>();
    protected long modeChecked;    
    protected List<Penalty> penalties;
    protected List<Penalty> penaltiesChecked;
    protected Subscriber subscriber;
    
    public void setModes(List<PaymentMethod> modes) {
        this.modes = modes;
    }

    public void setModeChecked(long modeChecked) {
        this.modeChecked = modeChecked;
    }

    public void setPenalties(List<Penalty> penalties) {
        this.penalties = penalties;
    }

    public void setPenaltiesChecked(List<Penalty> penaltiesChecked) {
        this.penaltiesChecked = penaltiesChecked;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public List<PaymentMethod> getModes() {
        return modes;
    }

    public long getModeChecked() {
        return modeChecked;
    }

    public List<Penalty> getPenalties() {
        return penalties;
    }

    public List<Penalty> getPenaltiesChecked() {
        return penaltiesChecked;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public String getCheckedInString() {
        String result = "";
        if (penaltiesChecked == null) return "";
        for (Penalty penalty : getPenaltiesChecked()) {
            result += penalty.getPenaltyPK()+",";
        }
        return result;
    }

    public String toString() {
        return "PenaltyForm [modeChecked : "+modeChecked+", penaltiesChecked : "+penaltiesChecked+", subscriber : "+subscriber+"]";
    }
}