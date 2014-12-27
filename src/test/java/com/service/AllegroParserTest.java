package com.service;

import com.model.Category;
import com.model.PageMetadata;
import com.service.AllegroParser;
import com.service.JsoupUtils;
import com.model.Product;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;


public class AllegroParserTest {

    @Test
    public void testParseProductPage() throws Exception {
        JsoupUtils jsoupUtils = new JsoupUtils();
        Document doc = jsoupUtils.getJsoupDocument(new File(this.getClass().getResource("/allegro/kostki.html").getFile()));
        Category superCategory = new Category();

        superCategory.addSubcategory("Kultura i rozrywka");

        Product product = new AllegroParser().parseProductPage(doc, superCategory);

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
    public void testParsePageMetadataMainCategories() throws Exception {
        JsoupUtils jsoupUtils = new JsoupUtils();
        Document doc = jsoupUtils.getJsoupDocument(new File(this.getClass().
                getResource("/allegro/dziecko/dziecko.html").getFile()));
        Category superCategory = new Category();

        PageMetadata pageMetadata = new AllegroParser().parsePageMetadata(doc, superCategory);
        assertEquals("http://allegro.pl/zabawki-11818?ref=simplified-category-tree",
                pageMetadata.getSubcategoryNameToUrl().get("Zabawki"));
        assertEquals(8, pageMetadata.getSubcategoryNameToUrl().size());
        assertFalse(pageMetadata.isLowestLevel());
    }

    @Test
    public void testParsePageMetadataSubcategories() throws Exception {
        JsoupUtils jsoupUtils = new JsoupUtils();
        Document doc = jsoupUtils.getJsoupDocument(new File(this.getClass().
                getResource("/allegro/zabawki/zabawki.html").getFile()));
        Category superCategory = new Category();
        PageMetadata pageMetadata = new AllegroParser().parsePageMetadata(doc, superCategory);
        assertEquals("http://allegro.pl/zabawki-klocki-11823?ref=simplified-category-tree",
                pageMetadata.getSubcategoryNameToUrl().get("Klocki"));
        assertEquals(19, pageMetadata.getSubcategoryNameToUrl().size());
        assertFalse(pageMetadata.isLowestLevel());
    }
}