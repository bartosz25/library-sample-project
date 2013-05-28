package com.example.library.model.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "admin")
public class Admin extends ParentEntity implements Serializable {
    final Logger logger = LoggerFactory.getLogger(Admin.class);
    private static final int ADMIN_ROLE = 1;
    private long id;
    private String login = null;
    private String password = null;
    private String email = null;
    private String role = "";
    private int isAdmin = 0;
    private Date created = null;
    private Date lastLogged = null;
    private List<Reply> replies;
    private List<Log> logs;
    private List<Newsletter> newsletters;
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id_ad")
    public long getId() {
        return id;
    }

    @Column(name = "login_ad")
    public String getLogin() {
        return login;
    }

    @Column(name = "password_ad")
    public String getPassword() {
        return password;
    }

    @Column(name = "email_ad")
    public String getEmail() {
        return email;
    }

    @Column(name = "role_ad")
    public String getRole() {
        return role;
    }

    @Column(name = "is_admin_ad")
    public int getIsAdmin() {
        return isAdmin;
    }

    @Column(name = "created_ad")
    public Date getCreated() {
        return created;
    }

    @Column(name = "last_logged_ad")
    public Date getLastLogged() {
        return lastLogged;
    }

    @OneToMany(mappedBy = "admin")
    public List<Reply> getReplies() {
        return replies;
    }

    @OneToMany(mappedBy = "admin")
    public List<Log> getLogs() {
        return logs;
    }

    @OneToMany(mappedBy = "admin")
    public List<Newsletter> getNewsletters() {
        return newsletters;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setCreated(Date created) {
        if (created == null) created = new Date();
        this.created = created;
    }

    public void setLastLogged(Date lastLogged) {
        if (lastLogged == null) lastLogged = new Date();
        this.lastLogged = lastLogged;    
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

    public void setNewsletters(List<Newsletter> newsletters) {
        this.newsletters = newsletters;
    }

    public boolean hasAdminRole() {
        return (isAdmin == ADMIN_ROLE);
    }

    public String toString() {
        return "Admin [id : "+id+", login : "+login+", email : "+email+""
        +", created : "+created+", lastLogged : "+lastLogged+", role : "+role+"]";
    }
}