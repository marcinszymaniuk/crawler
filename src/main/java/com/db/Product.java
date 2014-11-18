package com.db;

import com.model.Category;

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
    private Category category;

    public Product(){
        listOfCost = new LinkedList<Double>();
    }

    public void setProduct(double price, List<Double> listOfCost, String seller,
                           String location ,Category category){
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

    public double getPrice() {
        return price;
    }

    public List<Double> getDeliveryCost() {
        return listOfCost;
    }

    public String getSeller() {
        return seller;
    }

    public String getLocation() {
        return location;
    }

    public Category getCategory() {
        return category;
    }
}
