package library.security;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;

public class SaltCellar implements SaltSource {
    final Logger logger = LoggerFactory.getLogger(SaltCellar.class);
    private Map<String, String> saltData;
    @Autowired
    private ShaPasswordEncoder passwordEncoder;
    
    public void setSaltData(Map<String, String> saltData) {
        logger.info("Put new salt data " + saltData);
        this.saltData = saltData;
    }
    
    /**
     * Salt is always constructed on login property.
     * @access public
     * @param UserDetails user Connected user instance.
     * @return Object Salt used in password.
     */
    public Object getSalt(UserDetails user) {
        logger.info("Getting salted password from UserDetails instance " + user);
        return getSaltFromString(user.getUsername());
    }
    
    public String getSaltFromString(String value) {
        if (value == null) return "-";
        StringBuffer salt = new StringBuffer();
        String[] loginParts = value.split("");
        for (int i = 0; i < loginParts.length; i++) {
            logger.info("Looking for " + loginParts[i]);
            if (!loginParts[i].equals("") && saltData.containsKey(loginParts[i].toLowerCase())) {
                salt.append(saltData.get(loginParts[i].toLowerCase()));
            }
        }
        logger.info("Generated salt = " + new String(salt));
        // logger.info("Encoded password = " + passwordEncoder.encodePassword("bartosz", "a"));
        logger.info("Encoded password salt = " + passwordEncoder.encodePassword("bartosz", new String(salt)));
        logger.info("Encoded password salt for admin = " + passwordEncoder.encodePassword("admin", new String(salt)));
        return new String(salt);
    }
}