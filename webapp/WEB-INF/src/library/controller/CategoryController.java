package library.controller;

import java.util.List;

import library.annotation.LocaleLang;
import library.model.entity.BookCategory;
import library.model.entity.Lang;
import library.service.BookCategoryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/categories")
@Controller
public class CategoryController extends MainController {
    final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    @Autowired
    private BookCategoryService bookCategoryService;
    
    /**
     * Gets all books by category.
     * First, find the category with Lang object. After that look for adequated books.
     * If the category doesn't exist, throw an Exception.
     */
    @RequestMapping(value = "{category}", method = RequestMethod.GET)
    public String listBooksByCategory(@PathVariable String category, Model layout, @LocaleLang Lang lang) {
        // // TEMPORARY
        // Lang lang = new Lang();
        // lang.setId(1l);
        List<BookCategory> bookCategory = bookCategoryService.findByCategoryTitle(category, lang);
        logger.info("=======> Found category " + bookCategory);
        layout.addAttribute("books", bookCategory);
        layout.addAttribute("communs", getViewCommunMap(lang));
        return "listBooksByCategory";
    }
}