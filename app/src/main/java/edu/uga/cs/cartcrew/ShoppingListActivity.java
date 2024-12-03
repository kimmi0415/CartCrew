package edu.uga.cs.cartcrew;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ShoppingListActivity extends AppCompatActivity
        implements AddShoppingItemDialogFragment.AddShoppingItemDialogListener,
        EditShoppingItemDialogFragment.EditShoppingItemDialogListener {

    private static final String DEBUG_TAG = "ShoppingListActivity";

    private RecyclerView recyclerView;
    private ShoppingListAdapter adapter;
    private List<ShoppingItem> itemList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private String type;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            type = extras.getString("type");
        }
        setContentView(R.layout.activity_shopping_list);
        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShoppingListAdapter(itemList, this, this, type);
        recyclerView.setAdapter(adapter);
        FloatingActionButton addButton = findViewById(R.id.floatingActionButton);
        TextView header = findViewById(R.id.textView);

        switch (type) {
            case "list":
                databaseReference = FirebaseDatabase.getInstance().getReference("shoppingList");
                addButton.setOnClickListener(v -> {
                    AddShoppingItemDialogFragment dialog = new AddShoppingItemDialogFragment();
                    dialog.show(getSupportFragmentManager(), "AddItemDialog");
                });
                break;
            case "basket":
                header.setText("Shopping Basket");
                databaseReference = FirebaseDatabase.getInstance().getReference("shoppingBasket");
                addButton.setImageResource(R.drawable.check);
                addButton.setOnClickListener(v -> {
                    FinalizePurchaseDialogFragment dialog = new FinalizePurchaseDialogFragment();
                    dialog.show(getSupportFragmentManager(), "FinalizePurchaseDialog");
                });
                break;
            default:
                // this is used when looking at a specific purchase
                header.setText("Past Purchase");
                databaseReference = FirebaseDatabase.getInstance().getReference(type);
                ((ViewGroup) addButton.getParent()).removeView(addButton);
                break;
        }

        loadShoppingList();
    }

    private void loadShoppingList() {
        if (type.equals("list") || type.equals("basket")) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    itemList.clear();
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        ShoppingItem item = postSnapshot.getValue(ShoppingItem.class);
                        if (item != null) {
                            item.setKey(postSnapshot.getKey());
                            itemList.add(item);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(DEBUG_TAG, "Database read failed: " + error.getMessage());
                }
            });
        } else {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    itemList.clear();
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        // should only be one
                        Log.d("D", postSnapshot.getValue() + "");
                        Log.d("D", type);
                        ShoppingItem item = postSnapshot.getValue(ShoppingItem.class);
                        itemList.add(item);
                        /*
                        ShoppingItem item = postSnapshot.getValue(ShoppingItem.class);
                        if (item != null) {
                            item.setKey(postSnapshot.getKey());
                            itemList.add(item);
                        } */
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(DEBUG_TAG, "Database read failed: " + error.getMessage());
                }
            });
        }
    }

    @Override
    public void addShoppingItem(ShoppingItem shoppingItem, boolean showToast) {
        databaseReference.push().setValue(shoppingItem)
                .addOnSuccessListener(showToast ?
                        aVoid -> Toast.makeText(this, "Item added successfully!", Toast.LENGTH_SHORT).show()
                        : aVoid -> {})
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void updateShoppingItem(int position, ShoppingItem shoppingItem, int action, boolean showToast) {
        if (action == EditShoppingItemDialogFragment.SAVE) {
            DatabaseReference itemRef = databaseReference.child(shoppingItem.getKey());
            itemRef.setValue(shoppingItem)
                    .addOnSuccessListener(showToast ?
                            aVoid -> Toast.makeText(this, "Item updated successfully!", Toast.LENGTH_SHORT).show()
                            : aVoid -> {})
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to update item", Toast.LENGTH_SHORT).show());
            adapter.notifyItemChanged(position);
        } else if (action == EditShoppingItemDialogFragment.DELETE) {
            DatabaseReference itemRef = databaseReference.child(shoppingItem.getKey());
            itemRef.removeValue()
                    .addOnSuccessListener(showToast ?
                            aVoid -> Toast.makeText(this, "Item deleted successfully!", Toast.LENGTH_SHORT).show()
                            : aVoid -> {})
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to delete item", Toast.LENGTH_SHORT).show());
            itemList.remove(position);
            adapter.notifyItemRemoved(position);
        }
    }

    public void deleteShoppingItemFromRecentlyPurchasedList(int position) {
        itemList.remove(position);
        databaseReference.setValue(itemList);
        adapter.notifyDataSetChanged();
    }

    public void addShoppingItem(ShoppingItem item, String reference) {
        DatabaseReference temp = databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference(reference);
        addShoppingItem(item, false);
        databaseReference = temp;
    }

    public void finalizePurchase(double price) {
        if (itemList.isEmpty()) {
            Toast.makeText(this, "Please add at least one item to the basket", Toast.LENGTH_LONG).show();
            return;
        }

        // will only be called when the database reference is for shoppingBasket
        Purchase purchase = new Purchase(price, itemList, mAuth.getCurrentUser().getEmail());
        DatabaseReference purchaseList = FirebaseDatabase.getInstance().getReference("purchaseList");
        purchaseList.push().setValue(purchase)
                .addOnFailureListener(e -> Toast.makeText(this, "Purchase failed", Toast.LENGTH_SHORT).show());

        // clear shopping basket
        databaseReference.setValue(null).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Purchased successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Purchase succeeded but failed to clear basket", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
