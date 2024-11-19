package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InventoryFragment extends Fragment {

    private ListView inventoryListView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the ListView
        inventoryListView = view.findViewById(R.id.inventory_list);

        // Dummy inventory data
        String[] inventoryItems = {
                "Product 1 - Quantity: 10",
                "Product 2 - Quantity: 15",
                "Product 3 - Quantity: 7",
                "Product 4 - Quantity: 25",
                "Product 5 - Quantity: 12"
        };

        // Set up the adapter for the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                inventoryItems
        );

        inventoryListView.setAdapter(adapter);

        // Set up item click listener
        inventoryListView.setOnItemClickListener((parent, itemView, position, id) -> {
            String selectedItem = inventoryItems[position];
            Toast.makeText(requireContext(), "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
        });
    }
}

