package crawler.service;

import crawler.model.Category;
import crawler.model.PageMetadata;
import crawler.model.Product;
import org.jsoup.nodes.Document;

import java.util.List;

public interface Parser {
    public Product parseProductPage(Document doc, Category superCategory);
    public PageMetadata parsePageMetadata(Document doc,Category superCategory);
    public List<String> getProductURLs(Document doc, Category superCategory);

}
