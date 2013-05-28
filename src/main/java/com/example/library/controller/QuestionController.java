package com.example.library.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.example.library.annotation.LocaleLang;
import com.example.library.annotation.LoggedUser;
import com.example.library.model.entity.Lang;
import com.example.library.model.entity.Question;
import com.example.library.model.entity.Subscriber;
import com.example.library.security.AuthenticationFrontendUserDetails;
import com.example.library.security.CSRFProtector;
import com.example.library.service.QuestionService;
import com.example.library.service.check.GeneralGroupCheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/**
 * TODO : 
 * - voir les aspects sécuritaires du Spring (XSS, CSRF etc.)
 */

@RequestMapping("/question")
@Controller
public class QuestionController extends MainController {
    final Logger logger = LoggerFactory.getLogger(QuestionController.class);
    private final String addBinding = "org.springframework.validation.BindingResult.question";
    @Autowired
    private QuestionService questionService;
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private CSRFProtector csrfProtector;

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/write", method = RequestMethod.GET)
    public String write(@ModelAttribute("question") Question question, 
    @LoggedUser AuthenticationFrontendUserDetails user, Model layout, HttpServletRequest request,
    @LocaleLang Lang lang) {
        Map<String, Object> layoutMap = layout.asMap();
        if (layoutMap.containsKey("errors")) {
            layout.addAttribute(addBinding, layoutMap.get("errors"));
        }
        try {
            csrfProtector.setIntention("question-write");
            question.setToken(csrfProtector.constructToken(request.getSession()));
            logger.info("Generated token " + question.getToken());
            question.setAction(csrfProtector.getIntention());
        } catch (Exception e) {
            logger.error("An exception occured on creating CSRF token", e);
            // TODO : faire la redirection
        }
        layout.addAttribute("communs", getViewCommunMap(lang));
        return "questionWrite";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/write", method = RequestMethod.POST)
    public String writeHandle(@ModelAttribute("question") @Validated({GeneralGroupCheck.class}) Question question, BindingResult binRes, 
    @LoggedUser AuthenticationFrontendUserDetails user, RedirectAttributes redAtt, @LocaleLang Lang lang) {
        logger.info("Received POST request " + question);
        if (binRes.hasErrors()) {
            redAtt.addFlashAttribute("error", true);
            redAtt.addFlashAttribute("question", question);
            redAtt.addFlashAttribute("errors", binRes);
        } else {
            try {
                question.setLang(lang);
                question.setSubscriber(conversionService.convert(user, Subscriber.class));
                questionService.addNew(question);
                redAtt.addFlashAttribute("success", true);
            } catch (Exception e) {
                binRes.addError(getExceptionError("question"));
                redAtt.addFlashAttribute("error", true);
                redAtt.addFlashAttribute("question", question);
                redAtt.addFlashAttribute("errors", binRes);
            }
        }
        return "redirect:/question/write";
    }
}