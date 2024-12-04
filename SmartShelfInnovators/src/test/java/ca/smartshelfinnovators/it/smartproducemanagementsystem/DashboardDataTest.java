package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DashboardDataTest {

    private DashboardData dashboardData;

    @Before
    public void setUp() {
        // Initialize the object for testing
        dashboardData = new DashboardData("Hello, User!",
                "No alerts at this time.",
                "Stock levels are sufficient.",
                "All sensors are operational.");
    }

    @Test
    public void testDefaultConstructor() {
        // Test default constructor
        DashboardData defaultDashboardData = new DashboardData();
        assertNull(defaultDashboardData.getGreeting());
        assertNull(defaultDashboardData.getAlertMessages());
        assertNull(defaultDashboardData.getStockMessages());
        assertNull(defaultDashboardData.getSensorMessages());
    }

    @Test
    public void testParameterizedConstructor() {
        // Test parameterized constructor
        assertEquals("Hello, User!", dashboardData.getGreeting());
        assertEquals("No alerts at this time.", dashboardData.getAlertMessages());
        assertEquals("Stock levels are sufficient.", dashboardData.getStockMessages());
        assertEquals("All sensors are operational.", dashboardData.getSensorMessages());
    }

    @Test
    public void testSetAndGetGreeting() {
        // Test setting and getting greeting
        dashboardData.setGreeting("Welcome to the Dashboard!");
        assertEquals("Welcome to the Dashboard!", dashboardData.getGreeting());
    }

    @Test
    public void testSetAndGetAlertMessages() {
        // Test setting and getting alert messages
        dashboardData.setAlertMessages("New alert: Check the system!");
        assertEquals("New alert: Check the system!", dashboardData.getAlertMessages());
    }

    @Test
    public void testSetAndGetStockMessages() {
        // Test setting and getting stock messages
        dashboardData.setStockMessages("Warning: Stock levels are low.");
        assertEquals("Warning: Stock levels are low.", dashboardData.getStockMessages());
    }

    @Test
    public void testSetAndGetSensorMessages() {
        // Test setting and getting sensor messages
        dashboardData.setSensorMessages("Sensor 3 is not responding.");
        assertEquals("Sensor 3 is not responding.", dashboardData.getSensorMessages());
    }
}
