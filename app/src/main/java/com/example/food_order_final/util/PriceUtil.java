package com.example.food_order_final.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class PriceUtil {

    public static String formatNumber(double totalAmount) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(symbols);

        return decimalFormat.format(totalAmount);
    }
}
