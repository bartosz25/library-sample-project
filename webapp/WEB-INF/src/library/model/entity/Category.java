package library.model.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * TODO : 
 * - faire en sorte que la validation fonctionne pour UniqueRecordDb
 * - surcharger equals pour toutes les classes d'entité (comparer sur la clé primaire uniquement) - sinon, malgré posséder l'identifiant de la requête POST/GET, on devra chercher l'instance complète dans la base à chaque fois
 */

@Entity
@Table(name = "category")
public class Category extends ParentEntity implements Serializable {
    final Logger logger = LoggerFactory.getLogger(Category.class);
    private long id;
    private String alias;
    private List<CategoryLang> categoryLangs; 
 
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id_ca")
    public long getId() {
        return id;
    }

    @NotEmpty(message = "{error.category.aliasEmpty}")
    @Size(min = 1, max = 10, message = "{error.category.aliasSize}")
    // @UniqueRecordDb(column = "alias", query = "SELECT c FROM Category c WHERE alias = :alias", parameter = "alias", message = "Alias is already taken. Please choose the another one.", groups = {Default.class})
    @Column(name = "alias_ca")
    public String getAlias() {
        return alias;
    }

    @OneToMany(mappedBy = "category")
    public List<CategoryLang> getCategoryLangs() {
        return categoryLangs;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setCategoryLangs(List<CategoryLang> categoryLangs) {
        categoryLangs = this.categoryLangs;
    }

    public String toString() {
        return "Category : [id : "+id+", alias : "+alias+"]";
    }
    /**
     * TODO NOTE : 
     * Si cette méthode n'est pas surchargée, Spring considère les deux instances suivantes 
     * comme étant différentes : 
     * Category c1 = new Category(); c1.setId(2l); c1.setAlias("Biographie");
     * Category c2 = new Category(); c2.setId(2l);
     * Ce qui est vrai au fond. Cependant, pour les tests, on va comparer uniquement sur id.
     * Cette comparaison peut nous éviter d'aller chercher les données dans la base à chaque,
     * même si l'on n'a besoin que de l'identifiant.
     * Dans le paramètre on doit passer Object au lieu du Category pour surcharger cette 
     * méthode.
     * VOIR : http://docs.oracle.com/javase/1.4.2/docs/api/java/lang/Object.html#equals%28java.lang.Object%29
     */
    public boolean equals(Object c) {
        Category category = (Category)c;
        logger.info("============> Comparing " + category + " with " + id);
        return (category.getId() == id);
    }
}