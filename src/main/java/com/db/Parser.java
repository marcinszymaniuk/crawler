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

    File input = new File("///home/marcin/RAFAŁ/NICE/theColdestPlace/OPEL.html");
    public void parseFromFile() {

        double price = 0 ;
        String costOfDeliveryString = null;
        try {

            List<Double> listOfCosts = new LinkedList<Double>();

            Document doc = Jsoup.parse(input, "UTF-8","http://allegro.pl/dobre-struny-do-gitary-akustycznej-10-47-3x-kostka-i4639566521.html");

            Element link = doc.select("strong[data-price]").first();
            Element link1 = doc.select("span[itemprop]").first();
            Elements link2 = doc.select("strong[class]");

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
                System.out.print("parseException");
            }


            String category = link1.text();

            System.out.println("Cena podstawowa: "+price+ " zł. " +"kategoria: "+
                    category + " " +"koszty dostawy:" + listOfCosts.toString());

        } catch (IOException e) {
            System.out.println("IOException");

        }



    }

}
