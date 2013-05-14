package library.controller.backend;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import library.annotation.LoggedUser;
import library.controller.MainController;
import library.model.entity.Admin;
import library.model.entity.Reply;
import library.model.repository.QuestionRepository;
import library.security.AuthenticationUserDetails;
import library.security.CSRFProtector;
import library.service.ReplyService;
import library.service.check.GeneralGroupCheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
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

@RequestMapping("/backend/reply")
@Controller
public class BackendReplyController extends MainController {
    final Logger logger = LoggerFactory.getLogger(BackendReplyController.class);
    private final String addBinding = "org.springframework.validation.BindingResult.reply";
    @Autowired
    private ReplyService replyService;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private CSRFProtector csrfProtector;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'REPLY_ADD')")
    @RequestMapping(value = "/write/{question}", method = RequestMethod.GET)
    public String write(@ModelAttribute("reply") Reply reply,  @PathVariable long question, Model layout,
    HttpServletRequest request) {
        Map<String, Object> layoutMap = layout.asMap();
        if (layoutMap.containsKey("errors")) {
            layout.addAttribute(addBinding, layoutMap.get("errors"));
        }
        try {
            csrfProtector.setIntention("b-reply-write");
            reply.setToken(csrfProtector.constructToken(request.getSession()));
            logger.info("Generated token " + reply.getToken());
            reply.setAction(csrfProtector.getIntention());
        } catch (Exception e) {
            logger.error("An exception occured on creating CSRF token", e);
            // TODO : faire la redirection
        }
        return "replyAdd";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'REPLY_ADD')")
    @RequestMapping(value = "/write/{question}", method = RequestMethod.POST)
    public String writeHandle(@ModelAttribute("reply") @Validated({GeneralGroupCheck.class}) Reply reply, 
    BindingResult binRes, @PathVariable long question, @LoggedUser AuthenticationUserDetails user, Model layout, 
    RedirectAttributes redAtt) {
        logger.info("Received POST request " + reply);
        if (binRes.hasErrors()) {
            redAtt.addFlashAttribute("error", true);
            redAtt.addFlashAttribute("reply", reply);
            redAtt.addFlashAttribute("errors", binRes);
        } else {
            try {
                reply.setAdmin(conversionService.convert(user, Admin.class));
                reply.setQuestion(questionRepository.findOne(question));
                replyService.addNew(reply);
                redAtt.addFlashAttribute("success", true);
            } catch (Exception e) { 
                binRes.addError(getExceptionError("reply"));
                redAtt.addFlashAttribute("error", true);
                redAtt.addFlashAttribute("reply", reply);
                redAtt.addFlashAttribute("errors", binRes);
            }
        }
        return "redirect:/backend/reply/write/"+question;
    }
}