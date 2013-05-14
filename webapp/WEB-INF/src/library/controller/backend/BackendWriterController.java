package library.controller.backend;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import library.controller.MainController;
import library.model.entity.Writer;
import library.security.CSRFProtector;
import library.service.WriterService;
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
 */
@Controller
@RequestMapping(value = "/backend/writer")
public class BackendWriterController extends MainController {
    final Logger logger = LoggerFactory.getLogger(BackendWriterController.class);
    private final String addBinding = "org.springframework.validation.BindingResult.writer";
    @Autowired
    private WriterService writerService;
    @Autowired
    private CSRFProtector csrfProtector;
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'WRITER_ADD')")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(@ModelAttribute("writer") Writer writer, Model layout, RedirectAttributes redAtt,
    HttpServletRequest request) {
        Map<String, Object> layoutMap = layout.asMap();
        if (layoutMap.containsKey("errors")) {
            layout.addAttribute(addBinding, layoutMap.get("errors"));
        }
        try {
            csrfProtector.setIntention("b-writer-new");
            writer.setToken(csrfProtector.constructToken(request.getSession()));
            logger.info("Generated token " + writer.getToken());
            writer.setAction(csrfProtector.getIntention());
        } catch (Exception e) {
            logger.error("An exception occured on creating CSRF token", e);
            // TODO : faire la redirection
        }
        return "writerAdd";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'WRITER_ADD')")
    @RequestMapping(value = "/add" ,method = RequestMethod.POST)
    public String addHandle(@ModelAttribute("writer")  @Validated({GeneralGroupCheck.class}) Writer writer,
    BindingResult binRes, Model layout, HttpServletRequest req, RedirectAttributes redAtt) {
        logger.info("Received POST request " + writer);
        if (binRes.hasErrors()) {
            redAtt.addFlashAttribute("error", true);
            redAtt.addFlashAttribute("writer", writer);
            redAtt.addFlashAttribute("errors", binRes);
        } else {
            try {
                writerService.addNew(writer);
                redAtt.addFlashAttribute("success", true);
            } catch (Exception e) { 
                binRes.addError(getExceptionError("writer"));
                redAtt.addFlashAttribute("error", true);
                redAtt.addFlashAttribute("writer", writer);
                redAtt.addFlashAttribute("errors", binRes);
            }
        }
        return "redirect:/backend/writer/add";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'WRITER_EDIT')")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable long id) {
        return "writerEdit";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'WRITER_DELETE')")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable long id) {
        return "writerDelete";
    }
}