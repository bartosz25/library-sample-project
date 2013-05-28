package com.example.library.model.entity;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.example.library.service.check.SubscriberAccountCheck;
import com.example.library.service.check.SubscriberAvatarCheck;
import com.example.library.service.check.SubscriberPasswordCheck;
import com.example.library.service.check.SubscriberRegisterCheck;
import com.example.library.validator.CSRFConstraint;
import com.example.library.validator.IsEqualLocally;
import com.example.library.validator.PasswordChecker;
import com.example.library.validator.UniqueRecordDb;
import com.example.library.validator.UniqueRecordDbCrossed;
import com.example.library.validator.UploadFile;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Entity
@Table(name = "subscriber")
@IsEqualLocally(firstValue = "password", secondValue = "passwordRepeated",  message = "{error.passwords.equal}", groups = {SubscriberRegisterCheck.class, SubscriberPasswordCheck.class})
@UniqueRecordDbCrossed(uniqueColumn = "oldEmail", oldColumn = "email", query = "SELECT s FROM Subscriber s WHERE email = :oldEmail AND email != :email", message = "{error.email.used}", groups = SubscriberAccountCheck.class)
@PasswordChecker(toSaltField = "login", passwordField = "oldPassword", query = "SELECT s FROM Subscriber s WHERE password = :password", queryUser = "SELECT s FROM Subscriber s WHERE login = :login", message = "{error.oldPassword.invalid}", groups = {SubscriberPasswordCheck.class})
@CSRFConstraint(message = "{error.csrf.invalid}", groups = {SubscriberPasswordCheck.class, SubscriberAccountCheck.class, SubscriberRegisterCheck.class, SubscriberAvatarCheck.class})
public class Subscriber extends ParentEntity implements Serializable {
    final Logger logger = LoggerFactory.getLogger(Subscriber.class);
    public static final int IS_NOT_BLACKLISTED = 0;
    public static final int IS_BLACKLISTED = 1;
    public static final int IS_NOT_CONFIRMED = 0;
    public static final int IS_CONFIRMED = 1;
    // number of books which subscriber can book
    public static final int BOOKINGS_NB = 5;
    private long id;
    private String login = null;
    private String password = null;
    private String passwordRepeated = null;
    private String email = null;
    private String oldEmail = null;
    private String oldPassword = null;
    private Date created = null;
    private String avatar = null;
    private CommonsMultipartFile avatarFile = null;
    private int confirmed = 0;
    private int blacklisted = 0;
    private int bookingNb = BOOKINGS_NB;
    private int revival;
    private String role = "ROLE_USER";
    private List<Question> questions;
    private List<Subscription> subscriptions;
    private List<Suggestion> suggestions;
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id_su")
    public long getId() {
        return id;
    }

    @NotEmpty(message = "{error.login.empty}", groups = SubscriberRegisterCheck.class)
    @Size(min = 1, max = 20, message = "{error.login.length}", groups = SubscriberRegisterCheck.class)
    @Pattern(regexp = "([A-Za-z0-9]+)", message = "{error.login.char}", groups = SubscriberRegisterCheck.class)
    @UniqueRecordDb(column = "login", query = "SELECT s FROM Subscriber s WHERE login = :login", parameter = "login", message = "{error.login.used}", groups = SubscriberRegisterCheck.class)
    @Column(name = "login_su")
    public String getLogin() {
        return login;
    }

    @NotEmpty(message = "{error.password.empty}", groups = {SubscriberRegisterCheck.class, SubscriberPasswordCheck.class})
    @Column(name = "password_su")
    public String getPassword() {
        return password;
    }

    @NotEmpty(message = "{error.email.empty}", groups = SubscriberRegisterCheck.class)
    @Email(message = "{error.email.format}", groups = SubscriberRegisterCheck.class)
    @UniqueRecordDb(column = "email", query = "SELECT s FROM Subscriber s WHERE email = :email", parameter = "email", message = "{error.email.used}", groups = SubscriberRegisterCheck.class)
    @Column(name = "email_su")
    public String getEmail() {
        return email;
    }

    @Temporal(TIMESTAMP)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_su")
    public Date getCreated() {
        return created;
    }

    @Column(name = "confirmed_su")
    public int getConfirmed() {
        return confirmed;
    }

    @Column(name = "blacklisted_su")
    public int getBlacklisted() {
        return blacklisted;
    }

    @Column(name = "booking_nb_su")
    public int getBookingNb() {
        return bookingNb;
    }

    @Column(name = "revival_su")
    public int getRevival() {
        return revival;
    }

    @Column(name = "avatar_su")
    public String getAvatar() {
        return avatar;
    }

    @Transient
    // max size = 500 kb
    @UploadFile(message = "{error.file.invalid}", groups = {SubscriberAvatarCheck.class}, acceptedTypes = {"image/jgp", "image/gif", "image/png", "image/jpeg"}, maxSize = 512000l, isRequired = true)
    public CommonsMultipartFile getAvatarFile() {
        return avatarFile;
    }

    @Transient
    @NotEmpty(message = "{error.repeatedPassword.empty}", groups = {SubscriberRegisterCheck.class, SubscriberPasswordCheck.class})
    public String getPasswordRepeated() {
        return passwordRepeated;
    }

    @Transient
    @NotEmpty(message = "{error.email.empty}", groups = SubscriberAccountCheck.class)
    @Email(message = "{error.email.format}", groups = SubscriberAccountCheck.class)
    public String getOldEmail() {
        return oldEmail;
    }

    @Transient
    @NotEmpty(message = "{error.oldPassword.empty}", groups = SubscriberPasswordCheck.class)
    public String getOldPassword() {
        return oldPassword;   
    }

    @Transient
    public String getRole() {
        return role;
    }

    @OneToMany(mappedBy = "subscriber")
    public List<Question> getQuestions() {
        return questions;
    }

    @OneToMany(mappedBy = "subscriber")
    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    @OneToMany(mappedBy = "subscriber")
    public List<Suggestion> getSuggestions() {
        return suggestions;
    }

    public boolean ifConfirmed() {
logger.info("===================> checking " + confirmed + " and " + IS_CONFIRMED);
        return (confirmed == IS_CONFIRMED);
    }

    public boolean ifBlacklisted() {
logger.info("=====================> checking " + blacklisted + " and " + IS_BLACKLISTED);
        return (blacklisted == IS_BLACKLISTED);
    }

    public static boolean canBorrow(int borrowedBooks) {
        return borrowedBooks > 0;
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

    public void setCreated(Date created) {
        if (created == null) created = new Date();
        this.created = created;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setAvatarFile(CommonsMultipartFile avatarFile) {
        this.avatarFile = avatarFile;
    }

    public void setBlacklisted(int blacklisted) {
        this.blacklisted = blacklisted;
    }

    public void setPasswordRepeated(String passwordRepeated) {
        this.passwordRepeated = passwordRepeated;
    }

    public void setRevival(int revival) {
        this.revival = revival;
    }

    public void setOldEmail(String oldEmail) {
        if (oldEmail == null || oldEmail.equals("")) oldEmail = email;
        this.oldEmail = oldEmail;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setBookingNb(int bookingNb) {
        this.bookingNb = bookingNb;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public void setSuggestions(List<Suggestion> suggestions) {
        this.suggestions = suggestions;
    }

    public String toString() {
        return "Subscriber[id : "+id+", login : "+login+", email : "+email+""
        +", created : "+created+", confirmed : "+confirmed+", blacklisted : "+blacklisted
        +", passwordRepeated : "+passwordRepeated+", oldEmail : "+oldEmail+", bookingNb : "+bookingNb+", token = "+token+", action="+action+"]";
    }
}