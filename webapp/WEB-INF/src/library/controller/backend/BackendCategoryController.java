package library.controller.backend;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import library.controller.MainController;
import library.model.entity.Category;
import library.security.CSRFProtector;
import library.service.CategoryService;
import library.service.check.GeneralGroupCheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/**
 * TODO : 
 * - delete must interact with jQuery yes-no popup
 * - rajouter la gestion des traductions sur la même page
 */
@Controller
@RequestMapping(value = "/backend/category")
public class BackendCategoryController extends MainController {
    final Logger logger = LoggerFactory.getLogger(BackendCategoryController.class);
    private final String addBinding = "org.springframework.validation.BindingResult.category";
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CSRFProtector csrfProtector;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'CATEGORY_ADD')")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(@ModelAttribute("category") Category category, Model layout, RedirectAttributes redAtt, 
    HttpServletRequest request) {
        Map<String, Object> layoutMap = layout.asMap();
        if (layoutMap.containsKey("errors")) {
            layout.addAttribute(addBinding, layoutMap.get("errors"));
        }
        try {
            csrfProtector.setIntention("b-category-new");
            category.setToken(csrfProtector.constructToken(request.getSession()));
            logger.info("Generated token " + category.getToken());
            category.setAction(csrfProtector.getIntention());
        } catch (Exception e) {
            logger.error("An exception occured on creating CSRF token", e);
            // TODO : faire la redirection
        }
        return "categoryAdd";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'CATEGORY_ADD')")
    @RequestMapping(value = "/add" ,method = RequestMethod.POST)
    public String addHandle(@ModelAttribute("category") @Validated({GeneralGroupCheck.class}) Category category,
    BindingResult binRes, Model layout, HttpServletRequest req,  RedirectAttributes redAtt) {
        logger.info("Received POST request " + category);
        if (binRes.hasErrors()) {
            redAtt.addFlashAttribute("error", true);
            redAtt.addFlashAttribute("category", category);
            redAtt.addFlashAttribute("errors", binRes);
        } else {
            try {
                categoryService.save(category);
                redAtt.addFlashAttribute("success", true);
            } catch (Exception e) {
                binRes.addError(getExceptionError("category"));
                redAtt.addFlashAttribute("error", true);
                redAtt.addFlashAttribute("category", category);
                redAtt.addFlashAttribute("errors", binRes);
            }
        }
        return "redirect:/backend/category/add";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'CATEGORY_EDIT')")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable long id) {
        return "categoryEdit";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'CATEGORY_DELETE')")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable long id) {
        return "categoryDelete";
    }
}