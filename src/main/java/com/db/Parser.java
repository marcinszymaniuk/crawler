package com.db;

import com.model.Category;
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
                    "UTF-8","http://allegro.pl/dobre-struny-do-gitary-akustycznej-10-47-3x-kostka-i4639566521.html");

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
    public Product parseDocument(Document doc, Category superCategory) {
        double price = -1;
        Product product = new Product();
        List<Double> listOfCosts = new LinkedList<Double>();
        String costOfDeliveryString;
        Element priceElement = doc.select("strong[data-price]").first();
        String subCategory = doc.select("span[itemprop]").first().text();
        superCategory.addSubcategory(subCategory);
        Elements link2 = doc.select("strong[class]");
        String seller = doc.select("dt[data-seller]").text().split(" ")[1];
        //to jest text, który jest pomiedzy tagami p,
        //niestety jest kilka wyrazow, ktore mają te same znaczniki i ciezko je troche rozdzielic zeby znalesc lokalizacje
        String location = doc.select("p[class]").text();
        //moze byc tak i to niby pokazuje to co chcemy, ale mala zmiana htmla moze to zepsuc
        location = location.split(" ")[15];
        System.out.println("lokalizacja " + location );

        for(Element src:link2){
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


}
