package com.forsrc.utils;


import org.hibernate.dialect.MySQL5InnoDBDialect;

public class MySQL5InnoDBDialectUtf8mb4 extends MySQL5InnoDBDialect {

    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
    }
}
