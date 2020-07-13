package com.springboot.service.oauth.service;

import brave.Tracer;
import com.commons.user.model.User;
import com.springboot.service.oauth.client.UserFeignClient;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService, UserDetailsService {

  private Logger log = LoggerFactory.getLogger(UserService.class);

  @Autowired
  private UserFeignClient clientFeign;

  @Autowired
  private Tracer trace;


  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    try {
      User u = clientFeign.findByUserName(s);
      List<GrantedAuthority> authorities = u.getRoles().stream()
          .map(rol -> new SimpleGrantedAuthority(rol.getName()))
          .peek(authority -> log.info("Role" + authority.getAuthority()))
          .collect(Collectors.toList());

      log.info("User authenticate:" + u);
      return new org.springframework.security.core.userdetails.User(s, u.getPassword(),
          u.getEnabled(), true, true, true, authorities);

    }catch(FeignException e){
      String error = "User don't exists at the System";
      trace.currentSpan().tag("error.message", error);
      log.error(error);
      throw  new UsernameNotFoundException("User don't exists at the System");
    }
  }

  @Override
  public User findByUserName(String userName) {
    try {
      return clientFeign.findByUserName(userName);
    }catch (FeignException e) {
      String error = "User don't exists at the System";
      trace.currentSpan().tag("error.message", error + ":" + e.getMessage());
      throw  new UsernameNotFoundException("User don't exists at the System");
    }

  }
}


