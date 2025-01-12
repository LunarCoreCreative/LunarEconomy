package org.lunar.lunarEconomy;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class NumberFormatter {
    public static String format(double value) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.of("pt", "BR"));
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
        return decimalFormat.format(value);
    }
}
