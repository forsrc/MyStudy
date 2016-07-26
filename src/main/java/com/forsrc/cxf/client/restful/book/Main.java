package com.forsrc.cxf.client.restful.book;


import com.forsrc.cxf.server.restful.base.vo.Page;
import com.forsrc.pojo.Book;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;

public class Main {

    private static final String URL = "http://localhost:8077/cxf/restful/v1.0/api";
    private static final String ID = "1";


    public static void main(String[] args) throws IOException {


        Book book = new Book();
        book = get(1L);
        book.setName("Java " + System.currentTimeMillis());
        book.setIsbnNumber("" + System.currentTimeMillis());
        book.setAuthor(new Date().toString());
        book.setCategoryId(1L);
        book.setCreateOn(new Date());
        book.setUpdateOn(new Date());
        book = save(book);
        System.out.println(book.getId());
        book.setAuthor("" + System.currentTimeMillis());
        book = update(book);
        System.out.println(book.getAuthor());
        book.setAuthor("" + System.currentTimeMillis());
        book = patch(book);
        System.out.println(book.getAuthor());
        delete(book);
        Page<Book> page = list();
        System.out.println(page.getList().size());
    }

    public static Page<Book> list() {
        WebClient client = WebClient.create(URL);
        Page page = client.path("/book")
                .query("start", 0).query("size", 10)
                .accept(MediaType.APPLICATION_XML_TYPE)
                .type(MediaType.APPLICATION_XML_TYPE)
                .get(Page.class);
        client.close();
        return page;
    }

    public static Book save(Book book) throws IOException {
        WebClient client = WebClient.create(URL);
        client.path("/book")
                .accept(MediaType.APPLICATION_XML_TYPE)
                .type(MediaType.APPLICATION_XML_TYPE);

        Book b = client.post(book, Book.class);
        client.close();
        return b;
    }

    public static Book get(Long id) {
        WebClient client = WebClient.create(URL);
        client.path("/book/" + id + "").accept(MediaType.APPLICATION_XML_TYPE)
                .type(MediaType.APPLICATION_XML_TYPE);
        Book book = client.get(Book.class);
        client.close();
        return book;
    }

    public static Book update(Book book) {
        WebClient client = WebClient.create(URL);
        client.path("/book/" + book.getId())
                .accept(MediaType.APPLICATION_XML_TYPE)
                .type(MediaType.APPLICATION_XML_TYPE);
        Book b = client.put(book, Book.class);
        client.close();
        return b;
    }

    public static Book patch(Book book) {
        WebClient client = WebClient.create(URL);
        client.path("/book/" + book.getId())
                .accept(MediaType.APPLICATION_XML_TYPE)
                .type(MediaType.APPLICATION_XML_TYPE);
        Book b = client.put(book, Book.class);
        client.close();
        return b;
    }

    public static void delete(Book book) {
        WebClient client = WebClient.create(URL);

        client.path("/book/" + book.getId())
                .query("id", book.getId())
                .query("version", book.getVersion())
                .accept(MediaType.APPLICATION_XML_TYPE)
                .type(MediaType.APPLICATION_XML_TYPE);
        Response response = client.delete();
        System.out.println(response.getStatus() + " --> " + response.getEntity());
        client.close();

    }
}
