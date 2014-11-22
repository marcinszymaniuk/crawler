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

/**
 * Created by marcin on 10/7/14.
 */
public class Parser {

    public Document getJsoupDocument() {

        File input = new File(getClass().getResource ("/kostki.html").getFile());

        String costOfDeliveryString = null;
        try {
            /*
             * It's hard to understand here whether you pull documents from allegro or take it from disk
             */
            Document doc = Jsoup.parse(input,
                    "UTF-8","http://allegro.pl/wyposazenie-123?ref=simplified-category-tree");

            return doc;

        } catch (IOException e) {
            /*
            We need to handle it a bit better. For now I just leave the println and return null.
             */
            e.printStackTrace();
            return null;
        }



    }

    /*
     * This method parses JSOUP document so we can deal with documents regardless of how they were created
     * (i.e. by pulling website through http or using input file). Doing it this way let us reuse this code and
     * makes testing way easier.
     *
     * For now this returns just price. We should create a class describing product so we can return an instance of it.
     *
     */
    public Product parseProductPage(Document doc, Category superCategory) {
        double price = -1;
        Product product = new Product();
        List<Double> listOfCosts = new LinkedList<Double>();
        String costOfDeliveryString;
        Element priceElement = doc.select("strong[data-price]").first();
        String subCategory = doc.select("span[itemprop]").first().text();
        superCategory.addSubcategory(subCategory);
        Elements costOfDeliveryElements = doc.select("strong[class]");
        String seller = doc.select("dt[data-seller]").text().split(" ")[1];
        String location = doc.select("p[class]").text();
        location = location.split(" ")[15];

//        System.out.println("lokalizacja " + location );


        System.out.println(doc.select("a[href*=/ref/]"));
        for(Element src:costOfDeliveryElements){
            if(src.attr("class").equals("whiteBg")){
                costOfDeliveryString = src.text().trim().split(" z")[0];
                try {
                    NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
                    Number number = format.parse(costOfDeliveryString);
                    double costOfDelivery = number.doubleValue();
                    listOfCosts.add(costOfDelivery);
                }catch (ParseException pe){
                    System.out.print("parseException");
                }

            }
        }
        String stringPrice = priceElement.text().split(" z")[0];

        try {
            NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
            Number number = format.parse(stringPrice);
            price = number.doubleValue();

        }catch (ParseException pe){
            pe.printStackTrace();
        }

        product.setProduct(price, listOfCosts, seller, location, superCategory);


        System.out.println(product.toString());
        return product;
    }

    public PageMetadata parsePageMetadata(Document doc,Category superCategory) {

        Elements subcategoryElements;
        Elements categoryElements;

        String subcategoryUrl = " ";
        String nameOfSubcategory;
        String nameOfSubcategoryFirstPage;
        String categoryString;

        Category category = new Category();
        PageMetadata pageMetadata = new PageMetadata();

        subcategoryElements= doc.select("a[href*=simplified]").not("[class]");

        categoryElements = doc.select("li[class^=main-breadcrumb]");

        for(Element element:categoryElements){
            System.out.println(element.child(0).select("span").text());
            categoryString = element.child(0).select("span").text();
            category.addSubcategory(categoryString);
        }

        for(Element element:subcategoryElements) {

            nameOfSubcategoryFirstPage = element.select("span").text();
            nameOfSubcategory = element.select("span[class*=name]").text();

            if (!nameOfSubcategory.equals("") || !nameOfSubcategoryFirstPage.equals("")) {
                subcategoryUrl = element.attr("href");

                if (!nameOfSubcategory.equals("")) {
                    pageMetadata.addToSubcategoryNameToUrl(nameOfSubcategory, subcategoryUrl);
                } else {
                    pageMetadata.addToSubcategoryNameToUrl(nameOfSubcategoryFirstPage, subcategoryUrl);
                    //ustawic flage o tym czy jest ostatnią kategorą
                }
            }
        }
        System.out.println(category.toString());
        System.out.println(pageMetadata.getSubcategoryNameToUrl());
        return pageMetadata;
    }


    public List<String> getProductURLs(Document doc, Category superCategory) {
        return null;
    }
}
