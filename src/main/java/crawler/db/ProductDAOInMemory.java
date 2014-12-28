package crawler.db;

import crawler.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDAOInMemory implements ProductDAO {

    Logger logger = LoggerFactory.getLogger(ProductDAOInMemory.class);
    Map<String,List<Product>> products = new HashMap<>();

    @Override
    public synchronized void saveProduct(Product product, String type) {
        List<Product> productsList = products.get(type);
        if(productsList==null){
            productsList = new ArrayList<>();
            products.put(type, productsList);
        }
        productsList.add(product);
        logger.info("products[{}] size: {}", type, productsList.size());
    }
}
