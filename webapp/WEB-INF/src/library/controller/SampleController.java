package library.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import library.model.entity.Book;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/sample")
@Controller
public class SampleController
{
  // final Logger logger = LoggerFactory.getLogger(SampleController.class);


  // @Autowired
  // private BookService bookService;
  
  // @Autowired
  // private BookValidator bookValidator;
  
  // @Autowired
  // private MessageSource messageSource;
  
  // @Autowired
  // private ConversionService conversionService;
  
  @RequestMapping(value = "/index", method = RequestMethod.GET)
  public String index(Book book, BindingResult bindingResult, Model layout)
  {
    // Map<String, String> animals = new HashMap<String, String>() {{
      // put("cat", "Cat");
      // put("dog", "Dog");
      // put("cow", "Cow");
    // }};
    // layout.addAttribute("animals", animals);
    
    // Set<ConstraintViolation<Book>> violations = new HashSet<ConstraintViolation<Book>>();
    // // book = new Book();
    // // book.setTitle("a");
    // // violations = bookValidator.validateBookAdd(book);
    // // for(ConstraintViolation<Book> viol : violations)
    // // {
        // // System.out.println("Violation found " + viol);
    // // }
    
// // GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
// // ctx.load("/META-INF/spring/root-context.xml");
// // ctx.refresh();

// // BookDaoImpl bookDao = ctx.getBean("bookDaoImpl", BookDaoImpl.class);

// // BookDaoImpl bookDao = new BookDaoImpl();
// // bookDao.setSessionFactory(sessionFactory);
// System.out.println("bookService = " + bookService);
// List<Book> books = bookService.findAllBooks();
// layout.addAttribute("books", books);
// for(Book b : books)
// {
  // System.out.println(conversionService.convert(b, String.class));
  
// }

    // BookService bs = new BookService();
    // layout.addAttribute("books", bs.findAll());
    // logger.info("Listing contacts");
    System.out.println("Test!!!!");
    return "sample/index";
  }

/** 
 * Si pas @ModelAttribute, une exception est captée (http://www.mkyong.com/spring-mvc/spring-mvc-neither-bindingresult-nor-plain-target-object-for-bean-name-xxx-available-as-request-attribute/).
 * Si pas @Valid, l'entité Book n'est pas validé et du coup on passe tout de soute dans la partie 
 * bookService.save(book), ce qui provoque aussi une exception.
 */
  @RequestMapping(value = "/index",method = RequestMethod.POST)
  public String add(@ModelAttribute("book") @Valid Book book, BindingResult binRes, Model layout, HttpServletRequest req, RedirectAttributes redAtt)
  {
// System.out.println("Book is " + book);
      // if(binRes.hasErrors())    
      // {
// System.out.println("Binding result has errors");
          // layout.addAttribute("message", "is error");    
          // // layout.addAttribute("book", book);
          // return "sample/index";
      // }    
// // // Si layout.asMap().clear() a été appelé après layout.addAttribute("book", book), Caused by: java.lang.IllegalStateException: Neither BindingResult nor plain
// // target object for bean name 'book' available as request attribute a été captée
      // layout.asMap().clear();    
          // layout.addAttribute("book", book);
// System.out.println("Binding result hasn't errors");
      // redAtt.addFlashAttribute("messages", "is ok");    
      // bookService.save(book);    
      // return "redirect:/sample/index";    
          return "sample/index";
          
  }
	// @RequestMapping(params = "form", method = RequestMethod.GET)
    // public String createForm(Model uiModel, HttpServletRequest httpServletRequest) {
		// // Entry entry = entryService.findById(blogid);
        // // uiModel.addAttribute("entry", entry);
        
        // // Comment comment = new Comment();
		// // comment.setPostDate(new DateTime());
		// // comment.setPostBy(auditorAwareBean.getCurrentAuditor());
        // // uiModel.addAttribute("comment", comment);
        // // populateSelectBox(uiModel, blogid);
        // return "sample/index";
    // }	
}