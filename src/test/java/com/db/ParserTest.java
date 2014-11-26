package com.db;

import com.model.Category;
import com.model.PageMetadata;
import com.service.Parser;
import com.model.Product;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


public class ParserTest {

    @Test
    public void testParseFromFile() throws Exception {
        Parser p = new Parser();
        Document doc = p.getJsoupDocument(new File(p.getClass().getResource("/kostki.html").getFile()));
        Category superCategory = new Category();

        superCategory.addSubcategory("Kultura i rozrywka");

        Product product = p.parseProductPage(doc, superCategory);

        assertEquals(9.9, product.getPrice());
        assertEquals("Kultura i rozrywka", product.getCategory().get(0));
        assertEquals("Instrumenty", product.getCategory().get(1));
        ArrayList<Double> expectedDeliveryCost = new ArrayList<>();
        expectedDeliveryCost.add(5.9);
        expectedDeliveryCost.add(6.9);
        expectedDeliveryCost.add(12.9);
        assertEquals(expectedDeliveryCost, product.getDeliveryCost());
        assertEquals("Nd_Instrumenty", product.getSeller());
        assertEquals("Wrocław", product.getLocation());
    }

    @Test
    public void testParsePageMetadata() throws Exception {
        Parser p = new Parser();
        Document doc = p.getJsoupDocument(new File(p.getClass().getResource("/wyposazenie.html").getFile()));
        Category superCategory = new Category();

        superCategory.addSubcategory("Wyposażenie");

        PageMetadata pageMetadata = p.parsePageMetadata(doc, superCategory);
        assertEquals("http://allegro.pl/wyposazenie-szkatulki-i-kuferki-110196?ref=simplified-category-tree",
                pageMetadata.getSubcategoryNameToUrl().get("Szkatułki i kuferki"));
        assertEquals(24, pageMetadata.getSubcategoryNameToUrl().size());
        assertTrue(pageMetadata.isLowestLevel());
    }
}