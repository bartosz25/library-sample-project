package com.example.library.controller.backend;

import com.example.library.controller.MainController;
import com.example.library.model.entity.Question;
import com.example.library.model.repository.QuestionRepository;
import com.example.library.service.QuestionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/backend/question")
@Controller
public class BackendQuestionController extends MainController {
    final Logger logger = LoggerFactory.getLogger(BackendQuestionController.class);
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionService questionService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'QUESTION_READ')")
    @RequestMapping(value = "/read/{id}", method = RequestMethod.GET)
    public String read(@PathVariable long id, Model layout) {
        Question question = questionRepository.findOne(id);
        layout.addAttribute("question", question);
        questionService.markAsRead(question);
        return "questionRead";
    }
}