package com.example.library.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.PasswordEncoder;

/**
 * CSRF's protection flow strategy is following : 
 * 1. User visits the site at the first time. We give to him an unique salt, known only
 *    by his session. This salt expires after some time. 
 * 2. This class possess three other attributs to generate the protection token : 
 *    - intention (String) : name of targeted action (for exemple SubscriberController.updateAccount(3) where 3 is parameter of subscriber's id)
 *    - hashStart (String) : webapp hash added at the beginning of generated token
 *    - hashEnd (String) : webapp hash added at the end of generated token
 * 3. Method compareTokens checks if generated and submitted tokens are the same. If not, 
 *    it causes InvalidCSRFTokenException
 */
/**
 * TODOS : 
 * - faire capted InvalidCSRFTokenException
 * - voir si PasswordEncoder est vraiement injecté (voir aussi dans SubscriberService)
 * - voir si la validité du sel dans la session est correcte
 */
public class CSRFProtector {
    final Logger logger = LoggerFactory.getLogger(CSRFProtector.class);
    public final String TOKEN_KEY = "token";
    public final String TOKEN_SALT_KEY = "localToken";
    public final String TOKEN_VALIDITY_KEY = "localValidityToken";
    private final int tokenLength = 30;
    private long expirationTime;
    private String hashStart;
    private String hashEnd;
    private String intention;
    private String alphaChain = "abcdefghijklmnopqrstuvwxyz0123456789";
    private PasswordEncoder passwordEncoder;   
    
    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public void setHashStart(String hashStart) {
        this.hashStart = hashStart;
    }

    public void setHashEnd(String hashEnd) {
        this.hashEnd = hashEnd;
    }

    public void setIntention(String intention) {
        logger.info("Setting intention " + intention);
        this.intention = intention;
    }

    public void setPasswordEncoder (PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public String getHashStart() {
        return hashStart;
    }

    public String getHashEnd() {
        return hashEnd;
    }

    public String getIntention() {
        return intention;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }
    
    public String constructToken(HttpSession session) throws Exception {
        if (session == null) throw new Exception("Session can't be null");
        if (!checkSessionTokenValidity(session)) setLocalToken(session);
        Map<String, String> tokens = new HashMap<String, String>();
        if (session.getAttribute(TOKEN_KEY) != null) tokens = (Map<String, String>) session.getAttribute(TOKEN_KEY);
        if (tokens.containsKey(intention) && tokens.get(intention) != null) return tokens.get(intention);
        String token = passwordEncoder.encodePassword(hashStart+intention+hashEnd, 
                                session.getAttribute(TOKEN_SALT_KEY));
        tokens.put(intention, token);
        session.setAttribute(TOKEN_KEY, tokens);
        logger.info("Tokens map is " + tokens);
        return token;
    }

    public boolean compareTokens(String requestToken, HttpSession session) throws Exception {
        if (session == null) throw new Exception("Session can't be null");
        if (!checkSessionTokenValidity(session)) setLocalToken(session);
        String userToken = constructToken(session);
        logger.info("=> Comparing requestToken ("+requestToken+") with userToken("+userToken+")");
        return requestToken.equals(userToken);
    }
    
    public void setLocalToken(HttpSession session) {
        // TODO : Pour expliquer d'où ça vient http://stackoverflow.com/questions/10516786/shifted-by-negative-number-in-java
        String token = "";
        Random ran = new Random();
        byte[] r = new byte[tokenLength];
        ran.nextBytes(r);
        for (int i = 0; i < tokenLength; i++) {
            token += (alphaChain.charAt(r[i] & 0x1f));
        }
        logger.info("=> Generated token " + token);
        session.setAttribute(TOKEN_VALIDITY_KEY, (new Date().getTime() + expirationTime));
        logger.info("=> Expiration time " + session.getAttribute(TOKEN_VALIDITY_KEY));
        session.setAttribute(TOKEN_SALT_KEY, token);
    }

    public boolean checkSessionTokenValidity(HttpSession session) {
        return (session.getAttribute(TOKEN_VALIDITY_KEY) != null && (new Date().getTime() < (Long)session.getAttribute(TOKEN_VALIDITY_KEY)));
    }
    
    public String getCSRFToken(HttpSession session, String intention) {
        if (this.intention == null) setIntention(intention);
        Map<String, String> tokens = (Map<String, String>)session.getAttribute(TOKEN_KEY);
        return tokens.get(intention);
    }
}