package library.model.entity;

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
import javax.persistence.Transient;

@Entity
@Table(name = "category_lang")
public class CategoryLang extends ParentEntity implements Serializable {
    private CategoryLangPK categoryLangPK;
    private Category category;
    private Lang lang;
    private String name;
    private long transIdCategory;    
    private long transIdLang;

    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "idCategory", column = @Column(name = "category_id_ca", nullable = false)),
        @AttributeOverride(name = "idLang", column = @Column(name = "lang_id_la", nullable = false)) })
    public CategoryLangPK getCategoryLangPK() {
        return categoryLangPK;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lang_id_la", nullable = false, insertable = false, updatable = false)
    public Lang getLang() {
        return lang;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id_ca", nullable = false, insertable = false, updatable = false)
    public Category getCategory() {
        return category;
    }

    @Transient 
    public long getTransIdCategory() {
        return transIdCategory;
    }

    @Transient
    public long getTransIdLang() {
        return transIdLang;
    }

    // Validation is included in library.validator.form.CategoryLangFormValidator class
    // @NotEmpty(message = "Value can't be empty")
    // @Size(min = 1, max = 255, message = "The length can't exced 255 chars")
    @Column(name = "name_cl")
    public String getName() {
        return name;
    }

    public void setCategoryLangPK(CategoryLangPK categoryLangPK) {
        this.categoryLangPK = categoryLangPK;
    }

    public void setCategoryLangPK(Lang lang, Category category) {
        this.categoryLangPK = new CategoryLangPK();
        this.categoryLangPK.setIdLang(lang.getId());
        this.categoryLangPK.setIdCategory(category.getId());
        setLang(lang);
        setCategory(category);
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTransIdCategory(long transIdCategory) {
        this.transIdCategory = transIdCategory;
        category = new Category();
        category.setId(transIdCategory);
        if (categoryLangPK == null) {
            categoryLangPK = new CategoryLangPK();
        }
        categoryLangPK.setIdCategory(transIdCategory);
    }

    public void setTransIdLang(long transIdLang) {
        this.transIdLang = transIdLang;
        lang = new Lang();
        lang.setId(transIdLang);
        if (categoryLangPK == null) {
            categoryLangPK = new CategoryLangPK();
        }
        categoryLangPK.setIdLang(transIdLang);
    }
    // Another way to put composed primary key
    /**
    @Embeddable
    private static class CategoryLangPK implements Serializable
    {
        private long idCategory;
        private long idLang;
        
        public CategoryLangPK()
        {
        
        }

        @Column(name = "category_id_ca")
        public long getIdCategory()
        {
            return idCategory;
        }
        @Column(name = "lang_id_la")
        public long getIdLang()
        {
            return idLang;
        }
        public void setIdCategory(long idCategory)
        {
            this.idCategory = idCategory;
        }
        public void setIdLang(long idLang)
        {
            this.idLang = idLang;
        }
    }
    */
    public String toString() {
        return "CategoryLang [ category : " + categoryLangPK.getIdCategory() + ", lang : " +
        categoryLangPK.getIdLang() + ", name : " + name + " ]";
    }
}