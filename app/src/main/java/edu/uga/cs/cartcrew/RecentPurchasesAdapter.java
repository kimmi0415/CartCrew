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

public class RecentPurchasesAdapter extends RecyclerView.Adapter<RecentPurchasesAdapter.ViewHolder> {

    private List<Purchase> itemList;
    private Context context;
    private RecentPurchasesActivity rpa;
    private String type;

    public RecentPurchasesAdapter(List<Purchase> itemList, Context context, RecentPurchasesActivity rpa, String type) {
        this.itemList = itemList;
        this.context = context;
        this.rpa = rpa;
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
