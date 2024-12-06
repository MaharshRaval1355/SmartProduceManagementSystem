package ca.smartshelfinnovators.it.smartproducemanagementsystem;

public class InventoryItem {
    private String itemName;
    private String stockLevel;
    private String status;

    public InventoryItem(String itemName, String stockLevel, String status) {
        this.itemName = itemName;
        this.stockLevel = stockLevel;
        this.status = status;
    }

    public String getItemName() {
        return itemName;
    }

    public String getStockLevel() {
        return stockLevel;
    }

    public String getStatus() {
        return status;
    }

    // Setter Methods
    public void setStockLevel(String stockLevel) {
        this.stockLevel = stockLevel;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

