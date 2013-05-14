package library.resolver;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import library.annotation.LocaleLang;
import library.model.entity.Lang;
import library.service.LangService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.support.RequestContextUtils;

public class LocaleLangResolver implements HandlerMethodArgumentResolver {
    final Logger logger = LoggerFactory.getLogger(LocaleLangResolver.class);
    @Autowired
    private LangService langService;
    
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LocaleLang.class);
    }

    @Override
    public Lang resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer, 
        NativeWebRequest nativeWebRequest, WebDataBinderFactory binderFactory) {
        Locale locale = RequestContextUtils.getLocale((HttpServletRequest) nativeWebRequest.getNativeRequest());
        return langService.getByLocale(locale);
    }
}