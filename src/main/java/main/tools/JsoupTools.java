package main.tools;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

public class JsoupTools {
    public static HashSet<String> getHrefElements(Document doc, String url){ //Document doc, String url) {
        List<String> list = doc.body().getElementsByAttribute("href")
                .eachAttr("href").stream()
                .filter(getPositivePattern(url).asPredicate())
                .filter(getNegativePattern().asPredicate().negate())
                .toList();
        HashSet<String> set = new HashSet<>();
        for(String str : list) {
            str = str.replaceFirst(url, "").trim();
            if(!str.isEmpty())
                set.add(str);
            else {
                set.add("/");
            }
        }
        return set;
    }

    public static HashSet<String> getHrefElements(String html, String url){
        HashSet<String> set = new HashSet<>();
        List<String> list = Jsoup.parse(html).body().getElementsByAttribute("href")
                .eachAttr("href").stream()
                .filter(getPositivePattern(url).asPredicate())
                .filter(getNegativePattern().asPredicate().negate())
                .toList();
        for(String str : list) {
            str = str.replaceFirst(url, "").trim();
            if(str.indexOf("http://") != -1 || str.indexOf("https://") != -1) {
                continue;
            }
            if(!str.isEmpty())
                set.add(str);
            else {
                set.add("/");
            }
        }
        return set;
    }

    private static Pattern getPositivePattern(String url) {
        return Pattern.compile("^/.+|^" + url);
    }

    private static Pattern getNegativePattern(){
        return Pattern.compile(".+#$|.+.css$|.+.css?|.+.png$|.+.ico$|.+.jpeg$|.+.jpg$|.+.jpe$|.+.jfif$"
                + "|.+.bmp$|.+.dib$|.+.gif$|.+.json$|.+.docx$|.+.pdf$|.+.xlsx$|.+.pptx$|.+.mp3$"
        + "|.+.mp4$|.+.csv$|.+.xml$|.+.exe|.+.apk$|.+.rar$|.+.zip$|.+.jar$|.+.js|.+.svg");
    }
}
