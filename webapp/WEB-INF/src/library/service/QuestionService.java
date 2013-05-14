package library.service;

import library.model.entity.Question;

public interface QuestionService {
    // public List<Question> findById();
    // public Question save(Question question);
    public Question addNew(Question question) throws Exception;
    public Question markAsRead(Question question);
    // public void delete(Question question);
}