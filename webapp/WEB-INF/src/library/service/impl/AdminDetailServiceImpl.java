package library.service.impl;

import java.util.ArrayList;
import java.util.Collection;

import library.model.entity.Admin;
import library.security.AuthenticationUserDetails;
import library.service.AdminService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("adminDetailsServiceImpl")
public class AdminDetailServiceImpl implements UserDetailsService {
    final Logger logger = LoggerFactory.getLogger(AdminDetailServiceImpl.class);
    @Autowired
    private AdminService adminService;

    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Looking for user by username " + username);
        Admin admin = adminService.loadByUsername(username);
        if (admin != null) {
            logger.info("Admin(s) was found : " + admin);
            String userRole = "ROLE_MODERATOR";
            if (admin.hasAdminRole()) {
                userRole = "ROLE_ADMIN";
            }
            logger.info("==========> setting new role " + userRole);
            Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new GrantedAuthorityImpl(userRole));
            String[] roles = admin.getRole().split(",");
            for (String role : roles) {
                logger.info("==========> adding role " + role);
                authorities.add(new GrantedAuthorityImpl(role.trim()));
            }
            /*UserDetails userDetails = new User(admin.getLogin(), admin.getPassword(),
                                               true, true, true, true, authorities
                                               );*/
            AuthenticationUserDetails userDetails = new AuthenticationUserDetails(admin.getLogin(), 
                    admin.getPassword(), true, true, true, true, authorities);
            userDetails.setId(admin.getId());
            logger.info("Created new UserDetails instance : " + userDetails);
            return userDetails;
        }
        logger.info("Admin wasn't found : null returned" + admin);
        return null;
    }
}