package app.fit.ba.vjezbanjeib130011.helper;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by HOME on 20.10.2016.
 */

public class DateDecimalConverterHelper
{
    public static String to_dd_mm_yyyy(Date date)
    {
        return new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).format(date);
    }

    public static String to_dd_mm_yyyy_hh_mm(Date date)
    {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMAN).format(date);
    }

    public static String decimal_0_00(float value)
    {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return  decimalFormat.format(value);
    }
}
