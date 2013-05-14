package library.form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackendPenaltyForm extends PenaltyForm  {
    final Logger logger = LoggerFactory.getLogger(BackendPenaltyForm.class);
    private String reference;
    
    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    public String toString() {
        return "BackendPenaltyForm [modeChecked : "+modeChecked+", penaltiesChecked : "+penaltiesChecked+", reference : "+reference+"]";
    }
}