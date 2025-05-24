package com.ArnabMdev.ArkaneGames.service;

import com.ArnabMdev.ArkaneGames.entity.User;
import com.ArnabMdev.ArkaneGames.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        // Extract user details from OAuth provider
        Map<String, Object> attributes = oauth2User.getAttributes();
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oauth2User.getAttribute("sub");
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String pictureUrl = oauth2User.getAttribute("picture");

        // Save or update user in database
        saveOrUpdateUser(provider, providerId, email, name, pictureUrl);

        return oauth2User;
    }

    private void saveOrUpdateUser(String provider, String providerId, String email, String name, String pictureUrl) {
        Optional<User> existingUser = userRepository.findByProviderAndProviderId(provider, providerId);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            // Update existing user's information if needed
            user.setEmail(email);
            user.setName(name);
            user.setPictureUrl(pictureUrl);
            userRepository.save(user);
        } else {
            User newUser = new User(email, name, pictureUrl, provider, providerId);
            userRepository.save(newUser);
        }
    }
}
