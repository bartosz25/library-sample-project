package com.example.library.controller.backend;

import com.example.library.controller.MainController;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/backend")
public class BackendIndexController extends MainController {

    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "backendLogin";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        return "backendLogout";
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'SUBSCRIBER_ADD')")
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboard() {
        return "backendDashboard";
    }

    @RequestMapping(value = "/access-denied", method = {RequestMethod.POST, RequestMethod.GET})
    public String accessDenied() {
        return "backedAccessDenied";
    }
}