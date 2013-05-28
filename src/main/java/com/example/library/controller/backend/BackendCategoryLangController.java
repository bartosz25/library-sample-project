package com.example.library.controller.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.example.library.controller.MainController;
import com.example.library.form.CategoryLangForm;
import com.example.library.model.entity.Category;
import com.example.library.model.entity.CategoryLang;
import com.example.library.model.entity.Lang;
import com.example.library.security.CSRFProtector;
import com.example.library.service.CategoryLangService;
import com.example.library.service.CategoryService;
import com.example.library.service.LangService;
import com.example.library.validator.form.CategoryLangFormValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * TODO : 
 * - delete must interact with jQuery yes-no popup
 * - remplacer error.getDefaultMessage() par error.getCode() et récupération d'un message traduit en fonction de la langue
 * - voir si toutes les méthodes dans CategoryLang et WriterLang sont utilisées
 */
 /**
  * Apparemment la validation de collections est impossible sous Spring : 
  * http://fcamblor.wordpress.com/2012/05/21/valider-ses-pojos-avec-bean-validation-spring-mvc-et-jquery/
  * http://stackoverflow.com/questions/8828457/spring-3-jsr-303-bean-validation-and-validating-collection
  * Il est donc préférable de développer un validateur spécifique pour cette partie.
  */
@Controller
@RequestMapping(value = "/backend/category-lang/")
public class BackendCategoryLangController extends MainController {
    final Logger logger = LoggerFactory.getLogger(BackendCategoryLangController.class);
    @Autowired
    private LangService langsService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryLangService categoryLangService;
    @Autowired
    private CSRFProtector csrfProtector;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'CATEGORYLANG_ADD')")
    @RequestMapping(value = "/add/{id}", method = RequestMethod.GET)
    public String add(@ModelAttribute("categoryLangForm") CategoryLangForm categoryLangForm, 
    @PathVariable long id, Model layout, HttpServletRequest request) {
        Category category = categoryService.getById(id);
        layout.addAttribute("category", category);
        logger.info("================> Found category " + category);
        List<CategoryLang> categoryLangList = new ArrayList<CategoryLang>();
        Map<Long, Lang> langs = langsService.getAllLangsOrderedById();
        // checks if any error occured after form submit and construct more friendly errors map
        Map<String, Object> layoutMap = layout.asMap();
        Map<String, Object> errorsMap = new HashMap<String, Object>();
        if (layoutMap.containsKey("errors")) {
            // Pattern pattern = Pattern.compile("categoryLang\\[.*\\]\\.name");
            // for(ObjectError error : ((BindingResult)layoutMap.get("errors")).getAllErrors())
            for (FieldError error : ((BindingResult)layoutMap.get("errors")).getFieldErrors()) {
                errorsMap.put(error.getField(), error.getDefaultMessage());
            }
            layout.addAttribute("errorsMap", errorsMap);
            logger.info("CATEGORY LANG FORM " + layoutMap.get("categoryLangForm"));
        } else {
            Map<Long, CategoryLang> catTranslated = categoryLangService.getTranslatedByCategory(category);
            logger.info("================> Found langs " + langs);
            for (Long key : langs.keySet()) {
                Lang lang = (Lang)langs.get(key);
                CategoryLang categoryLang = new CategoryLang();
                logger.info("======> setting lang " + lang);
                categoryLang.setTransIdCategory(category.getId());
                categoryLang.setTransIdLang(lang.getId());
                if (catTranslated.containsKey(lang.getId())) {
                    categoryLang.setName(catTranslated.get(lang.getId()).getName());
                    logger.info("================> Found categoryLang in Map " + catTranslated.get(lang.getId()));
                }
                categoryLangList.add(categoryLang);
            }
            categoryLangForm.setCategoryLang(categoryLangList);
        }
        layout.addAttribute("langs", langs);
        try {
            csrfProtector.setIntention("bookAdd");
            categoryLangForm.setToken(csrfProtector.constructToken(request.getSession()));
            categoryLangForm.setAction(csrfProtector.getIntention());
            logger.info("Generated token " + categoryLangForm.getToken());
        } catch (Exception e) {
            logger.error("An exception occured on creating CSRF token", e);
        }
        logger.info("====================> found categoryLangForm " + categoryLangForm);
        return "categoryLangAdd";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'CATEGORYLANG_ADD')")
    @RequestMapping(value = "/add/{id}", method = RequestMethod.POST)
    public String addHandle(@ModelAttribute("categoryLangForm") CategoryLangForm categoryLangForm, 
    @PathVariable  long id, Model layout, HttpServletRequest req,  RedirectAttributes redAtt, 
    HttpServletRequest request) {
        logger.info("POST ====================> found categoryLangForm " + categoryLangForm);
        DataBinder binder = new DataBinder(categoryLangForm);
        binder.setValidator(new CategoryLangFormValidator(csrfProtector, request));
        // bind to the target object
        // binder.bind(propertyValues);
        // validate the target object
        binder.validate();
        // get BindingResult that includes any validation errors
        BindingResult results = binder.getBindingResult();

        logger.info("Received POST request " + categoryLangForm);
        if (results.hasErrors()) {
            redAtt.addFlashAttribute("error", true);
            redAtt.addFlashAttribute("categoryLangForm", categoryLangForm);
            redAtt.addFlashAttribute("errors", results);
            logger.info("errors found = " + categoryLangForm);
        } else {
            try {
                categoryLangService.addNew(categoryLangForm.getCategoryLang());
                redAtt.addFlashAttribute("success", true);
            } catch (Exception e) {
                results.addError(getExceptionError("categoryLangForm"));
                redAtt.addFlashAttribute("error", true);
                redAtt.addFlashAttribute("categoryLangForm", categoryLangForm);
                redAtt.addFlashAttribute("errors", results);
            }
        }
        return "redirect:/backend/category-lang/add/"+id;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'CATEGORY_DELETE')")
    @RequestMapping(value = "/delete/{categoryId}/{langId}/{type}", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> delete(@PathVariable long categoryId, @PathVariable long langId, 
    @PathVariable String type) {
        Category category = new Category();
        category.setId(categoryId);
        Lang lang = new Lang();
        lang.setId(langId);
        CategoryLang categoryLang = categoryLangService.getByCatLang(category, lang);
        logger.info("=============> found categoryLang to delete " + categoryLang);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        if (type.equals("question")) {
            jsonMap.put("question", "Are you sur to delete this translated category ?");
            jsonMap.put("category", categoryLang);
        } else if(type.equals("do"))
        {
            // TODO : Delete category
        }
        return jsonMap;
    }
}