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
}
