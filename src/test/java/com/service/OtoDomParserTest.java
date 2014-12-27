package com.service;

import com.model.Category;
import com.model.Product;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class OtoDomParserTest {

    @Test
    public void testAgentParseProductPage() throws Exception {
        JsoupUtils jsoupUtils = new JsoupUtils();

        OtoDomParser parser= new OtoDomParser();

        File otodomMain = new File(this.getClass().getResource("/otodom/product/agent/mieszkanie.html").getFile());
        Document otodomMainDoc = jsoupUtils.getJsoupDocument(otodomMain);
        Product product = parser.parseProductPage(otodomMainDoc, new Category());

        assertEquals(87000, product.getPrice(), 0);
        assertEquals("http://otodom.pl/biuro-nieruchomosci/caruknieruchomosci-id717669/ Sławek Caruk", product.getSeller());
        assertEquals("Kamień Pomorski, Długosza, pow. kamieński", product.getLocation());
    }
    @Test
    public void testDirectParseProductPage() throws Exception {
        JsoupUtils jsoupUtils = new JsoupUtils();

        OtoDomParser parser= new OtoDomParser();

        File otodomMain = new File(this.getClass().getResource("/otodom/product/direct/bezposrednio.html").getFile());
        Document otodomMainDoc = jsoupUtils.getJsoupDocument(otodomMain);
        Product product = parser.parseProductPage(otodomMainDoc, new Category());

        assertEquals(969000, product.getPrice(), 0);
        assertEquals("Jacek Piotrowicz", product.getSeller());
        assertEquals("Warszawa, Mokotów, Sobieskiego 100", product.getLocation());
    }

    @Test
    public void testParsePageMetadata() throws Exception {

    }

    @Test
    public void testGetProductURLs() throws Exception {
        JsoupUtils jsoupUtils = new JsoupUtils();

        OtoDomParser parser= new OtoDomParser();

        File otodomMain = new File(this.getClass().getResource("/otodom/main_search/main.html").getFile());
        Document otodomMainDoc = jsoupUtils.getJsoupDocument(otodomMain);
        List<String> otodomHousesUrls = parser.getProductURLs(otodomMainDoc, new Category());

        assertTrue(otodomHousesUrls.contains("http://otodom.pl/mieszkanie-tarnow-ul-bitwy-pod-studziankami-id33274763.html"));
        assertEquals(25, otodomHousesUrls.size());
    }

    @Test
    public void testParseListOfProduct() throws Exception {

    }
}