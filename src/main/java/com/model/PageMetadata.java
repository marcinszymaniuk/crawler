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
    public void get(String selfUrl){
        this.selfUrl = selfUrl;
    }
    public void setCategory(){

    }
    public void addToSubcategoryNameToUrl(String subcategoryUrl,String nameOfSubcategory) {
        subcategoryNameToUrl.put(subcategoryUrl, nameOfSubcategory);
    }
    public Map<String,String> getSubcategoryNameToUrl(){
        return subcategoryNameToUrl;
    }


}
