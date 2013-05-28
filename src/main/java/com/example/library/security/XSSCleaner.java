package com.example.library.security;

import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODOS : 
 * - remplacer les > et < solitaires par &gt; et &lt;
 * - devrait aussi remplacer 
     outputs.put("<IMG SRC=\"javascript:alert('XSS')\"", "<IMG SRC=\"javascript:alert('XSS')\""); par une chaîne vide
 * 
 */
public class XSSCleaner {
    final Logger logger = LoggerFactory.getLogger(XSSCleaner.class);
    /**
     * Array with tags accepted by webapp.
     * @access private
     * @return List<String>
     */
    private List<String> acceptedTags;
    private String regexAcceptedTags = "";
    
    public void setAcceptedTags(List<String> acceptedTags) {
        logger.info("=> Setting accepted tags " + acceptedTags);
        this.acceptedTags = acceptedTags;
        StringBuffer tagsBuffer = new StringBuffer();
        int tagsSize = this.acceptedTags.size();
        for (int i = 0; i < tagsSize; i++) {
            tagsBuffer.append(this.acceptedTags.get(i));
            if ((i + 1) < tagsSize) tagsBuffer.append("|");
        }
        regexAcceptedTags = tagsBuffer.toString();
    }

    public List<String> getAcceptedTags() {
        return acceptedTags;
    }
    
    public String filterString(String toFilter, List<String> acceptedTags) {
        String filteredString = "";
        // Pattern strictPattern = Pattern.compile("\\<.*?\\>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        Pattern strictPattern = Pattern.compile("((?!\\<(|/)("+regexAcceptedTags+")\\>)(\\<.*?\\>))", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        return strictPattern.matcher(toFilter).replaceAll("").trim();
    }

    public String filterString(String toFilter) {
        return filterString(toFilter, acceptedTags);
    }
}