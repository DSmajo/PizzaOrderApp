package app.fit.ba.vjezbanjeib130011.helper;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by HOME on 3.11.2016.
 */

public class InputValidationHelper
{
    public static boolean isNullOrEmpty(String string){
        return TextUtils.isEmpty(string);
    }

    public static boolean isValidDate(String date)
    {
        String regex = "^(0[1-9]|[12][0-9]|3[01])[-/ .]?(0[1-9]|1[012])[-/ .]?(19|20)\\d{2}$";

        Matcher matcher = Pattern.compile(regex).matcher(date);

        return matcher.matches();
    }

    public static boolean isValidPhone(String phone)
    {
        String regex ="^(0[0-9][0-9]|[06]|[0-6])-?[0-9]{3}-?[0-9]{3,4}$";

        Matcher matcher = Pattern.compile(regex).matcher(phone);

        return matcher.matches();
    }
}
