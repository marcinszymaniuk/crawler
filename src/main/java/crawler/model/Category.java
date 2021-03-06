package crawler.model;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private List<String> categories;

    public Category() {
        this.categories = new ArrayList<String>();
    }

    public void addSubcategory(String category){
        categories.add(category);
    }

    public String get(int i){
         return categories.get(i);
    }

    public List<String> getCategories(){
        return categories;
    }
    @Override
    public String toString() {
        return "Category{" +
                "categories=" + categories +
                '}';
    }
}
