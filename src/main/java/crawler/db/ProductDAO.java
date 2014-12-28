package crawler.db;


import crawler.model.Product;

public interface ProductDAO {
    void saveProduct(Product product, String type);
}
