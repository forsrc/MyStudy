package com.forsrc.lucene;


import org.hibernate.search.analyzer.Discriminator;

public class LanguageDiscriminator implements Discriminator {

    public static final String REF_GET_LANGUAGE = "getLanguage";
    public static final String LANG_EN = "en";
    public static final String LANG_CN = "cn";
    public static final String LANG_JP = "jp";
    public static final String LANG_DEF = "def";

    @Override
    public String getAnalyzerDefinitionName(Object value, Object entity, String filed) {
        if (entity == null) {
            return LANG_DEF;
        }

        return value == null ? LANG_DEF : value.toString();
    }

}
