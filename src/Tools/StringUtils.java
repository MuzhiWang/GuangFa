package Tools;

/**
 * Created by muwang on 4/16/2019.
 */
public class StringUtils {
    public static boolean isLetterChinese(String str) {
        String regex = "[\\u4e00-\\u9fa5]+";
        return str.matches(regex);
    }
}
