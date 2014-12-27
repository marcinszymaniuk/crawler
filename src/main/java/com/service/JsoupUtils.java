package com.service;

import com.model.Category;
import com.model.PageMetadata;
import com.model.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class JsoupUtils {

    public Document getJsoupDocument(File input) {
        String costOfDeliveryString = null;
        try {
            Document doc = Jsoup.parse(input,
                    "UTF-8", "http://allegro.pl/wyposazenie-123?ref=simplified-category-tree");

            return doc;
        } catch (IOException e) {
            /*
            We need to handle it a bit better. For now I just leave the println and return null.
             */
            e.printStackTrace();
            return null;
        }
    }

    public Document getJsoupDocument(String url) {
        String costOfDeliveryString = null;
        try {
            Document doc = Jsoup.connect(url).timeout(10000).get();
            return doc;

        } catch (IOException e) {
            /*
            We need to handle it a bit better. For now I just leave the println and return null.
             */
            e.printStackTrace();
            return null;
        }
    }

}
