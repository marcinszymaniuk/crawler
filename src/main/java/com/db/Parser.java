package com.db;

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

        File input = new File(getClass().getResource("/kostki.html").getFile());
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
    public double parseDocument(Document doc) {
        double price = -1;
        List<Double> listOfCosts = new LinkedList<Double>();
        String costOfDeliveryString;
        Element link = doc.select("strong[data-price]").first();
        Element link1 = doc.select("span[itemprop]").first();
        Elements link2 = doc.select("strong[class]");
               /*Throw useless comments away before commit. (Do the same with my comments as well) */
//            System.out.print(link2);
//            System.out.println(link2.attr("class"));

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
        String stringPrice = link.text().split(" z")[0];

        System.out.println(stringPrice);


        try {
            NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
            Number number = format.parse(stringPrice);
            price = number.doubleValue();

        }catch (ParseException pe){
            pe.printStackTrace();
        }


        String category = link1.text();

        System.out.println("Cena podstawowa: "+price+ " zł. " +"kategoria: "+
                category + " " +"koszty dostawy:" + listOfCosts.toString());
        return price;
    }


}
