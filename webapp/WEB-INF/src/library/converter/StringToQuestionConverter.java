package library.converter;

import library.model.entity.Question;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

public class StringToQuestionConverter implements Converter<String, Question> {
    final Logger logger = LoggerFactory.getLogger(StringToQuestionConverter.class);
    
    public Question convert(String question) {
        logger.info("============> StringToQuestion = " + question);
        try {
            long id = Long.parseLong(question.trim());
            Question questionInst = new Question();
            questionInst.setId(id);
            return questionInst;
        } catch (NumberFormatException e) {
            logger.error("Exception when trying to convert String ("+question+")to long ", e);
        }
        return null;
    }
}