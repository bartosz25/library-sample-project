package library.context;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import library.service.CategoryService;
import library.service.LangService;
import library.service.PageContentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
// TODO : expliquer l'annotation @Component org.springframework.stereotype.Component;
public class LibraryContextLoaderListener extends ContextLoaderListener {
    final Logger logger = LoggerFactory.getLogger(LibraryContextLoaderListener.class);
    
    @Override
    public void contextInitialized(ServletContextEvent event) {
        logger.info("Context initialization called ");
        super.contextInitialized(event);

        WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());

        LangService langService = (LangService) springContext.getBean("langService");
        CategoryService categoryService = (CategoryService) springContext.getBean("categoryService");
        PageContentService pageContentService = (PageContentService) springContext.getBean("pageContentService");

        ServletContext servletContext = event.getServletContext();
        servletContext.setAttribute("langs", langService.getAllLangsByIso());
        servletContext.setAttribute("categories", categoryService.getAndGroupCategoriesByLang());        
        servletContext.setAttribute("pages", pageContentService.getAndGroupPagesByLang());

    }
}