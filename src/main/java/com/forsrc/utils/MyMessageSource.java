package com.forsrc.utils;


import org.springframework.context.MessageSource;

public class MyMessageSource {
    private MessageSource MessageSource;

    private MyMessageSource() {

    }

    public static MyMessageSource getInstance() {
        return MyMessageSourceClass.INSTANCE;
    }

    public org.springframework.context.MessageSource getMessageSource() {
        return MessageSource;
    }

    public void setMessageSource(org.springframework.context.MessageSource messageSource) {
        MessageSource = messageSource;
    }

    private static class MyMessageSourceClass {
        private static final MyMessageSource INSTANCE = new MyMessageSource();
    }
}
