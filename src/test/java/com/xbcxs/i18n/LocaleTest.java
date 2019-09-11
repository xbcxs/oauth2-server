package com.xbcxs.i18n;

import org.junit.BeforeClass;
import org.junit.Test;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author xiaosh
 * @date 2019/9/9
 */
public class LocaleTest {

    Locale chinaLocale;
    Locale americaLocale;

    @BeforeClass
    public void init() {
        chinaLocale = new Locale("zh", "CN");
        americaLocale = new Locale("en", "US");
    }



    @Test
    public void numberFormat() {
        final double money = 2972.29d;

        NumberFormat format = NumberFormat.getCurrencyInstance(chinaLocale);
        System.out.println("中国：" + format.format(money));

        NumberFormat format2 = NumberFormat.getCurrencyInstance(americaLocale);
        System.out.println("美国：" + format2.format(money));
    }
}
