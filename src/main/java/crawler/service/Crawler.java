package crawler.service;

import crawler.db.ProductDAO;
import crawler.db.ProductDAOInMemory;
import crawler.model.Category;
import crawler.model.PageMetadata;
import crawler.model.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by marcin on 27/12/14.
 */
public class Crawler {

    Logger logger = LoggerFactory.getLogger(Crawler.class);

    private final Parser parser;
    private ProductDAO productDAO;
    private String name;
    JsoupUtils jsoupUtils = new JsoupUtils();

    public Crawler(ProductDAO productDAO, Parser parser, String name) {
        this.productDAO = productDAO;
        this.parser = parser;
        this.name = name;
    }

    public static void main(String[] args) throws InterruptedException {
        ProductDAOInMemory productDAO = new ProductDAOInMemory();
        final Crawler allegroCrawler = new Crawler(productDAO, new AllegroParser(), "allegro");
        final Crawler otoDomCrawler = new Crawler(productDAO, new OtoDomParser(), "otodom");

        Runnable runnable1 = new Runnable() {
            public void run() {
                try {
                    allegroCrawler.getDeeper("http://allegro.pl/dzial/dom-i-zdrowie");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };

        Runnable runnable2 = new Runnable() {
            public void run() {
                try {
                    otoDomCrawler.getDeeper("http://otodom.pl/index.php?mod=listing&resultsPerPage=25&objSearchQuery.Orderby=&objSearchQuery.ObjectName=Flat&objSearchQuery.OfferType=sell&currentPage=4");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        for(int i=0; i<2;i++){
            Thread t = new Thread(runnable1);
            t.start();
            Thread t2 = new Thread(runnable2);
            t2.start();

        }

    }

    private void getDeeper(String url) throws InterruptedException {
        logger.info("getDepper {}", name);
        Document doc = jsoupUtils.getJsoupDocument(url);
        PageMetadata pageMetadata = parser.parsePageMetadata(doc, new Category());
        try {
            if(pageMetadata==null){
                logger.info("pageMetadata is null {}", url);
                return;
            }
            if (pageMetadata.isLowestLevel()) {
                //Pobierz produkty
                getProducts(url);
                return;
            }

            for (String subcategoryLink : pageMetadata.getSubcategoryNameToUrl().values()) {
                //idź głębiej
//                Thread.sleep(150);
                getDeeper(subcategoryLink);

            }
        } catch (UnexpectedParserState e) {
            e.printStackTrace();
        }
    }

    private void getProducts(String url) {
        PageMetadata pageMetadata;
        String nextUrl = url;
        Document doc;

        do {
            logger.info("Iterating through product page {}", nextUrl);
            doc = jsoupUtils.getJsoupDocument(nextUrl);
            pageMetadata = parser.parsePageMetadata(doc, new Category());
            nextUrl = pageMetadata.getNextPageLink();
            List<String> productUrls = parser.getProductURLs(doc, new Category());
            for (String productUrl : productUrls) {
//                    Thread.sleep(150);
                logger.info("Going to product page: {} {}", name, productUrl);
                Document productDoc = jsoupUtils.getJsoupDocument(productUrl);
                Product product = parser.parseProductPage(productDoc, new Category());
                logger.info("Product[{}]: {}", name, product);
                productDAO.saveProduct(product, name);
            }
        }while(nextUrl != null);

    }
}
