package real.zani.util;

import com.google.common.base.Charsets;

import java.nio.charset.Charset;

public class StringUtils {

    public static final Charset DEFAULT_CHARSET = Charsets.UTF_8;

    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        } else if (str.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public static byte[] toBytes(String s) {
        return s.getBytes(DEFAULT_CHARSET);
    }

    public static String fromBytes(byte[] bytes) {
        return new String(bytes, DEFAULT_CHARSET);
    }
}