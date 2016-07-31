package com.forsrc.cxf.server.restful.base.vo;


import com.forsrc.pojo.Book;
import com.forsrc.pojo.User;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The type Page.
 *
 * @param <E> the type parameter
 */
@XmlRootElement(name = "Page")
@XmlSeeAlso({User.class, Book.class})
public class Page<E> implements Serializable {
    private int start;
    private int size;
    private int total;
    private Collection<E> list = new ArrayList<E>(0);

    /**
     * Instantiates a new Page.
     */
    public Page() {

    }

    /**
     * Gets start.
     *
     * @return the start
     */
    public int getStart() {
        return start;
    }

    /**
     * Sets start.
     *
     * @param start the start
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * Gets size.
     *
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets size.
     *
     * @param size the size
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Gets total.
     *
     * @return the total
     */
    public int getTotal() {
        return total;
    }

    /**
     * Sets total.
     *
     * @param total the total
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * Gets list.
     *
     * @return the list
     */
    public Collection<E> getList() {
        return list;
    }

    /**
     * Sets list.
     *
     * @param list the list
     */
    public void setList(Collection<E> list) {
        this.list = list;
    }
}
