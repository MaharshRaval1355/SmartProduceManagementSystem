package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        inventoryList.add(new InventoryItem("Milk", "5 liters", "Low"));
        inventoryList.add(new InventoryItem("Bread", "20 units", "Optimal"));
        inventoryList.add(new InventoryItem("Apples", "15 kg", "High"));
        inventoryList.add(new InventoryItem("Chicken", "10 kg", "Low"));

        // Set up Adapter
        inventoryAdapter = new InventoryAdapter(getContext(), inventoryList);
        recyclerView.setAdapter(inventoryAdapter);

        // Handle FAB click
        FloatingActionButton fab = view.findViewById(R.id.fab_add_item);
        fab.setOnClickListener(v -> {
            // Add logic to add, edit, or delete inventory items
        });

        return view;
    }
}