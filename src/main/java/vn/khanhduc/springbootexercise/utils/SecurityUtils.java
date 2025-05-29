package vn.khanhduc.springbootexercise.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    private SecurityUtils() {}

    public static Long getUserLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth !=null && auth.getName() != null) {
            return Long.parseLong(auth.getName());
        }

        return null;
    }

}
