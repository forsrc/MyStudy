package com.forsrc.cxf.server.restful.base.vo;


import com.forsrc.pojo.Book;
import com.forsrc.pojo.User;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@XmlRootElement(name = "Page")
@XmlSeeAlso({User.class, Book.class})
public class Page<E> implements Serializable {
    private int start;
    private int size;
    private int total;
    private Collection<E> list = new ArrayList<E>(0);

    public Page() {

    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Collection<E> getList() {
        return list;
    }

    public void setList(Collection<E> list) {
        this.list = list;
    }
}
