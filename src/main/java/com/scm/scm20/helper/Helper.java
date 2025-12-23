package com.scm.scm20.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authentication) {

        if (authentication instanceof OAuth2AuthenticatedPrincipal) {
            var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            var clientId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

            var oauth2User = (OAuth2User) authentication.getPrincipal();
            String username = "";

            if (clientId.equalsIgnoreCase("google")) {
                System.out.println("Getting email from google");
                username = oauth2User.getAttribute("email").toString();

            } else if (clientId.equalsIgnoreCase("facebook")) {
                System.out.println("facebook");
                username = oauth2User.getAttributes().get("email") != null
                        ? oauth2User.getAttributes().get("email").toString()
                        : oauth2User.getAttributes().get("login").toString().toLowerCase() + "@gmail.com";
            } else if (clientId.equalsIgnoreCase("github")) {
                System.out.println("github");
            }
            return username;
        } else {
            System.out.println("Getting data from local db.");
            return authentication.getName();
        }
    }
}
