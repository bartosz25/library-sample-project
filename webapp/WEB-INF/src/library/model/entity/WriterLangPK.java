package library.model.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class WriterLangPK implements Serializable {

    private long idWriter;
    private long idLang;
    private String type;

    public WriterLangPK() {}

    // @Column(name = "writer_id_wr")
    public long getIdWriter() {
	    return idWriter;
    }

    // @Column(name = "lang_id_la")
    public long getIdLang() {
	    return idLang;
    }

    // @Column(name = "type_wl")
    public String getType() {
        return type;
    }

    public void setIdWriter(long idWriter) {
	    this.idWriter = idWriter;
    }

    public void setIdLang(long idLang) {
	    this.idLang = idLang;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (object.getClass() != getClass()) return false;
        
        WriterLangPK writerLangPk = (WriterLangPK) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(idWriter, writerLangPk.getIdWriter())
            .append(idLang, writerLangPk.getIdLang())
            .append(type, writerLangPk.getType())
            .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(idWriter)
            .append(idLang)
            .append(type)
            .toHashCode();
    }
}