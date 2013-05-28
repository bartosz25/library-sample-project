package com.example.library.model.entity;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="reply")
public class Reply extends ParentEntity implements Serializable {
    public static final int STATE_READ = 0;
    public static final int STATE_NEW = 1;
    private long id;
    private Admin admin;
    private Question question;
    private String content;
    private Date date;
    private int state;
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="id_re")
    public long getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id_ad")
    public Admin getAdmin() {
        return admin;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id_qu")
    public Question getQuestion() {
        return question;
    }

    @NotEmpty(message = "{error.reply.contentEmpty}")
    @Column(name="content_re")
    public String getContent() {
        return content;
    }

    @Temporal(TIMESTAMP)
    @NotNull(message = "{error.reply.dateNull}")
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @Column(name="date_re")
    public Date getDate() {
        return date;
    }

    @Min(value = 0, message = "{error.reply.stateMin}")
    @Max(value = 1, message = "{error.reply.stateMax}")
    @Column(name="state_re")
    public int getState() {
        return state;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(Date date) {
        if (date == null) date = new Date();
        this.date = date;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String toString() {
        return "Reply [id = "+id+", admin = "+admin+", question = "+question+","+
        "content ="+content+", date ="+date+", state ="+state+"]";
    }
}