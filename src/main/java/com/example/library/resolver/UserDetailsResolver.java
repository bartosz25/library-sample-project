package com.example.library.resolver;

import java.security.Principal;

import com.example.library.annotation.LoggedUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * TODO : 
 * - méthode supportsParameter doit vérifier la classe User et non pas mettre true en dur
 */

public class UserDetailsResolver implements HandlerMethodArgumentResolver {
    final Logger logger = LoggerFactory.getLogger(UserDetailsResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoggedUser.class);
    }
    
    @Override
    public User resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer, 
        NativeWebRequest nativeWebRequest, WebDataBinderFactory binderFactory) throws Exception {
        logger.info("Found getDeclaringClass = " + methodParameter.getDeclaringClass());
        logger.info("Found getMethod = " + methodParameter.getMethod());
        logger.info("Found getMethodAnnotations = " + methodParameter.getMethodAnnotations());
        Principal principal = nativeWebRequest.getUserPrincipal();
        if (principal == null) return null;
        User user = (User) ((Authentication) principal).getPrincipal();
        if (user == null) throw new Exception("User can't be found from request");
        else return user;
    }
}