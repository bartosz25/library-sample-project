package library.model.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="placement")
public class Placement extends ParentEntity implements Serializable {
    private Long id;
    private String name;
    private String place;
    private String code;
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="id_pl")
    public Long getId() {
        return id;
    }

    @Column(name="name_pl")
    public String getName() {
        return name;
    }

    @Column(name="place_pl")
    public String getPlace() {
        return place;
    }

    @Column(name="place_code_pl")
    public String getCode() {
        return code;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setCode(String code) {
        this.code = code;
    }
}