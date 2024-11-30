package edu.uga.cs.cartcrew;

public class ShoppingItem {
    private String name;
    private int quantity;

    public ShoppingItem() {
        // Default constructor required for calls to DataSnapshot.getValue()
    }

    public ShoppingItem(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
