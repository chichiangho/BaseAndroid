package com.changhong.park.base.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    private static final String TAG = "StringUtil";

    /**
     * 判断str1和str2是否相同
     *
     * @param str1 str1
     * @param str2 str2
     * @return true or false
     */
    public static boolean equals(String str1, String str2) {
        return str1 == str2 || str1 != null && str1.equals(str2);
    }

    /**
     * 判断str1和str2是否相同(不区分大小写)
     *
     * @param str1 str1
     * @param str2 str2
     * @return true or false
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 != null && str1.equalsIgnoreCase(str2);
    }

    /**
     * 判断字符串str1是否包含字符串str2
     *
     * @param str1 源字符串
     * @param str2 指定字符串
     * @return true源字符串包含指定字符串，false源字符串不包含指定字符串
     */
    public static boolean contains(String str1, String str2) {
        return str1 != null && str1.contains(str2);
    }

    public static String getFileType(String fileName) {
        if (fileName != null) {
            int typeIndex = fileName.lastIndexOf(".");
            if (typeIndex != -1) {
                String fileType = fileName.substring(typeIndex + 1).toLowerCase();
                return fileType;
            }
        }
        return "";
    }

    /**
     * 验证字符串是否符合email格式
     *
     * @param email 需要验证的字符串
     * @return 验证其是否符合email格式，符合则返回true,不符合则返回false
     */
    public static boolean isEmail(String email) {
        // 通过正则表达式验证email是否合法
        if (email == null) {
            return false;
        }
        if (email.toLowerCase().matches(
                "^([a-zA-Z0-9_\\.-])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})+$")) {
            return true;
        }
        return false;
    }

    /**
     * 验证字符串是否为数字
     *
     * @param str 需要验证的字符串
     * @return 不是数字返回false，是数字就返回true
     */
    public static boolean isNumeric(String str) {
        return str != null && str.matches("[0-9]*");
    }

    /**
     * 替换字符串中特殊字符
     *
     * @param strData 源字符串
     * @return 替换了特殊字符后的字符串，如果strData为NULL，则返回空字符串
     */
    public static String encodeString(String strData) {
        if (strData == null) {
            return "";
        }
        return strData.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;")
                .replaceAll("'", "&apos;").replaceAll("\"", "&quot;");
    }

    /**
     * 还原字符串中特殊字符
     *
     * @param strData strData
     * @return 还原后的字符串
     */
    public static String decodeString(String strData) {
        if (strData == null) {
            return "";
        }
        return strData.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&apos;", "'")
                .replaceAll("&quot;", "\"").replaceAll("&amp;", "&");
    }

    /**
     * 按照一个汉字两个字节的方法计算字数
     *
     * @param string String
     * @return 返回字符串's count
     */
    public static int count2BytesChar(String string) {
        int count = 0;
        if (string != null) {
            for (char c : string.toCharArray()) {
                count++;
                if (isChinese(c)) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 判断字符串中是否包含中文
     *
     * @param str 检索的字符串
     * @return 是否包含中文
     */
    public static boolean hasChinese(String str) {
        boolean hasChinese = false;
        if (str != null) {
            for (char c : str.toCharArray()) {
                if (isChinese(c)) {
                    hasChinese = true;
                    break;
                }
            }
        }
        return hasChinese;
    }

    /**
     * 截取字符串，一个汉字按两个字符来截取
     *
     * @param src        源字符串
     * @param charLength 字符长度
     * @return 截取后符合长度的字符串
     */
    public static String subString(String src, int charLength) {
        if (src != null) {
            int i = 0;
            for (char c : src.toCharArray()) {
                i++;
                charLength--;
                if (isChinese(c)) {
                    charLength--;
                }
                if (charLength <= 0) {
                    if (charLength < 0) {
                        i--;
                    }
                    break;
                }
            }
            return src.substring(0, i);
        }
        return src;
    }

    /**
     * 对字符串进行截取, 超过则以...结束
     *
     * @param originStr     原字符串
     * @param maxCharLength 最大字符数
     * @return 截取后的字符串
     */
    public static String trim(String originStr, int maxCharLength) {
        if (TextUtils.isEmpty(originStr)) {
            return "";
        }
        int count = 0;
        int index = 0;
        int originLen = originStr.length();
        for (index = 0; index < originLen; index++) {
            char c = originStr.charAt(index);
            int len = 1;
            if (isChinese(c)) {
                len++;
            }
            if (count + len <= maxCharLength) {
                count += len;
            } else {
                break;
            }
        }

        if (index < originLen) {
            return originStr.substring(0, index) + "...";
        } else {
            return originStr;
        }
    }

    /**
     * 判断参数c是否为中文
     *
     * @param c char
     * @return 是中文字符返回true，反之false
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    /**
     * 检测密码强度
     *
     * @param password 密码
     * @return 密码强度（1：低 2：中 3：高）
     */
    public static int checkStrong(String password) {
        boolean num = false;
        boolean lowerCase = false;
        boolean upperCase = false;
        boolean other = false;

        int threeMode = 0;
        int fourMode = 0;

        for (int i = 0; i < password.length(); i++) {
            // 单个字符是数字
            if (password.codePointAt(i) >= 48 && password.codePointAt(i) <= 57) {
                num = true;
            }
            // 单个字符是小写字母
            else if (password.codePointAt(i) >= 97 && password.codePointAt(i) <= 122) {
                lowerCase = true;
            }
            // 单个字符是大写字母
            else if (password.codePointAt(i) >= 65 && password.codePointAt(i) <= 90) {
                upperCase = true;
            }
            // 特殊字符
            else {
                other = true;
            }
        }

        if (num) {
            threeMode++;
            fourMode++;
        }

        if (lowerCase) {
            threeMode++;
            fourMode++;
        }

        if (upperCase) {
            threeMode++;
            fourMode++;
        }

        if (other) {
            fourMode = fourMode + 1;
        }

        // 数字、大写字母、小写字母只有一个，密码强度低（只有一种特殊字符也是密码强度低）
        if (threeMode == 1 && !other || fourMode == 1) {
            return 1;
        }
        // 四种格式有其中两个，密码强度中
        else if (fourMode == 2) {
            return 2;
        }
        // 四种格式有三个或者四个，密码强度高
        else if (fourMode >= 3) {
            return 3;
        }
        // 正常情况下不会出现该判断
        else {
            return 3;
        }
    }

    /**
     * 返回一个制定长度范围内的随机字符串
     *
     * @param min 范围下限
     * @param max 范围上限
     * @return 字符串
     */
    public static String createRandomString(int min, int max) {
        StringBuffer strB = new StringBuffer();
        SecureRandom random = new SecureRandom();
        int lenght = min;
        if (max > min) {
            lenght += random.nextInt(max - min + 1);
        }
        for (int i = 0; i < lenght; i++) {
            strB.append((char) (97 + random.nextInt(26)));
        }
        return strB.toString();
    }

    /**
     * 用于获取字符串中字符的个数
     *
     * @param content 文本内容
     * @return 返回字符的个数
     */
    public static int getStringLeng(String content) {
        return (int) Math.ceil(count2BytesChar(content) / 2.0);
    }


    /**
     * 去掉字符串前后空格
     *
     * @param string 要去掉前后空格的字符串
     * @return 去掉前后空格的字符串
     */
    public static String stringTrimAllSpace(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        string = string.trim();
        while (string.startsWith(" ")) {
            string = string.substring(1, string.length());
        }
        return string;
    }

    /**
     * 生成唯一的字符串对象
     *
     * @return 唯一的字符串
     */
    public static String generateUniqueID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 是否为null或空字符串
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str.trim())) {
            return true;
        }
        return false;
    }

    /**
     * 非null或非空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断是否为手机号码
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        if (null != phoneNumber) {
            phoneNumber = phoneNumber.trim();
            String reg = "1[3,4,5,6,7,8,9]{1}\\d{9}";
            return phoneNumber.matches(reg);
        } else {
            return false;
        }
    }

    /**
     * 如果是手机号时，将其中4位变成*号
     *
     * @param phoneNumber
     * @return
     */
    public static String encryptPhoneNumber(String phoneNumber) {
        if (null == phoneNumber) {
            return "";
        } else if (isPhoneNumber(phoneNumber)) {
            String encryption = phoneNumber.substring(0, 3);
            encryption += "****";
            encryption += phoneNumber.substring(7, 11);
            return encryption;
        } else {
            return phoneNumber;
        }

    }

    /**
     * 判断是否是数字
     */
    public static boolean isNumber(String str) {
        String reg = "[0-9]+";
        return str.matches(reg);
    }


    /**
     * <从异常中获取调用栈>
     *
     * @param ex
     * @return String [返回类型说明]
     * @throws throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static String getExceptionStackTrace(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        return writer.toString();
    }

    /**
     * Unicode转化为中文
     */
    public static String decodeUnicode(String dataStr) {
        final StringBuffer buffer = new StringBuffer();
        String tempStr = "";
        String operStr = dataStr;
        if (operStr != null && operStr.indexOf("\\u") == -1) {
            return buffer.append(operStr).toString();
        }
        if (operStr != null && !operStr.equals("") && !operStr.startsWith("\\u")) {
            tempStr = substring(operStr, 0, operStr.indexOf("\\u"));
            operStr = substring(operStr, operStr.indexOf("\\u"), operStr.length());
        }
        buffer.append(tempStr);
        // 循环处理,处理对象一定是以unicode编码字符打头的字符串
        while (operStr != null && !operStr.equals("") && operStr.startsWith("\\u")) {
            tempStr = substring(operStr, 0, 6);
            operStr = substring(operStr, 6, operStr.length());
            String charStr = "";
            charStr = substring(tempStr, 2, tempStr.length());
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串
            buffer.append(letter);
            if (operStr.indexOf("\\u") == -1) {
                buffer.append(operStr);
            } else { // 处理operStr使其打头字符为unicode字符
                tempStr = substring(operStr, 0, operStr.indexOf("\\u"));
                operStr = substring(operStr, operStr.indexOf("\\u"), operStr.length());
                buffer.append(tempStr);
            }
        }
        return buffer.toString();
    }

    /**
     * 字条串截取
     *
     * @param str   源字符串
     * @param start 开始位置
     * @param end   结束位置
     */
    public static String substring(String str, int start, int end) {
        if (isEmpty(str)) {
            return "";
        }
        int len = str.length();
        if (start > end) {
            return "";
        }
        if (start > len) {
            return "";
        }
        if (end > len) {
            return str.substring(start, len);
        }
        return str.substring(start, end);
    }

    /**
     * 字条串截取
     *
     * @param str   源字符串
     * @param start 开始位置
     * @return String [返回类型说明]
     * @throws throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static String substring(String str, int start) {
        if (isEmpty(str)) {
            return "";
        }
        int len = str.length();
        if (start > len) {
            return "";
        }
        return str.substring(start);
    }

    /**
     * 将字符串截取为指定长度的字符串
     */
    public static String cutString(String content, int length) {
        if (isEmpty(content)) {
            return "";
        }
        if (content.length() <= length) {
            return content;
        }
        return content.substring(0, length);
    }

    /**
     * 将字符串多空格，多换行替换成一个空格
     */
    public static String tirmString(String content) {
        if (isEmpty(content)) {
            return "";
        }
        return content.replaceAll("[ \n\r\t]+", " ");
    }

    /**
     * 判断字符是否数字
     *
     * @param str
     * @return
     */
    public static boolean isDigital(String str) {
        return str.matches("(-)?\\d+");
    }

    /**
     * 判断字符是否带小数
     *
     * @param str
     * @return
     */
    public static boolean isDouble(String str) {
        if (isDigital(str)) {
            return true;
        }
        return str.matches("(-)?\\d+\\.\\d+");
    }

    /**
     * 返回x小数,如果小数部分不够两位则自动填充小数部分
     */
    public static String getProcess(String process) {
        // 空字符
        if (null == process || "".equals(process.trim())) {
            return "";
        }

        // 非整数或小数
        if (!(isDigital(process) || isDouble(process))) {
            return process;
        }

        int index = process.indexOf('.');

        // 无小数部分
        if (-1 == index) {
            return process + ".00";
        }

        // 整数部分
        String prefix = process.substring(0, index);

        // 小数部分
        String postfix = process.substring(index + 1);

        StringBuilder result = new StringBuilder();

        // 小数部分长度
        switch (postfix.length()) {
            // 无小数部分
            case 0:
                result.append(prefix).append(".00");
                break;
            // 只有一位小数
            case 1:
                result.append(prefix).append('.').append(postfix).append('0');
                break;
            // 两位小数
            case 2:
                result.append(prefix).append('.').append(postfix);
                break;
            // 三位或以上小数,需要进行四舍五入
            default:
                result.append(
                        String.valueOf(Math.round(Double.parseDouble(prefix + postfix.substring(0, 3)) / 10)))
                        .insert(result.length() - 2, '.');
                break;
        }
        return result.toString();
    }

    /**
     * 去掉url中多余的斜杠
     *
     * @param url 字符串
     * @return 去掉多余斜杠的字符串
     */
    public static String fixUrl(String url) {
        if (null == url) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer(url);
        for (int i = stringBuffer.indexOf("//", stringBuffer.indexOf("//") + 2); i != -1; i = stringBuffer
                .indexOf("//", i + 1)) {
            stringBuffer.deleteCharAt(i);
        }
        return stringBuffer.toString();
    }

    /**
     * 字符串翻转
     *
     * @param s
     * @return
     */
    public static String reverse(String s) {
        return new StringBuffer(s).reverse().toString();
    }

    /**
     * 截取指定字节长度的字符串，不能返回半个汉字
     *
     * @param str
     * @param length
     * @return
     */
    public static String getSubString(String str, int length) {
        int count = 0;
        int offset = 0;
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] > 256) {
                offset = 2;
                count += 2;
            } else {
                offset = 1;
                count++;
            }

            if (count == length) {
                return str.substring(0, i + 1);
            }
            if ((count == length + 1 && offset == 2)) {
                return str.substring(0, i);
            }
        }
        return "";
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    public static String getAlpha(String str) {
        if (str == null) {
            return "#";
        }

        if (str.trim().length() == 0) {
            return "#";
        }

        char c = str.trim().substring(0, 1).charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase(); // 大写输出
        } else {
            return "#";
        }
    }

    /**
     * 字符串解析为字符串数组
     *
     * @param str
     * @return
     */
    public static String[] getStrings(String str) {
        return str.split(" ");
    }

    /***
     * 是否为字母
     *
     * @param str
     * @return
     */
    public static boolean getLetter(String str) {
        return str.matches("^[A-Za-z]+$");
    }

    /**
     * 得到全拼或简拼
     *
     * @param strs 字符串数组
     * @param type 全拼还是简拼 0---简拼 1--全拼
     * @return
     */
    public static String getString(String[] strs, int type) {
        String[] newStrs = new String[strs.length];
        int j = 0;
        for (int i = 0; i < strs.length; i++) {
            String firstLetter = strs[i].substring(0, 1);
            if (getLetter(firstLetter)) {
                // type=0 out 简拼
                if (type == 0) {
                    newStrs[j] = firstLetter;
                }
                // type=1 out 全拼
                else {
                    newStrs[j] = strs[i];
                }
                j++;
            }
        }
        StringBuffer sb = new StringBuffer();
        for (int k = 0; k < newStrs.length; k++) {
            if (newStrs[k] != null) {
                sb.append(newStrs[k]);
            }
        }
        return sb.toString();
    }

    /**
     * 将右斜杠替换为左斜杠
     *
     * @param strData strData
     * @return
     */
    public static String replaceRightSlash(String strData) {
        if (strData == null || strData.equals("")) {
            return "";
        }
        return strData.replace('\\', '/');
    }

    /**
     * JAVA判断字符串数组中是否包含某字符串元素
     *
     * @param substring 某字符串
     * @param source    源字符串数组
     * @return 包含则返回true，否则返回false
     */
    public static boolean isIn(String substring, String[] source) {
        if (source == null || source.length == 0) {
            return false;
        }
        for (int i = 0; i < source.length; i++) {
            String aSource = source[i];
            if (aSource.equals(substring)) {
                return true;
            }
        }
        return false;
    }

    /**
     * java去除字符串中的空格、回车、换行符、制表符
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static SpannableStringBuilder getColorForegroundStr(Context ctx, String wholeStr,
                                                               int colorRes, String... foregroundStrs) {
        SpannableStringBuilder style = new SpannableStringBuilder(wholeStr);
        for (int i = 0; i < foregroundStrs.length; i++) {
            int fstart = wholeStr.indexOf(foregroundStrs[i]);
            int fend = fstart + foregroundStrs[i].length();
            style.setSpan(new ForegroundColorSpan(ctx.getResources().getColor(colorRes)), fstart,
                    fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return style;
    }

    public static String getPriceNumberString(double price) {
        if (0 == Double.compare(price, (int) price)) {
            DecimalFormat df = new DecimalFormat("0");
            return df.format(price);
        } else {
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(price);
        }
    }

    /**
     * MD5加密
     */
    public static String getMD5String(String str) {
        MessageDigest md;
        String result = str;
        try {
            md = MessageDigest.getInstance("MD5");
            result = MD5(str, md);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static String MD5(String strSrc, MessageDigest md) {
        byte[] bt = strSrc.getBytes();
        md.update(bt);
        String strDes = bytes2Hex(md.digest()); // to HexString
        return strDes;
    }

    private static String bytes2Hex(byte[] bts) {
        StringBuffer des = new StringBuffer();
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des.append("0");
            }
            des.append(tmp);
        }
        return des.toString();
    }

    /**
     * 检查字符串是否为字母和数字组成（主要用于密码检查）
     *
     * @param str
     * @return
     */
    public static boolean isNumAndLetter(String str) {
        String regNum = "^[0-9]+$";
        String reg = "^[a-zA-Z]+$";
        Pattern patternNum = Pattern.compile(regNum);
        Pattern pattern = Pattern.compile(reg);
        Matcher matcherNum = patternNum.matcher(str);
        Matcher matcher = pattern.matcher(str);
        return !(matcher.matches() || matcherNum.matches());
    }

    public static int getNextPage(int pageSize, int size) {
        if (size % pageSize == 0)
            return size / pageSize + 1;
        else
            return size / pageSize + 2;// 防止无限取最后几条数据
    }

    /**
     * 获取日期的毫秒数
     *
     * @param date
     * @return
     */
    public static long getDateLongData(String date) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            return formatter.parse(date + " 23:59:59").getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
