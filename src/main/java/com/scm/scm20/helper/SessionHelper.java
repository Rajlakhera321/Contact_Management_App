package com.scm.scm20.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionHelper {
    public static void removeMessage() {

        try {
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest().getSession(false);

            session.removeAttribute("message");
        } catch (Exception e) {
            System.out.println("Error removing message from session");
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
