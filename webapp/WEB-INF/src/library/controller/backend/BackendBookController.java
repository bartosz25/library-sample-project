package library.controller.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import library.controller.MainController;
import library.editor.BookCheckedPropertyEditor;
import library.form.BookForm;
import library.model.entity.BookCopy;
import library.model.entity.BookLang;
import library.model.entity.Category;
import library.model.entity.Lang;
import library.model.entity.Writer;
import library.security.CSRFProtector;
import library.service.BookService;
import library.service.CategoryService;
import library.service.LangService;
import library.service.WriterService;
import library.validator.form.BookFormValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * TODO :
 * - traduire les libellés
 */
@Controller
@RequestMapping(value = "/backend/book")
public class BackendBookController extends MainController {
    final Logger logger = LoggerFactory.getLogger(BackendBookController.class);
    private final String addBinding = "org.springframework.validation.BindingResult.bookForm";
    @Autowired
    private BookService bookService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private WriterService writerService;
    @Autowired
    private LangService langService;
    @Autowired
    private CSRFProtector csrfProtector;
    @Autowired
    private MessageSource messageSource;

    @InitBinder
    public void bindForm(WebDataBinder binder) {
        binder.registerCustomEditor(List.class, "categoriesChecked", new BookCheckedPropertyEditor<Category>(List.class, BookCheckedPropertyEditor.TYPE_CATEGORIES));
        binder.registerCustomEditor(List.class, "writersChecked", new BookCheckedPropertyEditor<Writer>(List.class, BookCheckedPropertyEditor.TYPE_WRITERS));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'BOOK_ADD')")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(@ModelAttribute("bookForm") BookForm bookForm, Model layout, RedirectAttributes redAtt, 
    HttpServletRequest request) {
        logger.info("==================> Received back bookForm " + bookForm);
        if (bookForm.getCategories() == null) bookForm.setCategories(categoryService.getAll());
        if (bookForm.getWriters() == null) bookForm.setWriters(writerService.getAll());
        if (bookForm.getTranslations() == null) {
            List<Lang> langs = langService.getAllLangs();
            BookLang bookLang = new BookLang();
            Map<String, Object> translationsInfo = new HashMap<String, Object>();
            Map<Long, Map<String, Object>> translMap = new HashMap<Long, Map<String, Object>>();
            for (Lang lang : langs) {
                for (String key : bookLang.getLabelCodes().keySet()) {
                    translationsInfo.put(key, "");
                }
                translMap.put(lang.getId(), translationsInfo);
            }
            bookForm.setTranslations(translMap);
        }
        if (bookForm.getCopies() == null) {
            bookForm.setCopies(
                new ArrayList<BookCopy>()
                {{
                    add(new BookCopy());
                }}
            );
        }
        try {
            csrfProtector.setIntention("bookAdd");
            bookForm.setToken(csrfProtector.constructToken(request.getSession()));
            bookForm.setAction(csrfProtector.getIntention());
            logger.info("Generated token " + bookForm.getToken());
        } catch (Exception e) {
            logger.error("An exception occured on creating CSRF token", e);
        }
        
        Map<String, Object> layoutMap = layout.asMap();
        if (layoutMap.containsKey("errors")) {
            layout.addAttribute(addBinding, layoutMap.get("errors"));
        }
        layout.addAttribute("bookConditions", BookCopy.getConditions());
        layout.addAttribute("bookStates", BookCopy.getStates());
        logger.info("=============> Created bookForm instance " + bookForm);
        return "bookAdd";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'BOOK_ADD')")
    @RequestMapping(value = "/add" ,method = RequestMethod.POST)
    public String addHandle(@ModelAttribute("bookForm") BookForm bookForm, Model layout, 
    RedirectAttributes redAtt, HttpServletRequest request) {
        logger.info("=============> Received BookForm " + bookForm);
        // make some validation before
        DataBinder binder = new DataBinder(bookForm);
        binder.setValidator(new BookFormValidator(csrfProtector, request, messageSource));
        binder.validate();
        BindingResult results = binder.getBindingResult();
        logger.info("==================> After BookForm validation " + results);
        if (results.hasErrors()) {
            redAtt.addFlashAttribute("error", true);
            redAtt.addFlashAttribute("bookForm", bookForm);
            redAtt.addFlashAttribute("errors", results);
            logger.info("errors found = " + bookForm);
        } else {
            try {
                bookService.save(bookForm);
                redAtt.addFlashAttribute("success", true);
            } catch (Exception e) {
                results.addError(getExceptionError("bookForm"));
                redAtt.addFlashAttribute("error", true);
                redAtt.addFlashAttribute("bookForm", bookForm);
                redAtt.addFlashAttribute("errors", results);
            }
        }
        return "redirect:/backend/book/add";
    }
}