package real.zani.util;

public class BASE64 {

    public static String encode(String clearText) {
        return StringUtils.fromBytes(java.util.Base64.getEncoder().encode(StringUtils.toBytes(clearText)));
    }

    public static String encode(byte[] clearBytes) {
        return java.util.Base64.getEncoder().encodeToString(clearBytes);
    }

    public static String decode(String src) {
        return StringUtils.fromBytes(java.util.Base64.getDecoder().decode(StringUtils.toBytes(src)));
    }

    public static byte[] decodeToBytes(String src) {
        return java.util.Base64.getDecoder().decode(src);
    }

}