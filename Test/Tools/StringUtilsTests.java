package Tools;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTests {
    @Test
    public void mapChineseNumberToDigitTest() {
        String str = "十二";
        Assert.assertEquals("12", StringUtils.mapChineseNumberToDigit(str));

        str = "十五";
        Assert.assertEquals("15", StringUtils.mapChineseNumberToDigit(str));

        str = "十";
        Assert.assertEquals("10", StringUtils.mapChineseNumberToDigit(str));

        str = "八";
        Assert.assertEquals("8", StringUtils.mapChineseNumberToDigit(str));

        str = "二";
        Assert.assertEquals("2", StringUtils.mapChineseNumberToDigit(str));

        str = "二十一";
        Assert.assertEquals("21", StringUtils.mapChineseNumberToDigit(str));

        str = "一百二十一";
        Assert.assertEquals("121", StringUtils.mapChineseNumberToDigit(str));

        str = "二百四十三";
        Assert.assertEquals("243", StringUtils.mapChineseNumberToDigit(str));

        str = "三百零五";
        Assert.assertEquals("305", StringUtils.mapChineseNumberToDigit(str));
    }

    @Test
    public void getChineseNumberInStringTest() {
        String str = "第十二章";
        Assert.assertEquals("十二", StringUtils.getChineseNumberInString(str));

        str = "第二十章";
        Assert.assertEquals("二十", StringUtils.getChineseNumberInString(str));

        str = "第八章";
        Assert.assertEquals("八", StringUtils.getChineseNumberInString(str));

        str = "第三十一部分";
        Assert.assertEquals("三十一", StringUtils.getChineseNumberInString(str));

        str = "第二百二十章";
        Assert.assertEquals("二百二十", StringUtils.getChineseNumberInString(str));

        str = "第五十四章";
        Assert.assertEquals("五十四", StringUtils.getChineseNumberInString(str));
    }
}
