package library.tools;

import java.io.FileInputStream;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * TODO
 * - implémenter le BCC et le CC
 * - mettre les fichiers .properties dans le contexte au chargement de la webapp
 */ 
/**
 * Modification du comportement : 
 * - chargement des properties
 * - choix du contenu et du titre en fonction de la langue
 * - le reste sans changements
 */
public class MailerTool {
    final Logger logger = LoggerFactory.getLogger(MailerTool.class);
    private JavaMailSender mailSender;
    private String sender;
    private VelocityEngine velocityEngine;
    private LocaleResolver localeResolver;
    @Autowired
    private ServletContext servletContext;
    /**
     * Mail's template data (subject and body). Not empty keys "title" and "template" are obligatory. If they are empty,
     * an Exception is catched on setTemplate().
     * @var HashMap<String, String>
     */
    private Map<String, Object> mailData;
    /**
     * Variables used in template.
     * @var Map<String, Object>
     */
    private Map<String, Object> vars;
    
    public void setMailData(Map<String, Object> mailData) throws Exception {
        if(!mailData.containsKey("config") || ((String)mailData.get("config")).equals("")) throw new Exception("Vars must contain config value");
        this.mailData = mailData;
    }

    public void setVars(Map<String, Object> vars) {
        this.vars = vars;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public JavaMailSender getMailSender() {
        return mailSender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    public void setLocaleResolver(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    public LocaleResolver getLocaleResolver() {
        return localeResolver;
    }

    public void setServletContext(ServletContext servletContext) {
        logger.info("Setting servletContext to MailerTool " + servletContext);
        // System.out.println("Setting servletContext to MailerTool " + servletContext);
        this.servletContext = servletContext;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public String prepareTemplate(String bodyTpl) {
        return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, bodyTpl, vars);
    }

    public String prepareTitle(String titleTpl) {
        VelocityContext context = new VelocityContext();
        context.put("vars", vars);
        StringWriter writer = new StringWriter();
        Velocity.init();
        Velocity.evaluate(context, writer, "MAILER_TITLE",  titleTpl);
        return writer.toString();
    }
    
    public void send() throws Exception, MailException {
        final String mailReceiver = (String) mailData.get("to");
        Properties config = new Properties();
        try {
            String realPath = "";
            // for unit testing
            if (servletContext instanceof MockServletContext) {
                realPath = "D:/resin-4.0.32/webapps/ROOT/";
            } else {
                realPath = servletContext.getRealPath(System.getProperty("file.separator"));
            }
            // TODO : ici que pour les tests // realPath = "D:/resin-4.0.25/webapps/ROOT/";
            FileInputStream fileInputStream = new FileInputStream(realPath+System.getProperty("file.separator")+"WEB-INF"+System.getProperty("file.separator")+"views_mails"+System.getProperty("file.separator")+"config.properties");
            config.load(fileInputStream);
            fileInputStream.close();
        } catch (Exception e) {
           throw new Exception("Properties can't be found",e);
        }

        // TODO : pour récupérer REQUEST sans contrôleur :(mais il faut utiliser Mock... pour tests unitaires) http://stackoverflow.com/questions/6300812/get-the-servlet-request-object-in-a-pojo-class
        HttpServletRequest req = new MockHttpServletRequest();
        if (servletContext != null && !(servletContext instanceof MockServletContext)) {
            logger.info("=> Real servletContext found");
            ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            req = sra.getRequest();
            logger.info("=> Found request from servletRequestAttributes"+req);
        }

        Locale locale = RequestContextUtils.getLocale(req);
logger.info("=> Found locale " + locale);
        final String subject = config.getProperty("title_"+(String) mailData.get("config")+"_"+locale.getLanguage().toLowerCase());
        final String bodyTpl = config.getProperty("body_"+(String) mailData.get("config")+"_"+locale.getLanguage().toLowerCase());
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                mimeMessage.setRecipient(Message.RecipientType.TO, 
                                         new InternetAddress(mailReceiver));
                mimeMessage.setFrom(new InternetAddress(sender));
                mimeMessage.setSubject(prepareTitle(subject));
                mimeMessage.setText(prepareTemplate(bodyTpl));
            }
        };
        mailSender.send(preparator);
    }
}