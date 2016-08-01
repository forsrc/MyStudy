package com.forsrc.cxf.client.restful.book;


import com.forsrc.cxf.server.restful.book.webservice.BookCxfWebService;
import com.forsrc.pojo.Book;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import javax.xml.namespace.QName;
import java.io.IOException;


public class WsMain {

    private static final String URL = "http://localhost:8077/cxf/ws/v1.0/api";
    private static final String ID = "1";


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws Exception {

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(BookCxfWebService.class);
        factory.setAddress(URL + "/book/1");

        BookCxfWebService service = (BookCxfWebService) factory.create();

        Book b = service.get(1L);
        System.out.println(b.getName());



        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        org.apache.cxf.endpoint.Client client = dcf.createClient(URL + "/book/1?wsdl");



        QName name = new QName("http://webservice.book.restful.server.cxf.forsrc.com/", "get");



        Object[] objects = client.invoke(name, 1L);
        //Book book = (Book) objects[0];
        System.out.println(objects.length);
    }


}
