package com.service;

import com.model.Category;
import com.model.PageMetadata;
import com.model.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by marcin on 10/7/14.
 */
public class AllegroParser implements Parser{

    public static void main(String[] args) {
        AllegroParser p = new AllegroParser();
        JsoupUtils jsoupUtils = new JsoupUtils();
        Document doc =jsoupUtils.getJsoupDocument("http://allegro.pl/dzial/dom-i-zdrowie");
        Category superCategory=new Category();
        superCategory.addSubcategory("main");
        PageMetadata metadata = p.parsePageMetadata(doc, superCategory);
        for(String url: metadata.getSubcategoryNameToUrl().values()){
            System.out.println(url);
            doc =jsoupUtils.getJsoupDocument("http://allegro.pl"+url);
            PageMetadata m2 = p.parsePageMetadata(doc, superCategory);
            for(String url2: m2.getSubcategoryNameToUrl().values()){
                System.out.println(url2);
            }
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
    public Product parseProductPage(Document doc, Category superCategory){
        double price = -1;
        Product product = new Product();
        List<Double> listOfCosts = new LinkedList<Double>();
        String costOfDeliveryString;
        Elements costOfDeliveryElements = null;
        Element priceElement = null;
        String subCategory = "";
        String seller = "";
        String location = "";
        String stringPrice ="";
        try {
            priceElement = doc.select("strong[data-price]").first();
            subCategory = doc.select("span[itemprop]").first().text();
            superCategory.addSubcategory(subCategory);
            costOfDeliveryElements = doc.select("strong[class]");
            seller = doc.select("dt[data-seller]").text().split(" ")[1];
            location = doc.select("p[class]").text();
            location = location.split(" ")[15];
            stringPrice = priceElement.text().split(" z")[0];
        } catch (NullPointerException e) {
            System.out.print("Strona wygasła lub inne zle rzeczy");
//            e.printStackTrace();
        }


        System.out.println(doc.select("a[href*=/ref/]"));
        try {
            for (Element src : costOfDeliveryElements) {
                if (src.attr("class").equals("whiteBg")) {
                    costOfDeliveryString = src.text().trim().split(" z")[0];

                    NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
                    Number number = format.parse(costOfDeliveryString);
                    double costOfDelivery = number.doubleValue();
                    listOfCosts.add(costOfDelivery);

//                    System.out.print("parseException");


                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        try {
            NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
            Number number = format.parse(stringPrice);
            price = number.doubleValue();

        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        product.setProduct(price, listOfCosts, seller, location, superCategory);


        System.out.println(product.toString());
        return product;
    }

    public PageMetadata parsePageMetadata(Document doc, Category superCategory) {

        String nameOfSubcategoryFirstPage;
        PageMetadata pageMetadata = new PageMetadata(superCategory, doc.baseUri(), false);

        Element current = doc.select(".sidebar-cat").select(".current").first();
        if(current!=null){
            pageMetadata.setLowestLevel(true);
            return pageMetadata;
        }


        Elements categories = doc.select(".category-map-list-wrapper .main-category a");
        //lack of main (large font) categories
        if(categories.isEmpty()){
            categories = doc.select("#sidebar #sidebar-categories .widget-content a");
        }
        if(!categories.isEmpty())
        {
            for(Element element:categories) {

                nameOfSubcategoryFirstPage = element.select("span").first().text();

                if (!nameOfSubcategoryFirstPage.isEmpty()) {
                    String subcategoryUrl = element.attr("href").replace("?string=","");
                    pageMetadata.addToSubcategoryNameToUrl(nameOfSubcategoryFirstPage, subcategoryUrl);

                }
            }
            return pageMetadata;
        }


        return null;
    }


    public List<String> getProductURLs(Document doc, Category superCategory) {
        Elements linksElements;
        String link;
        List<String> listOfUrl = new LinkedList<String>();

        linksElements = doc.select("a[href*=html]");
        for (Element element : linksElements) {
            link = element.attr("href");
            listOfUrl.add(link);
//            System.out.println(link);
        }

        return listOfUrl;
    }
    public void parseListOfProduct(Document doc,Category superCategory) throws ParseException{
        List<String> listOfProductUrl = new LinkedList<String>();
        List<Product> listOfProduct = new LinkedList<Product>();
        Product product = new Product();
        listOfProductUrl = getProductURLs(doc, superCategory);
        Document productDocument;
        try {
            for (String element : listOfProductUrl) {
                productDocument = Jsoup.connect(element).get();
                System.out.println(element);
//                System.out.println(productDocument);
                product = parseProductPage(productDocument, superCategory);
                listOfProduct.add(product);

            }
        }catch (IOException e){
            e.printStackTrace();
        }




    }
}
