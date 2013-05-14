package library.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import library.model.entity.Lang;
import library.model.repository.LangRepository;
import library.service.LangService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import org.springframework.webflow.execution.RequestContextHolder;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import javax.servlet.http.HttpServletRequest;
import org.springframework.webflow.execution.RequestContext;
import library.model.entity.PageContent;

@Service("langService")
@Repository
// @Transactional
public class LangServiceImpl implements LangService {
    final Logger logger = LoggerFactory.getLogger(LangServiceImpl.class);
    @Autowired
    private LangRepository langRepository;

    @Override
    public Map<String, Object> getViewCommunsFromRequest(HttpServletRequest request, Lang lang) {
        // TODO : request.getLocale() matchs always to default Locale and not to set Locale from LocaleResolver
        // request == null or lang == null : call comes from web flow
        if (request == null) {
            RequestContext context = RequestContextHolder.getRequestContext();
            request = ((HttpServletRequest) ((ServletExternalContext) context.getExternalContext()).getNativeRequest());
            logger.info("Got Locale : " + request.getLocale());
        }
        if (lang == null) {
            lang = getByLocale(request.getLocale());
        }
        ServletContext servletContext = request.getServletContext();

        Map<String, Object> viewMap = new HashMap<String, Object>();
        viewMap.put("categories", ((Map<String, List<String>>) servletContext.getAttribute("categories")).get(lang.getIso()));
        viewMap.put("pages", ((Map<String, Map<String, List<PageContent>>>) servletContext.getAttribute("pages")).get(lang.getIso()));

        return viewMap;
    }
    
    @Override
    public List<Lang> getAllLangs() {
        return langRepository.getAllLangs();
    }

    @Override
    public Map<Long, Lang> getAllLangsOrderedById() {
        List<Lang> langs = langRepository.getAllLangs();
        Map<Long, Lang> finalLangs = new HashMap<Long, Lang>();
        for (Lang lang : langs) {
            finalLangs.put(lang.getId(), lang);
        }
        return finalLangs;
    }
    
    @Override
    public Map<String, Lang> getAllLangsByIso() {
        logger.info("servlet context : getAllLangsByIso()");
        List<Lang> langs = langRepository.getAllLangs();
        logger.info("=======> " + langs);
        Map<String, Lang> langsIso = new HashMap<String, Lang>();
        for (Lang lang : langs) {
            logger.info("==============> lang " + lang);
            langsIso.put(lang.getIso(), lang);
        }
        return langsIso;
    }
    
    @Override
    public Map<Locale, Lang> getLangsForLocales() {
        Map<Locale, Lang> result = new HashMap<Locale, Lang>();
        for (Lang lang : langRepository.getAllLangs()) {
            result.put(new Locale(lang.getIso()), lang);            
        }
        return result;
    }
    
    @Override
    public Lang getByLocale(Locale locale) {
        if (locale == null) Locale.getDefault();
        logger.info("locale is " + locale);
        
        logger.info("============> uppercase : " + locale.getLanguage().toUpperCase());
        logger.info("============> locale : " + locale);
        Lang lang = null;
        lang = langRepository.getByIso(locale.getLanguage().toUpperCase());
        if (lang == null) lang = langRepository.getDefaultLang();
        return lang;
    }
}