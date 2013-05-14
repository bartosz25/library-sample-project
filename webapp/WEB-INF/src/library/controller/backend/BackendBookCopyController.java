package library.controller.backend;

import library.controller.MainController;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * TODO : 
 * - delete must interact with jQuery yes-no popup
 */
@Controller
@RequestMapping(value = "/backend/book-copy")
public class BackendBookCopyController extends MainController {

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'BOOKCOPY_ADD')")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
        return "bookCopyAdd";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'BOOKCOPY_EDIT')")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable long id) {
        return "bookCopyEdit";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'BOOKCOPY_DELETE')")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable long id) {
        return "bookCopyDelete";
    }
}