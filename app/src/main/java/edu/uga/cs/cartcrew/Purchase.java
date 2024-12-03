package edu.uga.cs.cartcrew;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import java.util.Locale;

public class Purchase {
    private double price;
    public List<ShoppingItem> items;
    private String buyer;
    private long date;

    // Default constructor required for calls to DataSnapshot.getValue(ShoppingItem.class)
    public Purchase() { }

    public Purchase(double price, List<ShoppingItem> items, String buyer) {
        this.price = price;
        this.items = items;
        this.buyer = buyer;
        date = new Date().getTime();
    }

    public List<ShoppingItem> getItems() {
        return items;
    }

    public double getPrice() {
        return price;
    }

    public void setItems(List<ShoppingItem> items) {
        this.items = items;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String toString() {
        return buyer + " purchased for " + price + ": " + items;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String s) {
        buyer = s;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDateAsString() {
        Date d = new Date(date);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault());
        return sdf.format(d);
    }
}
