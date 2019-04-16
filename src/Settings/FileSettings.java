package Settings;

/**
 * Created by muwang on 4/4/2019.
 */
public final class FileSettings {
    // Just an example.
    public final static String FILE_FORMAT_EXAMPLE = "1-2-3-4项目规划 表1.pdf";

    public final static String FILE_FORMAT_SPLITTER = "-";

    public final static String FILE_APPEND_INDEX_SPLITTER = "+++";

    public final static String DIRECTORY_PATH = "E:\\Muzhi\\Test";

    public final static String FILE_EXT_FORMAT_STR = "(?i)(pdf|txt)";

    public final static String FILE_EXT_FORMAT = "(?i).(pdf|txt)";

    public final static String EXCEL_FORMAT_STR = "(?i)(xlsx|xls)";

    public final static String EXCEL_FORMAT = "(?i).(xlsx|xls)";

    public final static String FILE_SINGLE_PATH_FORMAT = "^(\\d+).*$";

    public final static String UNCERTERN_FILE_SUFFIX = "=====NeedToBeRevised";

    public static class ExcelCellSettings {
        // Match as "1-2-3" or "12"
        public final static String TITLE_FORMAT = "\\b(((\\d+)\\-){1,10}(\\d+)|(\\d+))\\b";

        public final static String TITLE_SPLITTER = "(－|-)";

        public final static String TITLE_ZHANG = "章";

        public final static String TITLE_PART = "部分";

        public final static String CHINESE_NUMBER = "一二三四五六七八九〇十百千万";

        // e.g.  "第一章" "第二部分"
        public final static String CHINESE_TITLE_FORMAT = String.format("第[%s]+(%s|%s)", CHINESE_NUMBER, TITLE_ZHANG, TITLE_PART);
    }
}
