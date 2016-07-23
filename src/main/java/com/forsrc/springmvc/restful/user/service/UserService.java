package com.forsrc.springmvc.restful.user.service;


import com.forsrc.base.service.BaseService;
import com.forsrc.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface UserService extends BaseService<User, Long> {


}
