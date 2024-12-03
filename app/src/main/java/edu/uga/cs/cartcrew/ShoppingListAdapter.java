package edu.uga.cs.cartcrew;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    private List<ShoppingItem> itemList;
    private Context context;
    private ShoppingListActivity sla;
    private String type;

    public ShoppingListAdapter(List<ShoppingItem> itemList, Context context, ShoppingListActivity sla, String type) {
        this.itemList = itemList;
        this.context = context;
        this.sla = sla;
        this.type = type;
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
        if (type.equals("list")) {
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

        if (type.equals("list")) {
            holder.buyButton.setText("Buy");
            holder.buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // remove from main shopping list
                    sla.updateShoppingItem(holder.getAdapterPosition(), item, EditShoppingItemDialogFragment.DELETE, false);
                    // add to shopping basket
                    item.setKey(null);
                    sla.addShoppingItem(item, "shoppingBasket");
                    Toast.makeText(context, "Item moved to shopping basket", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (type.equals("basket")) {
            holder.buyButton.setText("Return");
            holder.buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // remove from main shopping list
                    sla.updateShoppingItem(holder.getAdapterPosition(), item, EditShoppingItemDialogFragment.DELETE, false);
                    // add to shopping basket
                    item.setKey(null);
                    sla.addShoppingItem(item, "shoppingList");
                    Toast.makeText(context, "Item returned to shopping list", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            holder.buyButton.setText("Return");
            holder.buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // remove from main shopping list
                    sla.deleteShoppingItemFromRecentlyPurchasedList(holder.getAdapterPosition());
                    // add to shopping basket
                    item.setKey(null);
                    sla.addShoppingItem(item, "shoppingList");
                    Toast.makeText(context, "Item returned to shopping list", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemNameTextView, itemQuantityTextView;
        public Button buyButton;

        public ViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemName);
            itemQuantityTextView = itemView.findViewById(R.id.itemQuantity);
            buyButton = itemView.findViewById(R.id.buyButton);
        }
    }
}
