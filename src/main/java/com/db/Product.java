package com.db;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by marcin on 11/5/14.
 */
public class Product {
    private double price;
    private List<Double> listOfCost;
    private String seller;
    private String location;
    private String category;

    public Product(){
        listOfCost = new LinkedList<Double>();
    }

    public void setProduct(double price, List<Double> listOfCost, String seller,
                           String location ,String category){
        this.price = price;
        this.listOfCost = listOfCost;
        this.seller = seller;
        this.location = location;
        this.category = category;

    }
    public String toString(){
//        System.out.println("Cena podstawowa" + price + " koszty dostawy : " + listOfCost.toString()
//            + "sprzedawca : "+ seller + "lokalizacja : " + location);
        String text ="Cena podstawowa : " + price + " koszty dostawy : " + listOfCost.toString()
                + " sprzedawca : "+ seller + " lokalizacja : " + location + " Kategoria : " + category;
        return text;
    }
    public Product getProduct(){
        return this;
    }
}
