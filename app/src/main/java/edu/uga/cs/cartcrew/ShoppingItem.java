package edu.uga.cs.cartcrew;

public class ShoppingItem {
    private String name;
    private String quantity;

    // Default constructor required for calls to DataSnapshot.getValue(ShoppingItem.class)
    public ShoppingItem() { }

    // Constructor with name and quantity
    public ShoppingItem(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    // Getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter for quantity
    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}