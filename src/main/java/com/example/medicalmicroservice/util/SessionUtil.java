package com.example.medicalmicroservice.util;

import com.example.medicalmicroservice.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionUtil {

    public static HttpSession getSession() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest().getSession();
        }
        return null;
    }

    public static void setUserInSession(User user) {
        HttpSession session = getSession();
        if (session != null) {
            session.setAttribute("user", user);
        }
    }

    public static User getUserFromSession() {
        HttpSession session = getSession();
        if (session != null) {
            return (User) session.getAttribute("user");
        }
        return null;
    }

    public static void removeUserFromSession() {
        HttpSession session = getSession();
        if (session != null) {
            session.removeAttribute("user");
        }
    }

    public static boolean isUserLoggedIn() {
        return getUserFromSession() != null;
    }

    public static boolean isUserInRole(String role) {
        User user = getUserFromSession();
        if (user != null) {
            return user.getRole().equals(role);
        }
        return false;
    }
}