package library.model.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
@Table(name = "writer_lang")
public class WriterLang extends ParentEntity implements Serializable {
    private WriterLangPK writerLangPK;
    private String type;
    private String value;
    private Lang lang;
    private Writer writer;
    private long transIdWriter;
    private long transIdLang;
    private Map<String, String> typeValueMap = new HashMap<String, String>();
    public enum Types {
        DESCRIPTION("desc", "description", "Description (biography)"),
        STYLE("styl", "style", "Style (writer's style)"),
        INFLUENCE("infl", "influence", "Influence (influences to his style)");

        private String dbCode, translateCode, defaultLabel;

        private Types(String dbCode, String translateCode, String defaultLabel) {
            this.dbCode = dbCode;
            this.translateCode = translateCode;
            this.defaultLabel = defaultLabel;
        }
        public String getDbCode() {
            return dbCode;
        }
        public String getTranslateCode() {
            return translateCode;
        }
        public String getDefaultLabel() {
            return defaultLabel;
        }
        public String toString() {
            return "Type : dbCode ("+dbCode+"), translateCode("+translateCode+"), defaultLabel("+defaultLabel+")";
        }
    }
    
    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "idWriter", column = @Column(name = "writer_id_wr", nullable = false)),
        @AttributeOverride(name = "idLang", column = @Column(name = "lang_id_la", nullable = false)),
        @AttributeOverride(name = "type", column = @Column(name = "type_wl", nullable = false)) })
    public WriterLangPK getWriterLangPK() {
        return writerLangPK;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lang_id_la", nullable = false, insertable = false, updatable = false)
    public Lang getLang() {
        return lang;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id_wr", nullable = false, insertable = false, updatable = false)
    public Writer getWriter() {
        return writer;
    }

    @Transient
    public String getType() {
        return type;
    }

    @Column(name = "value_wl")
    public String getValue() {
        return value;
    }

    @Transient 
    public long getTransIdWriter() {
        return transIdWriter;
    }

    @Transient
    public long getTransIdLang() {
        return transIdLang;
    }

    @Transient
    public Map<String, String> getTypeValueMap(Map<String, String> typeValueMap) {
        return typeValueMap;
    }

    public void setWriterLangPK(Lang lang, Writer writer, String type) {
        this.writerLangPK = new WriterLangPK();
        this.writerLangPK.setIdLang(lang.getId());
        this.writerLangPK.setIdWriter(writer.getId());
        this.writerLangPK.setType(type);
        setLang(lang);
        setWriter(writer);
    }

    public void setWriterLangPK(WriterLangPK writerLangPK) {
        this.writerLangPK = writerLangPK;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    public void setType(String type) {
        this.type = type;
        if (writerLangPK == null) {
            writerLangPK = new WriterLangPK();
        }
        writerLangPK.setIdLang(transIdLang);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setTransIdWriter(long transIdWriter) {
        this.transIdWriter = transIdWriter;
        writer = new Writer();
        writer.setId(transIdWriter);
        if (writerLangPK == null) {
            WriterLangPK writerLangPK = new WriterLangPK();
        }
        writerLangPK.setIdWriter(transIdWriter);
    }

    public void setTransIdLang(long transIdLang) {
        this.transIdLang = transIdLang;
        lang = new Lang();
        lang.setId(transIdLang);
        if (writerLangPK == null) {
            writerLangPK = new WriterLangPK();
        }
        writerLangPK.setIdLang(transIdLang);
    }

    public void setTypeValueMap(Map<String, String> typeValueMap) {
        this.typeValueMap = typeValueMap;
    }

    public static String getMapEntryKey(Lang lang, String type) {
        return lang.getId()+"-"+type;
    }

    public String toString() {
        return "WriterLang : lang ["+lang+"], writer ["+writer+"], type ["+type+"], value ["+value+"]";
        
    }
}