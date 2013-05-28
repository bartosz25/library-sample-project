package com.example.library.controller;

import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.library.annotation.LocaleLang;
import com.example.library.model.entity.Book;
import com.example.library.model.entity.Lang;
import com.example.library.model.repository.BookRepository;
import com.example.library.service.BookLangService;
import com.example.library.service.BookService;
import com.example.library.tools.UtilitaryTool;
import net.sf.ehcache.Ehcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 * TODO : 
 * - /books/{category}/{title}/{id} : éviter le duplicate content
 * - déplacer tout ce qui concerne la pagination dans les beans
 * - tri
 * - transférer le paramètre PER_PAGE dans le IoC
 * - rajouter quelque chose du type "main_category" pour distinguer la catégorie à afficher dans le lien
 * - remplacer partout la transformation manuelle d'User en Subscriber par conversionService.convert(user, Subscriber.class) => voir l'exemple dans SubscriberController.java
 */

@RequestMapping("/books")
@Controller
public class BookController extends MainController {
    final Logger logger = LoggerFactory.getLogger(BookController.class);
    @Autowired
    private BookService bookService;
    @Autowired
    private BookLangService bookLangService;
    @Autowired
    private CacheManager cacheManager;
// test only
@Autowired
private BookRepository bookRepository;
    
    // @RequestMapping(value = "{page}", method = RequestMethod.GET, params = {"page"})
    @RequestMapping(value = "{page}", method = RequestMethod.GET)
    public String allBooks(@PathVariable String page, Model layout, @LocaleLang Lang lang) {
    // public String allBooks(@RequestParam int page, Model layout) {
        // for test purposes only, check if cache was correctly written
        logger.info("====> Caches " + cacheManager.getCacheNames());
        logger.info("====> Cache " + cacheManager.getCache("allBooks"));
        if (cacheManager.getCache("allBooks") != null) {
            Ehcache map = (Ehcache)cacheManager.getCache("allBooks").getNativeCache();
            logger.info("=> Map " + map.get(1));
            logger.info("=> Map size " + map.getSize());
            logger.info("=> Map name " + map.getName());
            logger.info("=> Map keys " + map.getKeys());
        } 
 for (String cacheName :cacheManager.getCacheNames())
 {
 	logger.info("==> " +((Ehcache)cacheManager.getCache(cacheName).getNativeCache()).toString());
 	logger.info("==> " +((Ehcache)cacheManager.getCache(cacheName).getNativeCache()).getKeys());
 	logger.info("==> " +((Ehcache)cacheManager.getCache(cacheName).getNativeCache()).getSize());
 	logger.info("==> " +((Ehcache)cacheManager.getCache(cacheName).getNativeCache()).getName());
 }
 
bookService.findAll(1, 1);
// // TODO : Lang
        // Lang lang = new Lang();
        // lang.setId(1l);
        int pageInt = Integer.valueOf(page);
        Page<Book> books = bookService.getAllBooks((--pageInt), 2, lang);
// bookLangService.findAllByTitleForLang((--pageInt), 2, lang);
        int current = books.getNumber() + 1;
        int begin = Math.max(1, (current-5));
        int end = Math.min((current + 5), books.getTotalPages());
        logger.info("=======> got books " + books.getContent());
        logger.info("=======> got books total " + books.getTotalPages());
        logger.info("=======> got current page " + books.getNumber());
        logger.info("=======> got current page (to view) " + current);
        logger.info("=======> got begin page (to view)  " + begin);
        logger.info("=======> got end page (to view)  " + end);
        layout.addAttribute("books", books.getContent());
        layout.addAttribute("begin", begin);
        layout.addAttribute("end", end);
        layout.addAttribute("current", current);
        layout.addAttribute("communs", getViewCommunMap(lang));
        return "allBooks";
    }
    
    @RequestMapping(value = "{category}/{title}/{id}", method = RequestMethod.GET)
    public String showBook(@PathVariable String category, @PathVariable String title, @PathVariable long 
	id, Model layout, HttpServletRequest request, HttpServletResponse response, @LocaleLang Lang lang) {
        // // TEMPORARY TODO
        // Lang lang = new Lang();
        // lang.setId(1l);
        List<Book> book = bookService.getByIdAndLang(id, lang);
        logger.info("====> found book instance " + book);
        layout.addAttribute("book", book);
        if (request.getParameter("pdf") != null && request.getParameter("pdf").equals("true")) {
            return "showBookSummary";
        }
        layout.addAttribute("communs", getViewCommunMap(lang));
        return "showBook";
    }
    
    @RequestMapping(value = "/summary/{bookId}", method = RequestMethod.GET)
    public void getBookPdfSummary(@PathVariable long bookId, HttpServletRequest request, 
    HttpServletResponse response) {
        ServletOutputStream outputStream = null;
        try  {
            String url = UtilitaryTool.getBaseUrl(request)+"/books/x/y/"+bookId+"?pdf=true";
            logger.info("Generating url " + url);
            // Debug only
            // InputStream input = new URL(url).openStream();
            // StringWriter writer = new StringWriter();
            // CopyUtils.copy(input, writer);
            // logger.info("Found : " + writer.toString());

            response.setContentType("application/pdf; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=report.pdf");
            outputStream = response.getOutputStream();

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocument(url);
            renderer.layout();
            renderer.createPDF(outputStream);
        } catch (Exception e)  {
            logger.error("An error occured on sending PDF to browser", e);
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (Exception ex) {
                logger.error("An error occured on flushing or closing outputStream", ex);
            }
        }
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String testBook(Model layout) {
        layout.addAttribute("book",  bookRepository.findOne(22l));
        return "testBook";
    }
}

/**
Une erreur d'encodage a été constament envoyée à cause de non-spécificiation de l'encoding dans la méthode showBook. Pour le débloquer, il faut soit rajouter 
<%@ page language="java" pageEncoding="UTF-8"%> dans JSP, soit response.setContentType("application/pdf; charset=UTF-8") dans Java
Exception trouvée
ERROR: com.example.library.controller.BookController - An error occured on sending PDF to browser
org.xhtmlrenderer.util.XRRuntimeException: Can't load the XML resource (using TRaX transformer). com.sun.org.apache.xerces.internal.impl.io.MalformedByteSequenceException: Octet 2 de la s<E9>quence
 UTF-8 <E0> 3 octets non valide.
        at org.xhtmlrenderer.resource.XMLResource$XMLResourceBuilder.createXMLResource(XMLResource.java:191)
        at org.xhtmlrenderer.resource.XMLResource.load(XMLResource.java:71)
        at org.xhtmlrenderer.swing.NaiveUserAgent.getXMLResource(NaiveUserAgent.java:211)
        at org.xhtmlrenderer.pdf.ITextRenderer.loadDocument(ITextRenderer.java:134)
        at org.xhtmlrenderer.pdf.ITextRenderer.setDocument(ITextRenderer.java:138)
        at com.example.library.controller.BookController.getBookPdfSummary(BookController.java:119)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:601)
        at org.springframework.web.method.support.InvocableHandlerMethod.invoke(InvocableHandlerMethod.java:219)
        at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:132)
        at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:100)
        at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:604)
        at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:565)
        at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:80)
        at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:923)
        at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:852)
        at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:882)
        at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:778)
        at javax.servlet.http.HttpServlet.service(HttpServlet.java:119)
        at javax.servlet.http.HttpServlet.service(HttpServlet.java:96)
        at com.caucho.server.dispatch.ServletFilterChain.doFilter(ServletFilterChain.java:109)
        at org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter.doFilterInternal(OpenEntityManagerInViewFilter.java:147)
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:76)
        at com.caucho.server.dispatch.FilterFilterChain.doFilter(FilterFilterChain.java:89)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:330)
        at org.springframework.security.web.access.intercept.FilterSecurityInterceptor.invoke(FilterSecurityInterceptor.java:118)
        at org.springframework.security.web.access.intercept.FilterSecurityInterceptor.doFilter(FilterSecurityInterceptor.java:84)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:342)
        at org.springframework.security.web.access.ExceptionTranslationFilter.doFilter(ExceptionTranslationFilter.java:113)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:342)
        at org.springframework.security.web.session.SessionManagementFilter.doFilter(SessionManagementFilter.java:103)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:342)        at org.springframework.security.web.authentication.AnonymousAuthenticationFilter.doFilter(AnonymousAuthenticationFilter.java:113)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:342)
        at org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter.doFilter(RememberMeAuthenticationFilter.java:139)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:342)
        at org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter.doFilter(SecurityContextHolderAwareRequestFilter.java:54)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:342)
        at org.springframework.security.web.savedrequest.RequestCacheAwareFilter.doFilter(RequestCacheAwareFilter.java:45)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:342)
        at org.springframework.security.web.authentication.www.BasicAuthenticationFilter.doFilter(BasicAuthenticationFilter.java:150)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:342)
        at org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter.doFilter(AbstractAuthenticationProcessingFilter.java:183)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:342)
        at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:105)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:342)
        at org.springframework.security.web.session.ConcurrentSessionFilter.doFilter(ConcurrentSessionFilter.java:125)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:342)
        at org.springframework.security.web.context.SecurityContextPersistenceFilter.doFilter(SecurityContextPersistenceFilter.java:87)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:342)
        at org.springframework.security.web.FilterChainProxy.doFilterInternal(FilterChainProxy.java:192)
        at org.springframework.security.web.FilterChainProxy.doFilter(FilterChainProxy.java:160)
        at org.springframework.web.filter.DelegatingFilterProxy.invokeDelegate(DelegatingFilterProxy.java:346)
        at org.springframework.web.filter.DelegatingFilterProxy.doFilter(DelegatingFilterProxy.java:259)
        at com.caucho.server.dispatch.FilterFilterChain.doFilter(FilterFilterChain.java:89)
        at com.caucho.server.webapp.WebAppFilterChain.doFilter(WebAppFilterChain.java:156)
        at com.caucho.server.webapp.AccessLogFilterChain.doFilter(AccessLogFilterChain.java:95)
        at com.caucho.server.dispatch.ServletInvocation.service(ServletInvocation.java:289)
        at com.caucho.server.http.HttpRequest.handleRequest(HttpRequest.java:811)
        at com.caucho.network.listen.TcpSocketLink.dispatchRequest(TcpSocketLink.java:1221)
        at com.caucho.network.listen.TcpSocketLink.handleRequest(TcpSocketLink.java:1177)
        at com.caucho.network.listen.TcpSocketLink.handleRequestsImpl(TcpSocketLink.java:1161)
        at com.caucho.network.listen.TcpSocketLink.handleRequests(TcpSocketLink.java:1084)
        at com.caucho.network.listen.TcpSocketLink.handleAcceptTask(TcpSocketLink.java:907)
        at com.caucho.network.listen.AcceptTask.doTask(AcceptTask.java:74)
        at com.caucho.network.listen.ConnectionTask.runThread(ConnectionTask.java:97)
        at com.caucho.network.listen.ConnectionTask.run(ConnectionTask.java:80)
        at com.caucho.network.listen.AcceptTask.run(AcceptTask.java:59)
        at com.caucho.env.thread.ResinThread.runTasks(ResinThread.java:164)
        at com.caucho.env.thread.ResinThread.run(ResinThread.java:130)
Caused by: javax.xml.transform.TransformerException: com.sun.org.apache.xerces.internal.impl.io.MalformedByteSequenceException: Octet 2 de la s<E9>quence UTF-8 <E0> 3 octets non valide.
        at com.sun.org.apache.xalan.internal.xsltc.trax.TransformerImpl.transform(TransformerImpl.java:723)
        at com.sun.org.apache.xalan.internal.xsltc.trax.TransformerImpl.transform(TransformerImpl.java:317)
        at org.xhtmlrenderer.resource.XMLResource$XMLResourceBuilder.createXMLResource(XMLResource.java:189)
        ... 71 more
Caused by: com.sun.org.apache.xerces.internal.impl.io.MalformedByteSequenceException: Octet 2 de la s<E9>quence UTF-8 <E0> 3 octets non valide.
        at com.sun.org.apache.xerces.internal.impl.io.UTF8Reader.invalidByte(UTF8Reader.java:687)
        at com.sun.org.apache.xerces.internal.impl.io.UTF8Reader.read(UTF8Reader.java:408)
        at com.sun.org.apache.xerces.internal.impl.XMLEntityScanner.load(XMLEntityScanner.java:1750)
        at com.sun.org.apache.xerces.internal.impl.XMLEntityScanner.peekChar(XMLEntityScanner.java:494)
        at com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl$FragmentContentDriver.next(XMLDocumentFragmentScannerImpl.java:2647)
        at com.sun.org.apache.xerces.internal.impl.XMLDocumentScannerImpl.next(XMLDocumentScannerImpl.java:607)
        at com.sun.org.apache.xerces.internal.impl.XMLNSDocumentScannerImpl.next(XMLNSDocumentScannerImpl.java:116)
        at com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl.scanDocument(XMLDocumentFragmentScannerImpl.java:488)
        at com.sun.org.apache.xerces.internal.parsers.XML11Configuration.parse(XML11Configuration.java:835)
        at com.sun.org.apache.xerces.internal.parsers.XML11Configuration.parse(XML11Configuration.java:764)
        at com.sun.org.apache.xerces.internal.parsers.XMLParser.parse(XMLParser.java:123)
        at com.sun.org.apache.xerces.internal.parsers.AbstractSAXParser.parse(AbstractSAXParser.java:1210)
        at com.sun.org.apache.xalan.internal.xsltc.trax.TransformerImpl.transformIdentity(TransformerImpl.java:640)
        at com.sun.org.apache.xalan.internal.xsltc.trax.TransformerImpl.transform(TransformerImpl.java:711)
        ... 73 more

*/