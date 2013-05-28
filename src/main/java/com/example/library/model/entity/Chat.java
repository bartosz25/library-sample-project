package com.example.library.model.entity;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "chat")
public class Chat extends ParentEntity implements Serializable {
    public static final int STATE_NOT_READ = 0;
    public static final int STATE_READ = 1;
    private ChatPK chatPK;
    private String text;
    private int state;
    private Subscriber subscriber;
    private Admin admin;

    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "time", column = @Column(name = "time_ch", nullable = false)),
        @AttributeOverride(name = "idSubscriber", column = @Column(name = "subscriber_id_su", nullable = false)),
        @AttributeOverride(name = "idAdmin", column = @Column(name = "admin_id_ad", nullable = false))
    })
    public ChatPK getChatPK() {
        return chatPK;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id_su", nullable = false, insertable = false, updatable = false)
    public Subscriber getSubscriber() {
        return subscriber;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id_ad", nullable = false, insertable = false, updatable = false)
    public Admin getAdmin() {
        return admin;
    }

    @NotEmpty(message = "{error.chat.textEmpty}")
    @Size(min = 1, max = 1000, message = "{error.chat.textSize}")
    @Column(name="text_ch")
    public String getText() {
        return text;
    }

    @Column(name="state_ch")
    public int getState() {
        return state;
    }

    public void setChatPK(ChatPK chatPK) {
        this.chatPK = chatPK;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setChatPK(long time, Subscriber subscriber, Admin admin) {
        chatPK = new ChatPK();
        chatPK.setTime(time);
        chatPK.setIdSubscriber(subscriber.getId());
        chatPK.setIdAdmin(admin.getId());
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public String toString() {
        return "Chat [chatPK : "+chatPK+", text : "+text+"]";
    }
}