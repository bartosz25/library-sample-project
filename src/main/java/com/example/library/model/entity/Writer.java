package com.example.library.model.entity;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.DATE;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * TODO : 
 * - valider la date (Pattern ne marche pas car "The annotation @Pattern is disallowed for the return type of this method.")
 * - gérer la date de décès qui peut être nulle
 */

@Entity
@Table(name = "writer")
public class Writer extends ParentEntity implements Serializable {
    final Logger logger = LoggerFactory.getLogger(Writer.class);
    private long id;
    private String firstname = null;
    private String familyname = null;
    private Date born = null;
    private Date dead = null;
    private String fullname;
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id_wr")
    public long getId() {
        return id;
    }

    @NotEmpty(message = "{error.writer.firstNameEmpty}")
    @Size(min = 1, max = 20, message = "{error.writer.firstNameSize}")
    @Column(name = "firstname_wr")
    public String getFirstname() {
        return firstname;
    }

    @NotEmpty(message = "{error.writer.familyNameEmpty}")
    @Size(min = 1, max = 30, message = "{error.writer.familyNameSize}")
    @Column(name = "familyname_wr")
    public String getFamilyname() {
        return familyname;
    }

    @Temporal(DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Column(name = "born_wr")
    public Date getBorn() {
        return born;
    }

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Column(name = "dead_wr")
    public Date getDead() {
        return dead;
    }

    @Transient
    public String getFullname() {
        if (fullname == null) setFullname(firstname, familyname);
        return fullname;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setFamilyname(String familyname) {
        this.familyname = familyname;
    }

    public void setBorn(Date born) {
        this.born = born;
    }

    public void setDead(Date dead) {
        this.dead = dead;
    }

    public void setFullname(String firstname, String familyname) {
        this.fullname = firstname + " " + familyname;
    }

    public String toString() {
        return "Writer [id : "+id+", firstname : "+firstname+", familyname : "+familyname+ 
               " , born : , "+born+" dead : "+dead;
    }
    
    @Override
    public boolean equals(Object w) {
        Writer writer = (Writer) w;
        logger.info("============> Comparing " + writer + " with " + id);
        return (writer.getId() == id);
    }
}