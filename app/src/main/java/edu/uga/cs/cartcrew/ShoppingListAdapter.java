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

/**
 * RecyclerView adapter for displaying information in the
 * ShoppingListActivity class.
 */
public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    private List<ShoppingItem> itemList; // the list of items to show
    private Context context; // the context of the parent activity
    private ShoppingListActivity sla; // the corresponding activity
    private String type; // the type of list this is being used for

    /**
     * Constructs a ShoppingListAdapter.
     *
     * @param itemList the list of shopping items to display
     * @param context the context in which this adapter is operating
     * @param sla the parent activity managing shopping list operations
     * @param type the type of list this is being used for
     */
    public ShoppingListAdapter(List<ShoppingItem> itemList, Context context, ShoppingListActivity sla, String type) {
        this.itemList = itemList;
        this.context = context;
        this.sla = sla;
        this.type = type;
    }

    /**
     * Creates a new ViewHolder based on the XML layout
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return the new ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopping_list_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds the data into the ViewHolder.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
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

        // Setup proper display and behavior according to whether this
        // adapter is being used for the shopping list, the shopping
        // basket, or a recent purchase
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
                    // remove from shopping basket
                    sla.updateShoppingItem(holder.getAdapterPosition(), item, EditShoppingItemDialogFragment.DELETE, false);
                    // add to main shopping list
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
                    // remove from this purchase
                    sla.deleteShoppingItemFromRecentlyPurchasedList(holder.getAdapterPosition());
                    // add to main shopping list
                    item.setKey(null);
                    sla.addShoppingItem(item, "shoppingList");
                    Toast.makeText(context, "Item returned to shopping list", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Returns the total number of items in the list.
     *
     * @return the number of items.
     */
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    /**
     * ViewHolder class for managing individual shopping list items.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemNameTextView, itemQuantityTextView;
        public Button buyButton;


        /**
         * Constructs a ViewHolder and initializes its views.
         *
         * @param itemView the view representing a single item.
         */
        public ViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemName);
            itemQuantityTextView = itemView.findViewById(R.id.itemQuantity);
            buyButton = itemView.findViewById(R.id.buyButton);
        }
    }
}
