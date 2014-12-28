package crawler.service;

import crawler.model.Category;
import crawler.model.PageMetadata;
import crawler.model.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by marcin on 10/7/14.
 */
public class AllegroParser implements Parser {

    public static void main(String[] args) {
        AllegroParser p = new AllegroParser();
        JsoupUtils jsoupUtils = new JsoupUtils();
        Document doc = jsoupUtils.getJsoupDocument("http://allegro.pl/dzial/dom-i-zdrowie");
        Category superCategory = new Category();
        superCategory.addSubcategory("main");
        PageMetadata metadata = p.parsePageMetadata(doc, superCategory);
        for (String url : metadata.getSubcategoryNameToUrl().values()) {
            doc = jsoupUtils.getJsoupDocument("http://allegro.pl" + url);
            PageMetadata m2 = p.parsePageMetadata(doc, superCategory);
            for (String url2 : m2.getSubcategoryNameToUrl().values()) {
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
    public Product parseProductPage(Document doc, Category superCategory) {
        double price = -1;
        Product product = new Product();
        Elements costOfDeliveryElements = null;
        Element priceElement = null;
        String subCategory = "";
        String seller = "";
        String location = "";
        String stringPrice = "";
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


        List<Double> listOfCosts = parseDeliveryCost(costOfDeliveryElements);


        try {
            NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
            Number number = format.parse(stringPrice);
            price = number.doubleValue();

        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        product.setProduct(price, listOfCosts, seller, location, superCategory);
        return product;
    }

    private List<Double> parseDeliveryCost(Elements costOfDeliveryElements) {
        List<Double> listOfCosts = new LinkedList<>();
        String costOfDeliveryString;

        for (Element src : costOfDeliveryElements) {
            if (src.attr("class").equals("whiteBg")) {
                costOfDeliveryString = src.text().trim().split(" z")[0];
                NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
                costOfDeliveryString = costOfDeliveryString.replace("od ", "");
                try {
                    Number number = format.parse(costOfDeliveryString);
                    double costOfDelivery = number.doubleValue();
                    listOfCosts.add(costOfDelivery);
                } catch (ParseException e) {
                    throw new UnexpectedParserState("Problem while parsing costOfDelivery: "+costOfDeliveryString);
                }
            }
        }
        return listOfCosts;

    }

    public PageMetadata parsePageMetadata(Document doc, Category superCategory) {

        String nameOfSubcategoryFirstPage;
        PageMetadata pageMetadata = new PageMetadata(superCategory, doc.baseUri(), false);

        Element current = doc.select(".sidebar-cat").select(".current").first();
        if (current != null ) {
            Element nextLink = doc.select(".next a").first();
            if(nextLink != null){
                pageMetadata.setNextPageLink(nextLink.attr("href"));
            }
            pageMetadata.setLowestLevel(true);
            return pageMetadata;
        }


        Elements categories = doc.select(".category-map-list-wrapper .main-category a");
        //lack of main (large font) categories
        if (categories.isEmpty()) {
            categories = doc.select("#sidebar #sidebar-categories .widget-content a");
        }
        if (!categories.isEmpty()) {
            for (Element element : categories) {

                nameOfSubcategoryFirstPage = element.select("span").first().text();

                if (!nameOfSubcategoryFirstPage.isEmpty()) {
                    String subcategoryUrl = element.attr("href").replace("?string=", "");
                    subcategoryUrl = subcategoryUrl.startsWith("http")?subcategoryUrl:"http://allegro.pl/"+subcategoryUrl;
                    pageMetadata.addToSubcategoryNameToUrl(nameOfSubcategoryFirstPage, subcategoryUrl);

                }
            }
            return pageMetadata;
        }


        return null;
    }


    public List<String> getProductURLs(Document doc, Category superCategory) {
        Elements linksElements;
        List<String> listOfUrl = new ArrayList<>();

        linksElements = doc.select("a[href*=html]");
        for (Element element : linksElements) {
            String link = element.attr("href");
            link = link.startsWith("http")?link:"http://allegro.pl/"+link;
            listOfUrl.add(link);
        }

        return listOfUrl;
    }


}
