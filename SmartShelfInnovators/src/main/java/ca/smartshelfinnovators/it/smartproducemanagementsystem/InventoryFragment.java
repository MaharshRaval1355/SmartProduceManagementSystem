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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class InventoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private InventoryAdapter inventoryAdapter;
    private List<InventoryItem> inventoryList;
    private FirebaseFirestore db;
    private CollectionReference inventoryRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
        inventoryRef = db.collection(getString(R.string.inventory));

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_inventory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the inventory list
        inventoryList = new ArrayList<>();

        // Set up Adapter
        inventoryAdapter = new InventoryAdapter(getContext(), inventoryList);
        recyclerView.setAdapter(inventoryAdapter);
        loadInventoryData();


        // Set up SearchView
        SearchView searchView = view.findViewById(R.id.search_view_inventory);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                inventoryAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                inventoryAdapter.filter(newText);
                return false;
            }
        });

        // Handle FAB click
        FloatingActionButton fab = view.findViewById(R.id.fab_add_item);
        fab.setOnClickListener(v -> showFabMenu(fab));

        return view;
    }

    private void loadInventoryData() {
        inventoryRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            List<InventoryItem> newInventoryList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                InventoryItem item = document.toObject(InventoryItem.class);
                                newInventoryList.add(item);
                            }
                            // Use updateData() to refresh the data in the adapter
                            inventoryAdapter.updateData(newInventoryList);
                        } else {
                            Toast.makeText(getContext(), getString(R.string.no_data_available), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), getString(R.string.error_getting_data), Toast.LENGTH_SHORT).show();
                    }
                });

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
            showSearchItemDialog(getString(R.string.edit));
            return true;
        } else if (item.getItemId() == R.id.menu_remove) {
            showSearchItemDialog(getString(R.string.remove));
            return true;
        } else {
            return false;
        }
    }

    private void showAddItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.add_new_item);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_item, null);
        EditText itemNameInput = dialogView.findViewById(R.id.input_item_name);
        EditText itemStockInput = dialogView.findViewById(R.id.input_item_stock);
        EditText itemStatusInput = dialogView.findViewById(R.id.input_item_status);
        builder.setView(dialogView);

        builder.setPositiveButton(getString(R.string.add), (dialog, which) -> {
            String name = itemNameInput.getText().toString();
            String stock = itemStockInput.getText().toString();
            String status = itemStatusInput.getText().toString();

            if (!name.isEmpty() && !stock.isEmpty() && !status.isEmpty()) {
                InventoryItem newItem = new InventoryItem(name, stock, status);
                addItemToFirestore(newItem);  // Add item to Firestore
                inventoryList.add(newItem);
                inventoryAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), getString(R.string.item_added), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), getString(R.string.all_fields_are_required), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void addItemToFirestore(InventoryItem newItem) {
        inventoryRef.add(newItem)
                .addOnSuccessListener(documentReference -> {
                    // Item added successfully
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), getString(R.string.error_adding_item), Toast.LENGTH_SHORT).show();
                });
    }


    private void showSearchItemDialog(String action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(action + getString(R.string.item));

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(getString(R.string.search), (dialog, which) -> {
            String query = input.getText().toString();
            InventoryItem foundItem = null;

            for (InventoryItem item : inventoryList) {
                if (item.getItemName().equalsIgnoreCase(query)) {
                    foundItem = item;
                    break;
                }
            }

            if (foundItem != null) {
                if (action.equals(getString(R.string.edit))) {
                    showEditItemDialog(foundItem);
                } else if (action.equals(getString(R.string.remove))) {
                    inventoryList.remove(foundItem);
                    inventoryAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), getString(R.string.item_removed), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), getString(R.string.item_not_found), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showEditItemDialog(InventoryItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.edit_item);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_item, null);
        EditText itemStockInput = dialogView.findViewById(R.id.input_item_stock);
        EditText itemStatusInput = dialogView.findViewById(R.id.input_item_status);

        itemStockInput.setText(item.getStockLevel());
        itemStatusInput.setText(item.getStatus());
        builder.setView(dialogView);

        builder.setPositiveButton(getString(R.string.update), (dialog, which) -> {
            String updatedStock = itemStockInput.getText().toString();
            String updatedStatus = itemStatusInput.getText().toString();

            if (!updatedStock.isEmpty() && !updatedStatus.isEmpty()) {
                item.setStockLevel(updatedStock);
                item.setStatus(updatedStatus);
                inventoryAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), getString(R.string.item_updated), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), getString(R.string.all_fields_are_required), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
