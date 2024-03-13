package com.example.nc1.server.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class JsoupTools {
    public static String getTextByClass(Element element, String className){
        if (element == null)
            return null;

        Element elTime = element.getElementsByClass(className).first();

        if ( elTime != null)
            return elTime.text();

        return null;
    }

    public static String getTextByTag(Element element, String tag) {
        if (element == null)
            return null;

        Element elA = element.getElementsByTag(tag).first();

        if ( elA != null)
            return elA.text();

        return null;
    }

    public static String getTextByAttribute(Element element, String attribute) {
        if (element == null)
            return null;

        Attribute attr = element.attribute(attribute);

        if (attr == null)
            return null;

        return attr.getValue();
    }

    public static String getTextByAttributeInTag(Element element, String attribute, String tag) {
        if (element == null)
            return null;

        Element elTag = element.getElementsByTag(tag).first();

        return getTextByAttribute(elTag, attribute);
    }

    public static Document connect(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla")
                .timeout(5000)
                .referrer("https://google.com")
                .get();
    }
}
