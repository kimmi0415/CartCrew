package edu.uga.cs.cartcrew;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingItemHolder> {

    private List<ShoppingItem> shoppingList;
    private Context context;

    public ShoppingListAdapter(List<ShoppingItem> shoppingList, Context context) {
        this.shoppingList = shoppingList;
        this.context = context;
    }

    class ShoppingItemHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemQuantity;

        public ShoppingItemHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
        }
    }

    @NonNull
    @Override
    public ShoppingItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_item, parent, false);
        return new ShoppingItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingItemHolder holder, int position) {
        ShoppingItem shoppingItem = shoppingList.get(position);
        holder.itemName.setText(shoppingItem.getName());
        holder.itemQuantity.setText(String.valueOf(shoppingItem.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return shoppingList.size();
    }

    // Add a new shopping item and notify the adapter
    public void addItem(ShoppingItem item) {
        shoppingList.add(item);
        notifyItemInserted(shoppingList.size() - 1);
    }
}
