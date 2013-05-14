package library.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import library.annotation.LocaleLang;
import library.annotation.LoggedUser;
import library.model.entity.Lang;
import library.model.entity.Subscriber;
import library.model.entity.Suggestion;
import library.security.AuthenticationFrontendUserDetails;
import library.security.CSRFProtector;
import library.service.SuggestionService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/**
 * Suggestions d'achat ont pour but d'indiquer les désirs des clients. Chaque client en compte
 * peut soumettre une suggestion d'achat. 
 * Avant de soumettre la suggestion, on vérifie si la suggestion avec le même titre fonctionne (chercher par LIKE). Si des références trouvées ne sont pas exactement les mêmes, on suggère au client de modifier, avant d'envoyer finalement le formulaire.
 * Par contre, si aucune référence est trouvée, on rajoute le formulaire immédiatement. 
 * Si une référence exacte est trouvée, alors on met à jour le nombre de demandes.
 * On ne veut que sauvegarder le premier soumetteur. C'est pourquoi il faut faire attention dans le cas de la mise à jour du nombre de demandes, que subscriber_id_su ne soit pas surchargé avec le enième demandeur.
 * Une fois suggestion validée par le backoffice (date de livraison saisie, état modifié), la liste des suggestions bientôt disponibles sera disponible sur le site.
 */
 /**
  * TODO : 
  * - faire en sorte que les suggestions puissent être rajoutés une fois par personne connectée
  */
@RequestMapping("/suggestions")
@Controller
public class SuggestionController extends MainController {
    final Logger logger = LoggerFactory.getLogger(SuggestionController.class);
    private final String addBinding = "org.springframework.validation.BindingResult.suggestion";
    @Autowired
    private SuggestionService suggestionService;
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private CSRFProtector csrfProtector;

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newSuggestion(@ModelAttribute("suggestion") Suggestion suggestion, @LoggedUser AuthenticationFrontendUserDetails user, Model layout, HttpServletRequest request, @LocaleLang Lang lang) {
        Map<String, Object> layoutMap = layout.asMap();
        if (layoutMap.containsKey("errors")) {
            layout.addAttribute(addBinding, layoutMap.get("errors"));
        }
        try {
            csrfProtector.setIntention("suggestion-new");
            suggestion.setToken(csrfProtector.constructToken(request.getSession()));
            logger.info("Generated token " + suggestion.getToken());
            suggestion.setAction(csrfProtector.getIntention());
        } catch (Exception e) {
            logger.error("An exception occured on creating CSRF token", e);
            // TODO : faire la redirection
        }
        logger.info("========> layoutMap " + layoutMap);
        layout.addAttribute("communs", getViewCommunMap(lang));
        return "newSuggestion";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String newSuggestionHandle(@ModelAttribute("suggestion") @Validated({GeneralGroupCheck.class}) Suggestion suggestion, 
    BindingResult binRes, @LoggedUser AuthenticationFrontendUserDetails user, 
    RedirectAttributes redAtt) {
        logger.info("Received POST request " + suggestion);
        if (binRes.hasErrors()) {
            redAtt.addFlashAttribute("error", true);
            redAtt.addFlashAttribute("suggestion", suggestion);
            redAtt.addFlashAttribute("errors", binRes);
            logger.info("Errors found " + binRes);
        } else {
            try {
                suggestion.setSubscriber(conversionService.convert(user, Subscriber.class));
                Suggestion suggestionResult = suggestionService.addNew(suggestion);
                logger.info("==========> RESULT : " + suggestionResult);
                redAtt.addFlashAttribute("success", true);
            } catch(Exception e) {
                binRes.addError(getExceptionError("suggestion"));
                redAtt.addFlashAttribute("error", true);
                redAtt.addFlashAttribute("suggestion", suggestion);
                redAtt.addFlashAttribute("errors", binRes);
            }
        }
        return "redirect:/suggestions/new";
    }
    
    @RequestMapping(value = "/if-exists", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> ifExists(@RequestParam("title") String title) {
        List<Suggestion> suggestions = suggestionService.findByTitleContaining(title);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Map<String, String> suggMap = new HashMap<String, String>();
        if (suggestions.size() > 0) {
            for (Suggestion suggestion : suggestions) {
                suggMap.put(""+suggestion.getId(), suggestion.getTitle());
            }
        }
        jsonMap.put("size", suggestions.size());
        jsonMap.put("list", suggMap);
        return jsonMap;
    }
}