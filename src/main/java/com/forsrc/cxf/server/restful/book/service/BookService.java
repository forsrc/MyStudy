package com.forsrc.cxf.server.restful.book.service;


import com.forsrc.cxf.server.restful.base.action.BaseCxfAction;
import com.forsrc.cxf.server.restful.base.service.BaseCxfService;
import com.forsrc.pojo.Book;
import com.forsrc.springmvc.restful.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface BookService extends BaseCxfService<Book, Long> {
}
