package edu.uga.cs.cartcrew;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    private List<ShoppingItem> itemList;
    private Context context;

    public ShoppingListAdapter(List<ShoppingItem> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopping_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingItem item = itemList.get(position);

        // Bind data to the item views
        holder.itemNameTextView.setText(item.getName());
        holder.itemQuantityTextView.setText(item.getQuantity());

        // Handle edit dialog opening when an item is clicked
        holder.itemView.setOnClickListener(v -> {
            EditShoppingItemDialogFragment editDialog = EditShoppingItemDialogFragment.newInstance(
                    position,
                    item.getKey(),
                    item.getName(),
                    item.getQuantity()
            );
            // Show the dialog
            editDialog.show(((ShoppingListActivity) context).getSupportFragmentManager(), "EditDialog");
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemNameTextView, itemQuantityTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemName);
            itemQuantityTextView = itemView.findViewById(R.id.itemQuantity);
        }
    }
}
