package com.example.library.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.example.library.model.entity.Lang;
import com.example.library.service.LangService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.ObjectError;

// TODO : voir méthode http://static.springsource.org/spring/docs/3.0.x/api/org/springframework/web/servlet/support/WebContentGenerator.html#cacheForSeconds(javax.servlet.http.HttpServletResponse,%20int)

@Controller
public abstract class MainController {
    final Logger logger = LoggerFactory.getLogger(MainController.class);
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private LangService langService;

    protected ObjectError getExceptionError(String object) {
        return new ObjectError(object, new String[] {"error.general.exception"}, null, "");
    }

    protected Map<String, Object> getViewCommunMap(Lang lang) {
        return langService.getViewCommunsFromRequest(request, lang);
    }
}