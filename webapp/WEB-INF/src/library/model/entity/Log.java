package library.model.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "log")
public class Log implements Serializable {
    private long id;
    private Admin admin;
    private Date date;
    private String action;
    
    @Id
    @GeneratedValue(strategy = IDENTITY)    
    @Column(name="id_lo")
    public long getId() {
        return id;
    }

    public Admin getAdmin() {
        return admin;
    }

    @Column(name="date_lo")
    public Date getDate() {
        return date;
    }

    @Column(name="action_lo")
    public String getAction() {
        return action;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String toString() {
        return "Logger [id :"+id+" , admin :"+admin+" , date :"+date+" , action :"+admin+" ]";
    }
}