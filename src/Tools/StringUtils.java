package Tools;

import Settings.FileSettings;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by muwang on 4/16/2019.
 */
public class StringUtils {
    private static Pattern CHN_NUM_PATTERN = Pattern.compile("[一二三四五六七八九][十百千]?");
    private static Map<Character, Integer> CHN_UNITS = new HashMap<>();
    private static Map<Character, Integer> CHN_NUMS = new HashMap<>();

    static {
        CHN_UNITS.put('十', 10);
        CHN_UNITS.put('百', 100);
        CHN_UNITS.put('千', 1000);
        CHN_UNITS.put('万', 10000);
        CHN_UNITS.put('亿', 10000000);
        CHN_NUMS.put('一', 1);
        CHN_NUMS.put('二', 2);
        CHN_NUMS.put('三', 3);
        CHN_NUMS.put('四', 4);
        CHN_NUMS.put('五', 5);
        CHN_NUMS.put('六', 6);
        CHN_NUMS.put('七', 7);
        CHN_NUMS.put('八', 8);
        CHN_NUMS.put('九', 9);
    }

    public static boolean isLetterChinese(String str) {
        String regex = "[\\u4e00-\\u9fa5]+";
        return str.matches(regex);
    }

    /*
    * 汉字数字转化为string  "十二" -> "12"
    * */
    public static String mapChineseNumberToDigit(String str) {
        if (!StringUtils.isLetterChinese(str)) {
            return "";
        }

        return Integer.toString(StringUtils.parseNumber(str));
    }

    // Get "十二" in "第十二章"
    public static String getChineseNumberInString(String str) {
        Pattern pattern = Pattern.compile(FileSettings.ExcelCellSettings.CHINESE_NUMBER_PATTERN);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    // Get "1—1" as "1-1"
    public static String getUniformString(String str) {
        return str.replace(FileSettings.EMPTY_SPACE_FORMAT, "").replaceAll(FileSettings.ExcelCellSettings.TITLE_SPLITTER, "-");
    }

    // Get "1-2-3" from "1-2-3 项目审计"
    public static String getTitleInString(String str) {
        String uniformStr = StringUtils.getUniformString(str);
        Pattern pattern = Pattern.compile(FileSettings.ExcelCellSettings.TITLE_FORMAT);
        Matcher matcher = pattern.matcher(uniformStr);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    /**
     * 将汉字转换为数字
     *
     * @param chnNum
     * @return
     */
    public static int parseNumber(String chnNum) {
        chnNum = chnNum.replaceAll("(?<![一二三四五六七八九])十", "一十").replaceAll("零", "");
        Pattern pattern = Pattern.compile("[万亿]");
        Matcher m = pattern.matcher(chnNum);
        BigInteger result = BigInteger.valueOf(0);
        int index = 0;
        while (m.find()) {
            int end = m.end();
            int multiple = CHN_UNITS.get(m.group().charAt(0));
            String num = chnNum.substring(index, m.start());
            result = result.add(getNumber(num)).multiply(BigInteger.valueOf(multiple));
            index = end;
        }
        String num = chnNum.substring(index);
        result = result.add(getNumber(num));
        return result.intValue();
    }

    /**
     * 将小于一万的汉字数字，转换为BigInteger
     *
     * @param chnNum
     * @return
     */
    private static BigInteger getNumber(String chnNum) {
        BigInteger number = BigInteger.valueOf(0);
        Matcher m = CHN_NUM_PATTERN.matcher(chnNum);
        m.reset(chnNum);
        while (m.find()) {
            String subNumber = m.group();
            if (subNumber.length() == 1) {
                number = number.add(BigInteger.valueOf(CHN_NUMS.get(subNumber.charAt(0))));
            } else if (subNumber.length() == 2) {
                number = number.add(BigInteger.valueOf(CHN_NUMS.get(subNumber.charAt(0))).multiply(BigInteger.valueOf(CHN_UNITS.get(subNumber.charAt(1)))));
            }
        }
        return number;
    }
}
