package library.security;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class AuthenticationFrontendUserDetails extends User  {
    final Logger logger = LoggerFactory.getLogger(AuthenticationFrontendUserDetails.class);
    private long id = 0l;
    private int bookingNb = 0;

    public AuthenticationFrontendUserDetails(String username, String password, boolean enabled, 
        boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, 
        Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
    
    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setBookingNb(int bookingNb) {
        this.bookingNb = bookingNb;
    }

    public int getBookingNb() {
        return bookingNb;
    }
}