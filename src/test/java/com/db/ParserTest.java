package com.db;

import org.jsoup.nodes.Document;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;


public class ParserTest {

    @Test
    public void testParseFromFile() throws Exception {
        Parser p = new Parser();
        Document doc = p.getJsoupDocument();
        double price = p.parseDocument(doc);


        assertEquals(9.9, price);
    }
}