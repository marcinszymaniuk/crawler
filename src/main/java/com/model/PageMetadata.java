package com.model;


import java.util.HashMap;
import java.util.Map;

public class PageMetadata {
    String selfUrl;
    Category category;
    Map<String, String> subcategoryNameToUrl;
    boolean isLowestLevel;

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
