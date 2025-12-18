package com.scm.scm20.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.scm20.entities.Providers;
import com.scm.scm20.entities.User;
import com.scm.scm20.helper.AppConstants;
import com.scm.scm20.repositories.UserRepositories;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OauthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(OauthAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepositories userRepositories;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        logger.info("OAuth2 Authentication Successful for user: " + authentication.getName());

        var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

        logger.info("Authorized Client Registration ID: " + authorizedClientRegistrationId);
        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

        user.getAttributes().forEach((k, v) -> logger.info(k + ": " + v));

        User user1 = new User();
        user1.setUserId(UUID.randomUUID().toString());
        user1.setEnabled(true);
        user1.setEmailVerified(true);
        user1.setRoleList(List.of(AppConstants.USER_ROLE));
        user1.setPassword("password");

        if (authorizedClientRegistrationId.equals("google")) {
            logger.info("Google OAuth2 login successful for user: " + authentication.getName());

            user1.setEmail(user.getAttributes().get("email").toString());
            user1.setProfilePic(user.getAttributes().get("picture").toString());
            user1.setName(user.getAttributes().get("name").toString());
            user1.setProviderId(user.getName());
            user1.setProvider(Providers.GOOGLE);
            user1.setAbout("This account is created using google.");

        } else if (authorizedClientRegistrationId.equals("github")) {
            logger.info("GitHub OAuth2 login successful for user: " + authentication.getName());

            String email = user.getAttributes().get("email") != null
                    ? user.getAttributes().get("email").toString()
                    : user.getAttributes().get("login").toString().toLowerCase() + "@gmail.com";

            user1.setEmail(email);
            user1.setProfilePic(user.getAttributes().get("avatar_url").toString());
            user1.setName(user.getAttributes().get("login").toString());
            user1.setProviderId(user.getName());
            user1.setProvider(Providers.GITHUB);
            user1.setAbout("This account is created using github.");

        } else if (authorizedClientRegistrationId.equals("linkedIn")) {
            logger.info("LinkedIn OAuth2 login successful for user: " + authentication.getName());
        } else {
            logger.warn("Unknown OAuth2 provider: " + authorizedClientRegistrationId);
        }

        // // logger.info(user.getName());

        // // user.getAttributes().forEach((k, v) -> logger.info(k + ": " + v));

        // // logger.info(user.getAuthorities().toString());

        // String email = (String) user.getAttributes().get("email");
        // String name = (String) user.getAttributes().get("name");
        // String picture = (String) user.getAttributes().get("picture");

        User user2 = userRepositories.findByEmail(user1.getEmail()).orElse(null);

        if (user2 == null) {
            userRepositories.save(user1);
            logger.info("New user created: " + user1.getEmail());
        } else {
            logger.info("Existing user logged in: " + user1.getEmail());
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }

}
