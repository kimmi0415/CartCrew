package edu.uga.cs.cartcrew;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Recycler adapter for the recent purchases activity.
 */
public class RecentPurchasesAdapter extends RecyclerView.Adapter<RecentPurchasesAdapter.ViewHolder> {

    private List<Purchase> itemList; // the list of purchases to show
    private Context context; // the original context
    private RecentPurchasesActivity rpa; // the recent purchases activity instance
    private String type; // the type of adapter

    /**
     * Creats a new adapter.
     * @param itemList the list of purchases
     * @param context the original context
     * @param rpa the activity instance
     * @param type the type of adapter
     */
    public RecentPurchasesAdapter(List<Purchase> itemList, Context context, RecentPurchasesActivity rpa, String type) {
        this.itemList = itemList;
        this.context = context;
        this.rpa = rpa;
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
        Purchase item = itemList.get(position);

        // Bind data to the item views
        holder.itemNameTextView.setText(item.getBuyer());
        holder.itemQuantityTextView.setText("Spent $" + item.getPrettyPrice() + " on " + item.getDateAsString());
        holder.buyButton.setText("Update Price");
        holder.buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePriceDialogFragment dialog = UpdatePriceDialogFragment.newInstance(holder.getAdapterPosition(), item.getKey(), item.getPrice());
                dialog.show(((RecentPurchasesActivity) context).getSupportFragmentManager(), "UpdateDialog");
            }
        });

        // Handle edit dialog opening when an item is clicked
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ShoppingListActivity.class);
            intent.putExtra("type", "purchaseList/" + item.getKey() + "/items");
            context.startActivity(intent);
        });
    }

    /**
     * Returns the number of items
     * @return the size of the list
     */
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    /**
     * ViewHolder class for showing individual past purchases.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemNameTextView, itemQuantityTextView;
        public Button buyButton;

        /**
         * Creates a new view holder.
         * @param itemView the view for each item
         */
        public ViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemName);
            itemQuantityTextView = itemView.findViewById(R.id.itemQuantity);
            buyButton = itemView.findViewById(R.id.buyButton);
        }
    }
}
