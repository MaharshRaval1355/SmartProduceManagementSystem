package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import com.google.firebase.firestore.PropertyName;

public class InventoryItem {
    private String itemName;
    private String stockLevel;
    private String status;

    // Default constructor for Firestore
    public InventoryItem() {
    }

    public InventoryItem(String itemName, String stockLevel, String status) {
        this.itemName = itemName;
        this.stockLevel = stockLevel;
        this.status = status;
    }

    @PropertyName("itemName")
    public String getItemName() {
        return itemName;
    }

    @PropertyName("itemName")
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @PropertyName("stockLevel")
    public String getStockLevel() {
        return stockLevel;
    }

    @PropertyName("stockLevel")
    public void setStockLevel(String stockLevel) {
        this.stockLevel = stockLevel;
    }

    @PropertyName("status")
    public String getStatus() {
        return status;
    }

    @PropertyName("status")
    public void setStatus(String status) {
        this.status = status;
    }
}
