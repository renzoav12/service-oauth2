package com.springboot.service.oauth.security;

import com.commons.user.model.User;
import com.springboot.service.oauth.service.IUserService;
import com.springboot.service.oauth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InfoAditionalToken implements TokenEnhancer {

  @Autowired
  private IUserService userService;

  @Override
  public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication oAuth2Authentication) {
    Map<String, Object> info = new HashMap<>();
    User user = userService.findByUserName(oAuth2Authentication.getName());
    info.put("name", user.getUserName());
    info.put("lastName", user.getLastName());
    info.put("email", user.getEmail());
    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
    return accessToken;
  }
}
