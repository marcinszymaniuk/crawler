package com.service;

import com.model.Category;
import com.model.PageMetadata;
import com.model.Product;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public interface Parser {
    public Product parseProductPage(Document doc, Category superCategory);
    public PageMetadata parsePageMetadata(Document doc,Category superCategory);
    public List<String> getProductURLs(Document doc, Category superCategory);
}
