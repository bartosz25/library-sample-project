package com.example.library.model.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "book_copy")
public class BookCopy extends ParentEntity implements Serializable {
    // public static final int COPY_NEW = 1;
    // public static final int COPY_USED = 2;
    // public static final String COPY_NEW_LABEL = "book.copy.new";
    // public static final String COPY_USED_LABEL = "book.copy.used";
    // public static final int STATE_BOOKED = 0;
    // public static final int STATE_AVAILABLE = 1;
    private long id;
    private Book book;
    private String code;
    private int condition;
    private int state;
    private enum Conditions  {
        COPY_NEW(1, "new", "book.copy.new"),
        COPY_USED(2, "used", "book.copy.used");
        
        private int id;
        private String label;
        private String code;
        
        private Conditions(int id, String label, String code) {
            this.id = id;
            this.label = label;
            this.code = code;
        }
        public int getId() {
            return id;
        }
        public String getLabel() {
            return label;
        }
        public String getCode() {
            return code;
        }
    }
    private enum States  {
        STATE_BOOKED(0, "booked", "book.copy.booked"),
        STATE_AVAILABLE(1, "available", "book.copy.available");
        
        private int id;
        private String label;
        private String code;
        
        private States(int id, String label, String code) {
            this.id = id;
            this.label = label;
            this.code = code;
        }
        public int getId() {
            return id;
        }
        public String getLabel() {
            return label;
        }
        public String getCode() {
            return code;
        }
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id_bc")
    public long getId() {
        return id;
    }

    @ManyToOne
    @JoinColumn(name = "book_id_bo")
    public Book getBook() {
        return book;
    }

    @NotEmpty(message = "{error.bookCopy.codeEmpty}")
    @Size(min = 1, max = 10, message = "{error.bookCopy.codeSize}")
    @Column(name = "code_bc")
    public String getCode() {
        return code;
    }

    @Min(value = 1, message = "{error.bookCopy.conditionMin}")
    @Max(value = 2, message = "{error.bookCopy.conditionMax}")
    @Column(name = "condition_bc")
    public int getCondition() {
        return condition;
    }

    @Min(value = 0, message = "{error.bookCopy.stateMin}")
    @Max(value = 1, message = "{error.bookCopy.stateMax}")
    @Column(name = "state_bc")
    public int getState() {
        return state;
    }

    @Transient
    public String getBookLabelCondition() {
        if (condition == Conditions.COPY_NEW.getId()) return Conditions.COPY_NEW.getLabel();
        return Conditions.COPY_USED.getLabel();
    }

    @Transient
    public boolean isAvailableForBook() {
        return (state == States.STATE_AVAILABLE.getId());
    }

    @Transient
    public static States[] getStates() {
        return States.values();
    }

    @Transient
    public static int getStateAvailableId() {
        return States.STATE_AVAILABLE.getId();
    }

    @Transient
    public static int getBookedStateId() {
        return States.STATE_BOOKED.getId();
    }

    @Transient
    public static Conditions[] getConditions() {
        return Conditions.values();
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String toString() {
        return "BookCopy [id = " + id + ", book = " + book + ", code = " + code + ", condition = " + condition +
        "state = " + state + "]";
    }
}