package edu.uga.cs.cartcrew;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingViewHolder> {
    private final List<ShoppingItem> itemList;
    private final Context context;

    public ShoppingListAdapter(List<ShoppingItem> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ShoppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shopping_list_item, parent, false);
        return new ShoppingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingViewHolder holder, int position) {
        ShoppingItem item = itemList.get(position);
        holder.nameTextView.setText(item.getName());
        holder.quantityTextView.setText(item.getQuantity());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ShoppingViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, quantityTextView;

        public ShoppingViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.itemName);
            quantityTextView = itemView.findViewById(R.id.itemQuantity);
        }
    }
}