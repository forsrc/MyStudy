package com.forsrc.cxf.server.restful.book.service.impl;


import com.forsrc.cxf.server.restful.base.service.impl.BaseCxfServiceImpl;
import com.forsrc.cxf.server.restful.book.service.BookService;
import com.forsrc.pojo.Book;
import com.forsrc.springmvc.restful.user.service.impl.UserServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "bookCxfService")
@Transactional
public class BookCxfServiceImpl extends BaseCxfServiceImpl<Book, Long> implements BookService {
}
