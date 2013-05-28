package com.example.library.controller;

import com.example.library.annotation.LocaleLang;
import com.example.library.model.entity.Lang;
import com.example.library.model.entity.PageContent;
import com.example.library.model.repository.PageContentRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ContentController extends MainController {
    final Logger logger = LoggerFactory.getLogger(ContentController.class);
    @Autowired
    private PageContentRepository pageContentRepository;
    
    @RequestMapping(value = "/s_{page}", method = RequestMethod.GET)
    public String showPage(@PathVariable String page, Model layout, @LocaleLang Lang lang) {
        PageContent pageContent = pageContentRepository.findByUrlAndLang(page, lang);
        logger.info("Found lang " + lang);
	    if (pageContent == null) {
	        return "redirect:/not-found";
	    }
        layout.addAttribute("page", pageContent);
        layout.addAttribute("communs", getViewCommunMap(lang));
        return "contentPage";
    }
}