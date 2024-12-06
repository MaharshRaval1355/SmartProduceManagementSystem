package ca.smartshelfinnovators.it.smartproducemanagementsystem;

public class DashboardData {
    private String greeting;
    private String alertMessages;
    private String stockMessages;
    private String sensorMessages;

    // Default constructor required for Firestore
    public DashboardData() {}

    public DashboardData(String greeting, String alertMessages, String stockMessages, String sensorMessages) {
        this.greeting = greeting;
        this.alertMessages = alertMessages;
        this.stockMessages = stockMessages;
        this.sensorMessages = sensorMessages;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public String getAlertMessages() {
        return alertMessages;
    }

    public void setAlertMessages(String alertMessages) {
        this.alertMessages = alertMessages;
    }

    public String getStockMessages() {
        return stockMessages;
    }

    public void setStockMessages(String stockMessages) {
        this.stockMessages = stockMessages;
    }

    public String getSensorMessages() {
        return sensorMessages;
    }

    public void setSensorMessages(String sensorMessages) {
        this.sensorMessages = sensorMessages;
    }
}


