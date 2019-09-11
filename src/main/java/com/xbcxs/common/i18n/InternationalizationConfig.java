package com.xbcxs.common.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author xiaosh
 * @date 2019/9/9
 */
public class InternationalizationConfig {

    private static String BUNDLE_I18N_NAME;

    static {
        BUNDLE_I18N_NAME = "i18n/content";
    }

    public static String getString(String i18nKey){
        return ResourceBundle.getBundle(BUNDLE_I18N_NAME, Locale.CHINA).getString(i18nKey);
    }

    public static String getFormatString(String i18nKey, Object[] params){
        return new MessageFormat(ResourceBundle.getBundle(BUNDLE_I18N_NAME, Locale.CHINA).getString(i18nKey), Locale.CHINA).format(params);
    }

}
