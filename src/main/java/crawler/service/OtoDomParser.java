package crawler.service;

import crawler.model.Category;
import crawler.model.PageMetadata;
import crawler.model.Product;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by marcin on 10/7/14.
 */
public class OtoDomParser implements Parser {

    public static void main(String[] args) throws InterruptedException {
        OtoDomParser p = new OtoDomParser();
        JsoupUtils jsoupUtils = new JsoupUtils();
        Document doc = jsoupUtils.getJsoupDocument("http://otodom.pl/index.php?mod=listing&act=&source=main&Search=Search&objSearchQuery.ObjectName=Flat&objSearchQuery.OfferType=sell&Location=&objSearchQuery.Distance=0&objSearchQuery.LatFrom=&objSearchQuery.LatTo=&objSearchQuery.LngFrom=&objSearchQuery.LngTo=&objSearchQuery.PriceFrom=&objSearchQuery.PriceTo=&objSearchQuery.AreaFrom=&objSearchQuery.AreaTo=&objSearchQuery.FlatRoomsNumFrom=&objSearchQuery.FlatRoomsNumTo=&objSearchQuery.MarketType=&objSearchQuery.PriceM2From=&objSearchQuery.PriceM2To=&objSearchQuery.PriceM2Currency.ID=1&objSearchQuery.FlatFloorFrom=&objSearchQuery.FlatFloorTo=&objSearchQuery.FlatFloorsNoFrom=&objSearchQuery.FlatFloorsNoTo=&objSearchQuery.FlatBuildingType=&objSearchQuery.BuildingMaterial=&objSearchQuery.BuildingYearFrom=&objSearchQuery.BuildingYearTo=&objSearchQuery.CreationDate=&objSearchQuery.Description=&objSearchQuery.offerId=");
        List<String> urls = p.getProductURLs(doc, new Category());
        System.out.println(urls);
        for(String url: urls){
            doc = jsoupUtils.getJsoupDocument("http://otodom.pl"+url);
            Product product = p.parseProductPage(doc, new Category());
            System.out.println(product.toString());
            Thread.sleep(1000);
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
        Product product = new Product();

        double price = calculatePrice(doc);
        String location = calculateLocation(doc);
        String seller = calculateSeller(doc);

        product.setProduct(price, null, seller, location, superCategory);
        return product;
    }

    public PageMetadata parsePageMetadata(Document doc, Category superCategory) {
        PageMetadata pageMetadata = new PageMetadata(new Category(), "", true);
        Element nextLink = doc.select(".od-pagination_next").first();
        if(nextLink != null){
            String link = nextLink.attr("href");
            link = link.startsWith("http")?link:"http://otodom.pl"+link;
            pageMetadata.setNextPageLink(link);

        }
        return pageMetadata;

    }


    public List<String> getProductURLs(Document doc, Category superCategory) {
        Elements linksElements;
        List<String> listOfUrl = new LinkedList<String>();
        linksElements = doc.select(".od-listing_item .od-listing_item-title a");
        for (Element element : linksElements) {
            String link = element.attr("href");
            link = link.startsWith("http")?link:"http://otodom.pl"+link;
            listOfUrl.add(link);
        }

        return listOfUrl;
    }

    public void parseListOfProduct(Document doc, Category superCategory) throws ParseException {
        List<String> listOfProductUrl = new LinkedList<String>();
        List<Product> listOfProduct = new LinkedList<Product>();
        Product product = new Product();
        listOfProductUrl = getProductURLs(doc, superCategory);
        Document productDocument;
        try {
            for (String element : listOfProductUrl) {
                productDocument = Jsoup.connect(element).get();
                product = parseProductPage(productDocument, superCategory);
                listOfProduct.add(product);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String calculateLocation(Document doc) {
        Element mapBoxLink = doc.select("p.map-box__location").first();
        return mapBoxLink.text();

    }

    private String calculateSeller(Document doc) {
        Element agentLink = doc.select("a.agent-link").first();
        Element ownerElement = doc.select(".contact-list__person").first();
        if(agentLink!=null){
            //TODO construct more descriptive seller object
            return agentLink.attr("href") + " " + agentLink.text();
        }
        if(ownerElement!=null) {
            return ownerElement.text();
        }
        throw new UnexpectedParserState("Couldn't find seller related elements.", doc);
    }

    private double calculatePrice(Document doc) {
        Elements offer = doc.select("div #offer");
        double price;
        HashMap<String, String> adOnAttrs = calculateAdOnParams(offer);
        if (StringUtils.equals(adOnAttrs.get("PriceFrom"), adOnAttrs.get("PriceTo"))) {
            price = Double.parseDouble(adOnAttrs.get("PriceFrom"));
        } else {
            throw new UnexpectedParserState(String.format("Expected priceFrom and priceTo to be equal. " +
                    "Got priceFrom=%s, priceTo=%s", adOnAttrs.get("PriceFrom"), adOnAttrs.get("PriceTo")));
        }
        return price;
    }

    private HashMap<String, String> calculateAdOnParams(Elements offer) {
        String attrDataAdo = offer.attr("data-ado-ct");
        String[] attrs = attrDataAdo.split("_");

        HashMap<String, String> adOAttrs = new HashMap<>();
        for (String attr : attrs) {
            String[] keyVal = attr.split("=");
            adOAttrs.put(keyVal[0], keyVal[1]);
        }
        return adOAttrs;
    }


}
