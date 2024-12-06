package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class InventoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private InventoryAdapter inventoryAdapter;
    private List<InventoryItem> inventoryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_inventory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Populate Inventory List with sample data
        inventoryList = new ArrayList<>();

// Deli
        inventoryList.add(new InventoryItem("Chicken", "10 kg", "Low"));
        inventoryList.add(new InventoryItem("Turkey", "15 kg", "Optimal"));
        inventoryList.add(new InventoryItem("Ham", "5 kg", "Low"));

// Bakery
        inventoryList.add(new InventoryItem("Bread", "20 units", "Optimal"));
        inventoryList.add(new InventoryItem("Cake", "8 units", "High"));
        inventoryList.add(new InventoryItem("Croissant", "15 units", "Optimal"));

// Drinks
        inventoryList.add(new InventoryItem("Milk", "5 liters", "Low"));
        inventoryList.add(new InventoryItem("Juice", "10 liters", "Optimal"));
        inventoryList.add(new InventoryItem("Soda", "25 cans", "High"));

// Produce
        inventoryList.add(new InventoryItem("Apples", "15 kg", "High"));
        inventoryList.add(new InventoryItem("Bananas", "12 kg", "Optimal"));
        inventoryList.add(new InventoryItem("Carrots", "8 kg", "Low"));

// Ice Cream
        inventoryList.add(new InventoryItem("Vanilla Ice Cream", "20 tubs", "Optimal"));
        inventoryList.add(new InventoryItem("Chocolate Ice Cream", "15 tubs", "High"));
        inventoryList.add(new InventoryItem("Strawberry Ice Cream", "5 tubs", "Low"));

// Dairy
        inventoryList.add(new InventoryItem("Cheese", "10 blocks", "Optimal"));
        inventoryList.add(new InventoryItem("Butter", "8 packs", "Low"));
        inventoryList.add(new InventoryItem("Yogurt", "12 units", "High"));


        // Set up Adapter
        inventoryAdapter = new InventoryAdapter(getContext(), inventoryList);
        recyclerView.setAdapter(inventoryAdapter);

        // Set up SearchView
        SearchView searchView = view.findViewById(R.id.search_view_inventory);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                inventoryAdapter.filter(query); // Filter list on submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                inventoryAdapter.filter(newText); // Filter list on text change
                return false;
            }
        });

        // Handle FAB click
        FloatingActionButton fab = view.findViewById(R.id.fab_add_item);
        fab.setOnClickListener(v -> showFabMenu(fab));

        return view;
    }

    private void showFabMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(getContext(), anchor);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.inventory_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this::onMenuItemClick);
        popupMenu.show();
    }

    private boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            showAddItemDialog();
            return true;
        } else if (item.getItemId() == R.id.menu_edit) {
            showSearchItemDialog("Edit");
            return true;
        } else if (item.getItemId() == R.id.menu_remove) {
            showSearchItemDialog("Remove");
            return true;
        } else {
            return false;
        }
    }

    private void showAddItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add New Item");

        // Set up input fields
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_item, null);
        EditText itemNameInput = dialogView.findViewById(R.id.input_item_name);
        EditText itemStockInput = dialogView.findViewById(R.id.input_item_stock);
        EditText itemStatusInput = dialogView.findViewById(R.id.input_item_status);
        builder.setView(dialogView);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = itemNameInput.getText().toString();
            String stock = itemStockInput.getText().toString();
            String status = itemStatusInput.getText().toString();

            if (!name.isEmpty() && !stock.isEmpty() && !status.isEmpty()) {
                InventoryItem newItem = new InventoryItem(name, stock, status);
                inventoryList.add(newItem);
                inventoryAdapter.resetData(inventoryList); // Reset the adapter's data
                recyclerView.smoothScrollToPosition(inventoryList.size() - 1);
                Toast.makeText(getContext(), "Item added!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showSearchItemDialog(String action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(action + " Item");

        // Set up input field
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Search", (dialog, which) -> {
            String query = input.getText().toString();
            InventoryItem foundItem = null;

            for (InventoryItem item : inventoryList) {
                if (item.getItemName().equalsIgnoreCase(query)) {
                    foundItem = item;
                    break;
                }
            }

            if (foundItem != null) {
                if (action.equals("Edit")) {
                    showEditItemDialog(foundItem);
                } else if (action.equals("Remove")) {
                    int position = inventoryList.indexOf(foundItem);
                    inventoryList.remove(foundItem);
                    inventoryAdapter.resetData(inventoryList); // Reset the adapter's data
                    Toast.makeText(getContext(), "Item removed!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Item not found!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showEditItemDialog(InventoryItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit Item");

        // Set up input fields
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_item, null);
        EditText itemStockInput = dialogView.findViewById(R.id.input_item_stock);
        EditText itemStatusInput = dialogView.findViewById(R.id.input_item_status);

        itemStockInput.setText(item.getStockLevel());
        itemStatusInput.setText(item.getStatus());
        builder.setView(dialogView);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String updatedStock = itemStockInput.getText().toString();
            String updatedStatus = itemStatusInput.getText().toString();

            if (!updatedStock.isEmpty() && !updatedStatus.isEmpty()) {
                item.setStockLevel(updatedStock);
                item.setStatus(updatedStatus);
                inventoryAdapter.resetData(inventoryList); // Reset the adapter's data
                Toast.makeText(getContext(), "Item updated!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}