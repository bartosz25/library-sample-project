package com.example.library.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.example.library.model.entity.Lang;
import javax.servlet.http.HttpServletRequest;

public interface LangService {
    public List<Lang> getAllLangs();
    public Map<Long, Lang> getAllLangsOrderedById();
    public Map<String, Lang> getAllLangsByIso();
    public Map<Locale, Lang> getLangsForLocales();
    public Map<String, Object> getViewCommunsFromRequest(HttpServletRequest request, Lang lang);
    public Lang getByLocale(Locale locale);
}