// package com.example.library.controller;

// import java.io.IOException;
// import java.util.Date;

// import org.atmosphere.cpr.AtmosphereRequest;
// import org.atmosphere.cpr.AtmosphereResource;
// import org.atmosphere.cpr.AtmosphereResource.TRANSPORT;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestMethod;
// import org.springframework.web.bind.annotation.ResponseBody;

// @Controller
// @RequestMapping(value = "chat")
// public class ChatController {
	// @ResponseBody
	// @RequestMapping(method = RequestMethod.GET)
	// public void onRequest(AtmosphereResource r) throws IOException {
		// AtmosphereRequest req = r.getRequest();
		
		// if (req.getHeader("negotiating") == null) {
			// r.resumeOnBroadcast(r.transport() == TRANSPORT.LONG_POLLING).suspend();
		// } else {
			// r.getResponse().getWriter().write("OK");
		// }
	// }
	
	// @ResponseBody
	// @RequestMapping(method = RequestMethod.POST)
	// public void post(AtmosphereResource r) throws IOException {
		// AtmosphereRequest req = r.getRequest();
		
		// String body = req.getReader().readLine().trim();
	
		// String author = body.substring(body.indexOf(":") + 2, body.indexOf(",") - 1);
		// String message = body.substring(body.lastIndexOf(":") + 2, body.length() - 2);
	
		// r.getBroadcaster().broadcast(new Data(author, message).toString());
	// }

    // private final static class Data {

        // private final String text;
        // private final String author;

        // public Data(String author, String text) {
            // this.author = author;
            // this.text = text;
        // }

        // public String toString() {
            // return "{ \"text\" : \"" + text + "\", \"author\" : \"" + author + "\" , \"time\" : " + new Date().getTime() + "}";
        // }
    // }
// }


package com.example.library.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import com.example.library.annotation.LocaleLang;
import com.example.library.annotation.LoggedUser;
import com.example.library.model.entity.Chat;
import com.example.library.model.entity.Lang;
import com.example.library.model.entity.Subscriber;
import com.example.library.security.AuthenticationFrontendUserDetails;
import com.example.library.security.Cryptograph;
import com.example.library.service.ChatService;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// http://repo2.maven.org/maven2/org/eclipse/jetty/ 
/**
 * Chat's development will be decomposed on some steps : 
 * - 1. Form creation.
 * - 2. Database saving (with choosing of administrator who will reply to the user).
 * - 3. Saving form on AJAX.
 * 4. Getting message from the database on ChatCometService.
 * 5. Publishing the message to all subscribers.
 * 6. Publishing the private message.
 * 7. Blacklisting some words.
 * 8. Block some user if they abuse of blacklisted words.
 * 9. Add some administrator checks (availability : an administrator can't lead no more than 10 conversations). If nobody is available, don't save the message and return an error message).
 * 10. After entry to the chat room, get 30 last messages of logged user.
 */
 /**
  * TODOS : 
  * - si le message (en fonction de la langue) est vide, retourner le message par défaut - pour tous les formulaires de validation
  * - éviter un null ici : chatService.addNew(chat, conversionService.convert(user, Subscriber.class), null) car s'il s'agit d'une conversation en continu, il faut faire en sorte qu'elle ne soit pas coupée - L'optimal serait de fonctionner avec des rooms privés
  */
// TODO : voir pour CSRF
@RequestMapping("/chat")
@Controller
public class ChatController extends MainController {
    final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final String addBinding = "org.springframework.validation.BindingResult.chat";
    @Autowired
    private ChatService chatService;
    @Autowired
    private ConversionService conversionService;
    @Autowired 
    private Cryptograph cryptograph;

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/room", method = RequestMethod.GET)
    public String room(@ModelAttribute("chat") Chat chat, @LoggedUser AuthenticationFrontendUserDetails 
	user, Model layout, @LocaleLang Lang lang) {
        layout.addAttribute("chatKey", cryptograph.encrypt(Long.toString(user.getId())));
        layout.addAttribute("communs", getViewCommunMap(lang));
        return "chatRoom";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/room", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> roomHandle(@ModelAttribute("chat") @Valid  Chat chat, 
    BindingResult binRes, @LoggedUser AuthenticationFrontendUserDetails user, RedirectAttributes redAtt) {
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
            chat = chatService.addNew(chat, conversionService.convert(user, Subscriber.class), null);
            jsonMap.put("added", true);
            jsonMap.put("from", cryptograph.encrypt(Long.toString(chat.getSubscriber().getId())));
            jsonMap.put("to", chat.getAdmin().getId());
            jsonMap.put("message", chat.getText());
        }
        return jsonMap;
    }
}