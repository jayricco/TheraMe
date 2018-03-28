package com.therame.view;

import com.therame.model.DetailedUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

        // Add the user object to all view requests so we can show user name, etc.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof DetailedUserDetails) {
            DetailedUserDetails userDetails = (DetailedUserDetails) auth.getPrincipal();

            if (userDetails != null) {
                modelAndView.addObject("user", userDetails.getUser());
            }
        }
    }
}
