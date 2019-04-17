package Core;

import java.util.HashMap;
import java.util.Map;

public class TitleCollection {
    // Map from digit to Chinese number
    // "1" -> "第一部分";  "12" -> "第十二部分"
    public Map<String, String> numberMap;

    // 部分 map
    public Map<String, TitleEntry> partsMap;

    public TitleErrorCollection errorCollection;

    public String path;

    public TitleCollection(String path) {
        this.partsMap = new HashMap<>();
        this.numberMap = new HashMap<>();
        this.errorCollection = new TitleErrorCollection();
        this.path = path;
    }

    public boolean addPart(String digitKey, String chineseTitle, String comment) {
        if (this.partsMap.containsKey(digitKey)) {
            return false;
        }

        String newPath = this.path + "\\" + chineseTitle;
        this.partsMap.put(digitKey, new TitleEntry(newPath, comment, false));
        this.numberMap.put(digitKey, chineseTitle);
        return true;
    }

}
