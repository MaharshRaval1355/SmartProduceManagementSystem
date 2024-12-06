package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder> {

    private Context context;
    private List<InventoryItem> inventoryList;
    private List<InventoryItem> fullInventoryList; // Original list for filtering

    public InventoryAdapter(Context context, List<InventoryItem> inventoryList) {
        this.context = context;
        this.inventoryList = new ArrayList<>(inventoryList);
        this.fullInventoryList = new ArrayList<>(inventoryList); // Copy for filtering
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_inventory, parent, false);
        return new InventoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        InventoryItem item = inventoryList.get(position);
        holder.itemName.setText(item.getItemName());
        holder.stockLevel.setText(item.getStockLevel());
        holder.status.setText(item.getStatus());

        // Set color based on status
        switch (item.getStatus().toLowerCase()) {
            case "low":
                holder.status.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                break;
            case "optimal":
                holder.status.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
                break;
            case "high":
                holder.status.setTextColor(context.getResources().getColor(android.R.color.holo_blue_dark));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return inventoryList.size();
    }

    // Method to filter the inventory list
    public void filter(String query) {
        inventoryList.clear();
        if (query.isEmpty()) {
            inventoryList.addAll(fullInventoryList); // Show all items if query is empty
        } else {
            for (InventoryItem item : fullInventoryList) {
                if (item.getItemName().toLowerCase().contains(query.toLowerCase())) {
                    inventoryList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class InventoryViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, stockLevel, status;

        public InventoryViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            stockLevel = itemView.findViewById(R.id.item_stock);
            status = itemView.findViewById(R.id.item_status);
        }
    }

    public void updateData(List<InventoryItem> newInventoryList) {
        this.inventoryList.clear();
        this.inventoryList.addAll(newInventoryList);
        this.fullInventoryList.clear();
        this.fullInventoryList.addAll(newInventoryList);
        notifyDataSetChanged();
    }

}
