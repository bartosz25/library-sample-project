package library.form;
 
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Temporal;

import library.model.entity.ParentEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

public class NewsletterWriteForm  extends ParentEntity implements Serializable {
    final Logger logger = LoggerFactory.getLogger(NewsletterWriteForm.class);
    private String title;
    private String text;
    private Date startTime;
    private List<String> preferencies;
    private Map<String, Map<String, Map<String, Object>>> categories;
    private Map<String, String> translations;
    
    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    @Temporal(TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    public Date getStartTime() {
        return startTime;
    }

    public Map<String, Map<String, Map<String, Object>>> getCategories() {
        return categories;
    }

    public Map<String, String> getTranslations() {
        return translations;
    }

    public List<String> getPreferencies() {
        return preferencies;
    }

    public String[] getPreferenciesArray() {
        String[] strArray = new String[preferencies.size()];
        return preferencies.toArray(strArray);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setCategories(Map<String, Map<String, Map<String, Object>>> categories) {
        this.categories = categories;
    }

    public void setTranslations(Map<String, String> translations) {
        this.translations = translations;
    }

    public void setPreferencies(List<String> preferencies) {
        this.preferencies = preferencies;
    }

    public String toString() {
        return "NewsletterWriteForm [title : "+title+", text :"+text+", preferencies " + preferencies + " ]";
    }
}