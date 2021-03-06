package crawler.model;


import java.util.HashMap;
import java.util.Map;

public class PageMetadata {
    String selfUrl;
    Category category;
    Map<String, String> subcategoryNameToUrl;
    boolean isLowestLevel;
    String nextPageLink;

    public PageMetadata() {
        subcategoryNameToUrl = new HashMap<String, String>();
    }

    public PageMetadata(Category category, String selfUrl, boolean isLowestLevel) {
        this.category = category;
        this.selfUrl = selfUrl;
        this.isLowestLevel = isLowestLevel;
        subcategoryNameToUrl = new HashMap<String, String>();
    }

    public void addToSubcategoryNameToUrl(String nameOfSubcategory, String subcategoryUrl) {
        subcategoryNameToUrl.put(nameOfSubcategory, subcategoryUrl);
    }

    public Map<String, String> getSubcategoryNameToUrl() {
        return subcategoryNameToUrl;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isLowestLevel() {
        return isLowestLevel;
    }

    public void setLowestLevel(boolean isLowestLevel) {
        this.isLowestLevel = isLowestLevel;
    }

    public String getNextPageLink() {
        return nextPageLink;
    }

    public void setNextPageLink(String nextPageLink) {
        this.nextPageLink = nextPageLink;
    }

    @Override
    public String toString() {
        return "PageMetadata{" +
                "selfUrl='" + selfUrl + '\'' +
                ", category=" + category +
                ", subcategoryNameToUrl=" + subcategoryNameToUrl +
                ", isLowestLevel=" + isLowestLevel +
                '}';
    }
}
