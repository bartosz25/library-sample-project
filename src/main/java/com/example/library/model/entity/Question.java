package com.example.library.model.entity;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.example.library.service.check.GeneralGroupCheck;
import com.example.library.validator.CSRFConstraint;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="question")
@CSRFConstraint(message = "{error.csrf.invalid}", groups = {GeneralGroupCheck.class})
public class Question extends ParentEntity implements Serializable {
    public static final int STATE_READ = 0;
    public static final int STATE_NEW = 1;
    public static final int STATE_REPLIED = 2;
    private long id;
    private Subscriber subscriber;
    private Lang lang;
    private String title;
    private String content;
    private Date date;
    private int state;
    private List<Reply> replies;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="id_qu")
    public long getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id_su")
    public Subscriber getSubscriber() {
        return subscriber;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lang_id_la")
    public Lang getLang() {
        return lang;
    }

    @NotEmpty(message = "{error.question.titleEmpty}")
    @Column(name="title_qu")
    public String getTitle() {
        return title;
    }

    @NotEmpty(message = "{error.question.contentEmpty}")
    @Column(name="content_qu")
    public String getContent() {
        return content;
    }

    @Temporal(TIMESTAMP)
    // @NotNull(message = "{error.question.dateNull}")
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @Column(name="date_qu")
    public Date getDate() {
        return date;
    }

    @Min(value = 0, message = "{error.question.stateMin}")
    @Max(value = 2, message = "{error.question.stateMax}")
    @Column(name="state_qu")
    public int getState() {
        return state;
    }

    @OneToMany(mappedBy = "question")
    public List<Reply> getReplies() {
        return replies;
    }

    public boolean wasReplied() {
        return state == STATE_REPLIED;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

    public String toString() {
        return "Question [id = "+id+", subscriber = "+subscriber+", lang = "+lang+", title = "+title+","+
        "content = "+content+", date = "+date+", state = "+state+"]";
    }
}