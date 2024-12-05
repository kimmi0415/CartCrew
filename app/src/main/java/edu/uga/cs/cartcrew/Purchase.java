package edu.uga.cs.cartcrew;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import java.util.Locale;

// POJO class to store information about a purchase
public class Purchase {
    private double price; // the total price of the purchase
    private List<ShoppingItem> items; // the list of items bought
    private String buyer; // the user who bought the items
    private long date; // the date represented as milliseconds since epoch
    private String key; // the key in the database

    /**
     * Default constructor required for calls to DataSnapshot.getValue(ShoppingItem.class)
     */
    public Purchase() { }

    /**
     * Creates a new Purchase object.
     * @param price the total price of the purchase
     * @param items the list of items
     * @param buyer the email address of the user making the purchase
     */
    public Purchase(double price, List<ShoppingItem> items, String buyer) {
        this.price = price;
        this.items = items;
        this.buyer = buyer;
        date = new Date().getTime();
    }

    /**
     * Gets the list of items
     * @return the list of items
     */
    public List<ShoppingItem> getItems() {
        return items;
    }

    /**
     * Gets the total price of the purchase
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the list of items
     * @param items the new list of items
     */
    public void setItems(List<ShoppingItem> items) {
        this.items = items;
    }

    /**
     * Sets the total price
     * @param price the new total price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Converts purchase to a string representation
     * @return string representation of the purchase
     */
    @Override
    public String toString() {
        return buyer + " purchased for " + price + ": " + items;
    }

    /**
     * Gets the email address of the buyer
     * @return the email address
     */
    public String getBuyer() {
        return buyer;
    }

    /**
     * Sets the email address of the buyer
     * @param s the new email address
     */
    public void setBuyer(String s) {
        buyer = s;
    }

    /**
     * Gets the date in milliseconds since epoch
     * @return the date
     */
    public long getDate() {
        return date;
    }

    /**
     * Sets the date
     * @param date the new date
     */
    public void setDate(long date) {
        this.date = date;
    }

    /**
     * Formats the date as a String
     * @return the formatted String
     */
    public String getDateAsString() {
        Date d = new Date(date);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault());
        return sdf.format(d);
    }

    /**
     * Gets the key of the purchase
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the key of the purchase
     * @param s the new key
     */
    public void setKey(String s) {
        key = s;
    }

    /**
     * Formats the price to 2 decimal places
     * @return the formatted price
     */
    public String getPrettyPrice() {
        return String.format("%.2f", price);
    }
}
