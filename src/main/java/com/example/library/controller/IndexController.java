package com.example.library.controller;

import com.example.library.annotation.LocaleLang;
import com.example.library.model.entity.Lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController extends MainController {
    final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(value = "/access-denied", method = {RequestMethod.POST, RequestMethod.GET})    
    public String accessDenied() {
        return "accessDenied";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model layout, @LocaleLang Lang lang) {
        // Lang lang = getLangFromLocaleResolver(request);
        logger.info("=========> Found lang " + lang);
        layout.addAttribute("communs", getViewCommunMap(lang));
        return "homePage";
    }
}