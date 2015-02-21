package cs122b.utils;

/**
 * Created by dinhho on 2/19/15.
 */
public class StringUtil {
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static int isStringIntParsable(String s) {
        int r = -1;
        try {
            r = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("Integer.Parse Error for String " + s);
            r = -1;
        }
        return r;
    }

}
