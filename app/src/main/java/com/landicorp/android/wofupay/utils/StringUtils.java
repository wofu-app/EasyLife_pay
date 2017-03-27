package com.landicorp.android.wofupay.utils;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/3/21 0021.
 */
public class StringUtils {

    /**
     * 移动号段：134,135,136,137,138,139,150,151,152,157,158,159,182,183,184,
     * 187,188,147,178,1705
     *
     * 联通号段: 130,131,132,155,156,185,186,145,176,1709
     *
     * 电信号段:133,153,180,181,189,177,1700
     *
     * @param phone
     * @return
     */
    public static boolean isPhoneNumber(String phone) {
        if (phone == null)
            return false;
        String str = "^1(3[0-9]|4[57]|5[0-35-9]|8[0-9]|70)\\d{8}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 获取当前时间
     *
     * @param type
     *            时间格式化的格式
     * @return 格式化的字符串
     */
    public static String getStringDate(String type) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(type);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    private static final int DATE_TIME_LEN = 10;
    private static final int DATE_LEN = 4;
    private static final int TIME_LEN = 6;
    private static final int DATA_LEN = 8;

    public static String formDateTime(String dateTime) {
        StringBuilder sb = new StringBuilder();
        switch (dateTime.length()) {
            case DATE_TIME_LEN:
                sb.append(dateTime.substring(0, 2));
                sb.append('/');
                sb.append(dateTime.substring(2, 4));
                sb.append(' ');
                sb.append(dateTime.substring(4, 6));
                sb.append(':');
                sb.append(dateTime.substring(6, 8));
                sb.append(':');
                sb.append(dateTime.substring(8, 10));
                break;
            case DATE_LEN:
                sb.append(dateTime.substring(0, 2));
                sb.append('/');
                sb.append(dateTime.substring(2, 4));
                break;
            case TIME_LEN:
                sb.append(dateTime.substring(0, 2));
                sb.append(':');
                sb.append(dateTime.substring(2, 4));
                sb.append(':');
                sb.append(dateTime.substring(4, 6));
                break;
            case DATA_LEN:
                sb.append(dateTime.substring(0, 4));
                sb.append('-');
                sb.append(dateTime.substring(4, 6));
                sb.append('-');
                sb.append(dateTime.substring(6, 8));
                break;
            default:
                return dateTime;
        }
        return sb.toString();
    }

    /**
     * 描述：MD5加密.
     *
     * @param str
     *            要加密的字符串
     */
    public final static String MD5(String str) {
        char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
                'e', 'f' };
        try {
            byte[] strTemp = str.getBytes("utf-8");
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte tmp[] = mdTemp.digest(); // MD5 的计算结果是一个 128 位的长整数，
            // 用字节表示就是 16 个字节
            char strs[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
            // 所以表示成 16 进制需要 32 个字符
            int k = 0; // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
                // 转换成 16 进制字符的转换
                byte byte0 = tmp[i]; // 取第 i 个字节
                strs[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
                // >>> 为逻辑右移，将符号位一起右移
                strs[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
            }
            return new String(strs).toLowerCase(); // 换后的结果转换为字符串
        } catch (Exception e) {
            return null;
        }
    }

}
