package library.controller.backend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import library.controller.MainController;
import library.form.WriterLangForm;
import library.model.entity.Lang;
import library.model.entity.Writer;
import library.model.entity.WriterLang;
import library.service.LangService;
import library.service.WriterLangService;
import library.service.WriterService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * TODO : 
 * - delete must interact with jQuery yes-no popup
 * - modifier partour HashMap en TreeMap (? - il s'agit d'une map ordonnée)
 */
@Controller
@RequestMapping(value = "/backend/writer-lang")
public class BackendWriterLangController extends MainController {
    final Logger logger = LoggerFactory.getLogger(BackendWriterLangController.class);
    @Autowired
    private WriterService writerService;
    @Autowired
    private LangService langsService;
    @Autowired
    private WriterLangService writerLangService;
    @Autowired
    private ConversionService conversionService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'WRITERLANG_ADD')")
    @RequestMapping(value = "/add/{id}", method = RequestMethod.GET)
    public String add(@ModelAttribute("writerLangForm") WriterLangForm writerLangForm, @PathVariable long id, 
    Model layout) {
        Writer writer = writerService.getById(id);
        layout.addAttribute("writer", writer);
        logger.info("================> Found writer " + writer);
        Map<String, WriterLang> addedEntries = writerLangService.getForWriter(writer);
        logger.info("=================> Found some already added entries " + addedEntries);
        Map<Long, Map<String, Object>> writerLang = new HashMap<Long, Map<String, Object>>();
        Map<Long, Lang> langs = langsService.getAllLangsOrderedById();
        for (Long key : langs.keySet()) {
            Lang lang = langs.get(key);
            logger.info("===========> Found lang : " + lang);
            HashMap<String, Object> typeMap = new HashMap<String, Object>();
            for (WriterLang.Types type : WriterLang.Types.values()) {
                String value = "";
                String entryKey = WriterLang.getMapEntryKey(lang, type.getDbCode());
                if (addedEntries.containsKey(entryKey)) {
                    WriterLang entry = addedEntries.get(entryKey);
                    value = entry.getValue();
                }
                logger.info("========> Found type : " + type.getDefaultLabel());
                logger.info("========> Found type : " + type.getTranslateCode());
                typeMap.put(type.getDbCode(), value);
            }
            writerLang.put(lang.getId(), typeMap);
        }
        writerLangForm.setWriterLang(writerLang);
        return "writerLangAdd";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'CATEGORYLANG_ADD')")
    @RequestMapping(value = "/add/{id}", method = RequestMethod.POST)
    public String addHandle(@ModelAttribute("writerLangForm") WriterLangForm writerLangForm, 
    @PathVariable long id, Model layout, HttpServletRequest req,  RedirectAttributes redAtt) {
        logger.info("POST ====================> found writerLangForm " + writerLangForm);
        try {
            writerLangForm.setWriter(writerService.getById(id));
            List<WriterLang> convertedWriterLang = conversionService.convert(writerLangForm, List.class);
            for (WriterLang writerLang : convertedWriterLang) {
                logger.info("Saving " + writerLang);
                writerLangService.addNew(writerLang);
            }
            redAtt.addFlashAttribute("success", true);
        } catch (Exception e) { 
                // results.addError(getExceptionError("writerLangForm"));
                // redAtt.addFlashAttribute("error", true);
                // redAtt.addFlashAttribute("writerLangForm", writerLangForm);
                // redAtt.addFlashAttribute("errors", results);
        }
        return "redirect:/backend/writer-lang/add/"+id;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'WRITER_DELETE')")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable long id) {
        return "writerDelete";
    }
}