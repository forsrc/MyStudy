package com.forsrc.cxf.server.restful.book.service;


import com.forsrc.cxf.server.restful.base.service.BaseCxfService;
import com.forsrc.pojo.Book;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface Book service.
 */
@Service
@Transactional
public interface BookService extends BaseCxfService<Book, Long> {
}
