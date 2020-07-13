package com.springboot.service.oauth.service;

import com.commons.user.model.User;

public interface IUserService {

   User findByUserName(String userName);
}
