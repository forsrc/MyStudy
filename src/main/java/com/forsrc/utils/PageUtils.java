package com.forsrc.utils;

public final class PageUtils {

    public static final int SIZE = 10;

    private PageUtils() {
    }

    public static int getTotalPage(final int size, final long total) {

        double s = size <= 0 ? SIZE : size;
        double t = total <= 0 ? 1 : total;
        double page = Math.ceil(t / s);
        return (int) page;
    }
}
