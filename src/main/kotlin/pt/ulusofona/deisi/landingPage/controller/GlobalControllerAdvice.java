package pt.ulusofona.deisi.landingPage.controller;
// Created by palves

//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("username")
    public String getUsername(Principal principal) {
        if (principal != null) {
            // uncomment this for orcid authentication
//            if (principal instanceof OAuth2AuthenticationToken) {
//                OAuth2AuthenticationToken oauth2Principal = (OAuth2AuthenticationToken) principal;
//                OAuth2User oAuth2User = oauth2Principal.getPrincipal();
//                return oAuth2User.getAttribute("given_name") +
//                        " " + oAuth2User.getAttribute("family_name");
//            }
            return principal.getName();
        }
        return "";
    }
}
