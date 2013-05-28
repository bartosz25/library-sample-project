package com.example.library.converter;

import com.example.library.model.entity.Penalty;
import com.example.library.model.entity.PenaltyPK;
import com.example.library.model.repository.PenaltyRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

public class StringToPenaltyConverter implements Converter<String, Penalty> {
    final Logger logger = LoggerFactory.getLogger(StringToPenaltyConverter.class);
    @Autowired
    private PenaltyRepository penaltyRepository;
    
    public Penalty convert(String method) {
        logger.info("============> StringToPenaltyConverter = " + method);
        try {
            String[] idParts = method.split("-");
            long time = Long.parseLong(idParts[0].trim());
            long subscriber = Long.parseLong(idParts[1].trim());
            PenaltyPK penaltyPK = new PenaltyPK();
            penaltyPK.setTime(time);
            penaltyPK.setIdSubscriber(subscriber);
            Penalty penalty = penaltyRepository.findOne(penaltyPK);
            return penalty;
        } catch(NumberFormatException e) {
            logger.error("Exception when trying to convert String ("+method+")to long ", e);
        }
        return null;
    }
}