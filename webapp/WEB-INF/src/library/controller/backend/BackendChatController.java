package library.controller.backend;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import library.annotation.LoggedUser;
import library.controller.MainController;
import library.model.entity.Admin;
import library.model.entity.Chat;
import library.model.entity.Subscriber;
import library.model.repository.SubscriberRepository;
import library.security.AuthenticationUserDetails;
import library.security.Cryptograph;
import library.service.ChatService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/backend/chat")
public class BackendChatController extends MainController {
    final Logger logger = LoggerFactory.getLogger(BackendChatController.class);
    @Autowired
    private ChatService chatService;
    @Autowired
    private ConversionService conversionService;
    @Autowired 
    private Cryptograph cryptograph;
    @Autowired
    private SubscriberRepository subscriberRepository;
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'CHAT_CONSULT')")
    @RequestMapping(value = "/consult", method = RequestMethod.GET)
    public String consult(@ModelAttribute("chat") Chat chat, @LoggedUser AuthenticationUserDetails user, 
    Model layout) {
        layout.addAttribute("chatKey", user.getId());
        return "consultChat";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'CHAT_CONSULT')")
    @RequestMapping(value = "/consult/{subscriberId}", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> consultHandle(@ModelAttribute("chat") @Valid  Chat chat, 
    BindingResult binRes, @LoggedUser AuthenticationUserDetails user, @PathVariable String subscriberId,
    RedirectAttributes redAtt) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        logger.info("Received POST request " + chat);
        if (binRes.hasErrors()) {
            jsonMap.put("added", false);
            StringBuffer msg = new StringBuffer();
            for (ObjectError error : binRes.getAllErrors()) {
                msg.append(error.getCode());
                msg.append(error.getDefaultMessage()); // this line is temporary
            }
            jsonMap.put("msg", msg.toString());
        } else {
            long id = 0;
            try {
                id = Long.parseLong(cryptograph.decrypt(subscriberId));
                Subscriber subscriber = subscriberRepository.findOne(id);
                logger.info("=======> found subscriber " + subscriber);
                chat = chatService.addNew(chat, subscriber, conversionService.convert(user, Admin.class));
                jsonMap.put("added", true);
                jsonMap.put("from", chat.getAdmin().getId());
                jsonMap.put("to", cryptograph.encrypt(Long.toString(chat.getSubscriber().getId())));
                jsonMap.put("message", chat.getText());
            } catch (Exception e) {
                logger.error("An error occured on transforming String ("+subscriberId+") to long", e);
                jsonMap.put("added", false);
                jsonMap.put("msg", "Conversion error");
            }
        }
        return jsonMap;
    }
}