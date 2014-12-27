package com.service;

import com.model.Category;
import com.model.PageMetadata;
import com.model.Product;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * Created by marcin on 27/12/14.
 */
public class Crawler {

    public static void main(String[] args) throws InterruptedException {
        JsoupUtils jsoupUtils = new JsoupUtils();
        Parser parser = new AllegroParser();

        getDeeper(jsoupUtils, parser, "http://allegro.pl/dzial/dom-i-zdrowie");
    }

    private static void getDeeper(JsoupUtils jsoupUtils, Parser parser, String url) throws InterruptedException {
        Document doc = jsoupUtils.getJsoupDocument(url);
        PageMetadata pageMetadata = parser.parsePageMetadata(doc, new Category());
        if(pageMetadata.isLowestLevel()){

            //Pobierz produkty
            List<String> productUrls = parser.getProductURLs(doc, new Category());
            for(String productUrl: productUrls){
                Thread.sleep(300);
                System.out.println("Going to product page:" + productUrl);
                Document productDoc = jsoupUtils.getJsoupDocument(productUrl);
                Product product = parser.parseProductPage(productDoc, new Category());
                System.out.println(product);
            }
            return;
        }
        for(String subcategoryLink: pageMetadata.getSubcategoryNameToUrl().values()){
            //idź głębiej
            Thread.sleep(300);
            System.out.println("getDeeper  "+subcategoryLink);
            getDeeper(jsoupUtils, parser, subcategoryLink);

        }
    }
}
