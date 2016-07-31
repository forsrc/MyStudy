package com.forsrc.lucene;


import org.hibernate.search.analyzer.Discriminator;

/**
 * The type Language discriminator.
 */
public class LanguageDiscriminator implements Discriminator {

    /**
     * The constant REF_GET_LANGUAGE.
     */
    public static final String REF_GET_LANGUAGE = "getLanguage";
    /**
     * The constant LANG_EN.
     */
    public static final String LANG_EN = "en";
    /**
     * The constant LANG_CN.
     */
    public static final String LANG_CN = "cn";
    /**
     * The constant LANG_JP.
     */
    public static final String LANG_JP = "jp";
    /**
     * The constant LANG_DEF.
     */
    public static final String LANG_DEF = "def";

    @Override
    public String getAnalyzerDefinitionName(Object value, Object entity, String filed) {
        if (entity == null) {
            return LANG_DEF;
        }

        return value == null ? LANG_DEF : value.toString();
    }

}
