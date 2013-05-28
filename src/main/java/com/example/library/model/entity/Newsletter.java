package com.example.library.model.entity;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name="newsletter")
public class Newsletter extends ParentEntity implements Serializable {
    public static final int STATE_NOT_SEND = 0;
    public static final int STATE_SENDING = 1;
    public static final int STATE_ALMOST_ENDED = 2;
    public static final int STATE_SEND = 3;
    private final String separator = "|";
    private final String separatorRegex = "\\|";
    private long id;
    private Admin admin;
    private Date created;
    private Date sendTime;
    private int state;
    private String title;
    private String text;
    private String preferencies;
    private List<NewsletterSend> sendNewsletters;
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="id_ne")
    public long getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id_ad")
    public Admin getAdmin() {
        return admin;
    }

    @Temporal(TIMESTAMP)
    @NotNull(message = "{error.newsletter.dateEmpty}")
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @Column(name="created_ne")
    public Date getCreated() {
        return created;
    }

    @Temporal(TIMESTAMP)
    @NotNull(message = "{error.newsletter.sendTimeEmpty}")
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @Column(name="send_ne")
    public Date getSendTime() {
        return sendTime;
    }

    @Column(name="state_ne")
    public int getState() {
        return state;
    }

    @Column(name="title_ne")
    public String getTitle() {
        return title;
    }

    @Column(name="text_ne")
    public String getText() {
        return text;
    }

    @Column(name="preferencies_ne")
    public String getPreferencies() {
        return preferencies;
    }

    @OneToMany(mappedBy = "newsletter")
    public List<NewsletterSend> getSendNewsletters() {
        return sendNewsletters;
    }

    @Transient
    public List<String> getPreferenciesList() {
        List<String> preferenciesList = new ArrayList<String>();
        String[] prefs = preferencies.split(separatorRegex);
        for (String preferency : prefs) {
            if (!preferency.trim().equals("")) preferenciesList.add(preferency);
        }
        return preferenciesList;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public void setCreated(Date created) {
        if (created == null) created = new Date();
        this.created = created;
    }

    public void setSendTime(Date sendTime) {
        if (sendTime == null) sendTime = new Date();
        this.sendTime = sendTime;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSendNewsletters(List<NewsletterSend> sendNewsletters) {
        this.sendNewsletters = sendNewsletters;
    }

    public void setPreferencies(String preferencies) {
        this.preferencies = preferencies;
    }

    public void setPreferenciesFromList(List<String> preferencies) {
        String preferenciesString = "";
        int i = 0;
        for (String preferency : preferencies) {
            preferenciesString += preferency;
            ++i;
            if (i < preferencies.size()) preferenciesString += separator;
        }
        this.preferencies = preferenciesString;
    }

    public String toString() {
        return "Newsletter [id = "+id+", admin = "+admin+", title = "+title+","+
        "text ="+text+", created ="+created+", state ="+state+", sendTime = "+sendTime+"]";
    }
}