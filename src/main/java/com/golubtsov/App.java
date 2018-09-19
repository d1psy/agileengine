package com.golubtsov;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class App {

    private static final String CHARSET_NAME = "utf8";
    private static final String TEXT_TO_FIND = "Make everything OK";

    public static void main(String[] args) {

        for (String str : args) {
            Element buttonOpt = findElementById(new File(str));
            Element main = getPath(buttonOpt);
            String path = null;
            if (Objects.nonNull(main)) {
                if (buttonOpt.hasParent()) {
                    path = getTest(main);
                }
                System.out.println(path);
            } else {
                System.out.println("No element was found for: " + str);
            }
        }
    }

    private static Element getPath(Element element) {
        Element makeOkElement = null;
        Elements elements = element.children();
        for (Element el : elements) {
            if (el.text().equals(TEXT_TO_FIND)) {
                makeOkElement = el;
                return makeOkElement;
            } else {
                makeOkElement = getPath(el);
            }
            if (Objects.nonNull(makeOkElement) && makeOkElement.text().equals(TEXT_TO_FIND)) {
                if (makeOkElement.children().size() == 0) {
                    return makeOkElement;
                } else {
                    return getPath(makeOkElement);
                }
            }
        }
        return makeOkElement;
    }

    private static String getTest(Element element) {
        Elements elements = element.parents();
        StringBuilder path = new StringBuilder(element.tag().toString());
        for (Element el : elements) {
            path.insert(0, el.tag().toString() + " > ");
        }
        return path.toString();
    }

    private static Element findElementById(File htmlFile) {
        try {
            Document doc = Jsoup.parse(
                    htmlFile,
                    CHARSET_NAME,
                    htmlFile.getAbsolutePath());
            return doc.body();
        } catch (IOException e) {
            return null;
        }
    }

}