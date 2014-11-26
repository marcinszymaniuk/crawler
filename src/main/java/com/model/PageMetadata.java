package com.model;


import java.util.HashMap;
import java.util.Map;

public class PageMetadata {
    String selfUrl;
    Category category;
    Map<String, String> subcategoryNameToUrl;
    boolean isLowestLevel;

    public PageMetadata(){
        subcategoryNameToUrl = new HashMap<String, String>();
    }

    public void addToSubcategoryNameToUrl(String nameOfSubcategory, String subcategoryUrl) {
        subcategoryNameToUrl.put(nameOfSubcategory, subcategoryUrl);
    }
    public Map<String,String> getSubcategoryNameToUrl(){
        return subcategoryNameToUrl;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isLowestLevel() {
        return isLowestLevel;
    }
}
