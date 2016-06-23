package com.forsrc.hibernate.search;


import org.springframework.context.ApplicationEvent;

public class HibernateSearchApplicationEvent<E> extends ApplicationEvent {

    private static final long serialVersionUID = 6969274858909795928L;


    private Op op;

    public HibernateSearchApplicationEvent(Object data) {
        super(data);
    }

    public HibernateSearchApplicationEvent(Op op, Object data) {
        super(data);
        this.op = op;
    }


    public Op getOp() {
        return this.op;
    }

    public void setOp(Op op) {
        this.op = op;
    }

    /**
     * @see com.forsrc.hibernate.search.HibernateSearchApplicationListener.HibernateSearchHandle
     */
    public static enum Op {
        OP_INDEX_CREATE, OP_INDEX_UPDATE, OP_INDEX_DELETE, QUERY, CLOSE;
    }

}
