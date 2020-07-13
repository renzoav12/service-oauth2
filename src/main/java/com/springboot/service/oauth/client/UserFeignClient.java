package com.springboot.service.oauth.client;

import com.commons.user.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "service-users")
public interface UserFeignClient {

  @GetMapping("/users/search/getUser")
  User findByUserName(@RequestParam String userName);
}
