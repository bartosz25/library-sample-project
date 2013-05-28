package com.example.library.validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * TODOS : 
 * - si une erreur s'est produite, retourner exactement le type d'erreur concerné (taille du fichier, extension invalide)
 */
public class UploadFileValidator implements ConstraintValidator<UploadFile, CommonsMultipartFile> {
    final Logger logger = LoggerFactory.getLogger(UploadFileValidator.class);
    private List<String> acceptedTypes = new ArrayList<String>();
    private long maxSize;
    private boolean isRequired;
    
    public void initialize(UploadFile constraintAnnotation) {
        Collections.addAll(acceptedTypes, constraintAnnotation.acceptedTypes());
        maxSize = constraintAnnotation.maxSize();
        isRequired = constraintAnnotation.isRequired();
    }

    public boolean isValid(CommonsMultipartFile file, ConstraintValidatorContext context) {
        logger.info("======> validating image file " + file);
        boolean result = true;
        try {
            if (file == null) return false;
            logger.info("=====> contentType "+file.getContentType());
            logger.info("=====> name "+file.getName());
            logger.info("=====> getSize "+file.getSize());
            logger.info("=====> originalFilename "+file.getOriginalFilename());
            logger.info("=====> file "+file);
            logger.info("=====> acceptedTypes " + acceptedTypes);
            if (!acceptedTypes.contains(file.getContentType())) {
                logger.info("=> File's content type is not accepted");
                result = false;
            } else if (file.getSize() > maxSize) {
                logger.info("=> File is too big");
                result = false;
            } else if (isRequired && file.getOriginalFilename().equals("")) {
                logger.info("=> Filename is empty");
                result = false;
            }
        } catch (Exception e) {
            logger.error("An exception occured on validating unique record", e);
            result = false;
        }
        return result;
    }
}