package com.db;

import com.model.Category;
import com.service.Parser;
import com.model.Product;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;


public class ParserTest {

    @Test
    public void testParseFromFile() throws Exception {
        Parser p = new Parser();
        Document doc = p.getJsoupDocument();
        Category superCategory = new Category();

        superCategory.addSubcategory("Kultura i rozrywka");

//        Product product = p.parseProductPage(doc, superCategory);
//
//        assertEquals(9.9, product.getPrice());
//        assertEquals("Kultura i rozrywka", product.getCategory().get(0));
//        assertEquals("Instrumenty", product.getCategory().get(1));
//        ArrayList<Double> expectedDeliveryCost = new ArrayList<>();
//        expectedDeliveryCost.add(5.9);
//        expectedDeliveryCost.add(6.9);
//        expectedDeliveryCost.add(12.9);
//        assertEquals(expectedDeliveryCost, product.getDeliveryCost());
//        assertEquals("Nd_Instrumenty", product.getSeller());
//        assertEquals("Wrocław", product.getLocation());

//        p.parsePageMetadata(doc, superCategory);
//        p.getProductURLs(doc,superCategory);
        p.parseListOfProduct(doc,superCategory);

    }
}