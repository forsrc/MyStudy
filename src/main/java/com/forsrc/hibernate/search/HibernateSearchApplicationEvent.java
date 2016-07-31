package com.forsrc.hibernate.search;


import org.springframework.context.ApplicationEvent;

/**
 * The type Hibernate search application event.
 *
 * @param <E> the type parameter
 */
public class HibernateSearchApplicationEvent<E> extends ApplicationEvent {

    private static final long serialVersionUID = 6969274858909795928L;


    private Op op;

    /**
     * Instantiates a new Hibernate search application event.
     *
     * @param data the data
     */
    public HibernateSearchApplicationEvent(Object data) {
        super(data);
    }

    /**
     * Instantiates a new Hibernate search application event.
     *
     * @param op   the op
     * @param data the data
     */
    public HibernateSearchApplicationEvent(Op op, Object data) {
        super(data);
        this.op = op;
    }


    /**
     * Gets op.
     *
     * @return the op
     */
    public Op getOp() {
        return this.op;
    }

    /**
     * Sets op.
     *
     * @param op the op
     */
    public void setOp(Op op) {
        this.op = op;
    }

    /**
     * The enum Op.
     *
     * @see com.forsrc.hibernate.search.HibernateSearchApplicationListener.HibernateSearchHandle
     */
    public static enum Op {
        /**
         * Op index create op.
         */
        OP_INDEX_CREATE, /**
         * Op index update op.
         */
        OP_INDEX_UPDATE, /**
         * Op index delete op.
         */
        OP_INDEX_DELETE, /**
         * Query op.
         */
        QUERY, /**
         * Close op.
         */
        CLOSE;
    }

}
