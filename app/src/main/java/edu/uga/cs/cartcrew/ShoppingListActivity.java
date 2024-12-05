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

/**
 * This activity handles displaying information from the shopping list,
 * shopping basket, or a recent purchase.
 */
public class ShoppingListActivity extends AppCompatActivity
        implements AddShoppingItemDialogFragment.AddShoppingItemDialogListener,
        EditShoppingItemDialogFragment.EditShoppingItemDialogListener {

    private static final String DEBUG_TAG = "ShoppingListActivity";

    private RecyclerView recyclerView; // the recycler view displaying the data
    private ShoppingListAdapter adapter; // adapter for the recycler view
    private List<ShoppingItem> itemList = new ArrayList<>(); // list of items to display
    private DatabaseReference databaseReference; // reference in the realtime database to pull from
    private String type; // the type of list to show
    private FirebaseAuth mAuth; // authentication for handling new purchases

    /**
     * Initializes the activity, setting up the layout, RecyclerView, and Firebase connection.
     *
     * @param savedInstanceState the saved instance state.
     */
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

        // Initialize instance variables and listeners according to whether this
        // activity is being used to show the shopping list, shopping basket,
        // or a particular recent purchase
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
                ((ViewGroup) addButton.getParent()).removeView(addButton); // unneeded
                break;
        }

        loadShoppingList();
    }

    /**
     * Loads the entire shopping list from the database into the adapter.
     */
    private void loadShoppingList() {
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
    }

    /**
     * Handles adding a new shopping item to the database.
     * @param shoppingItem the new shopping item to be added
     * @param showToast whether to show a toast upon success (will still
     *                  be shown on failure)
     */
    @Override
    public void addShoppingItem(ShoppingItem shoppingItem, boolean showToast) {
        databaseReference.push().setValue(shoppingItem)
                .addOnSuccessListener(showToast ?
                        aVoid -> Toast.makeText(this, "Item added successfully!", Toast.LENGTH_SHORT).show()
                        : aVoid -> {})
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show());
    }

    /**
     * Handles updating or deleting a shopping item in the database.
     * @param position the position in the recycler view of the item
     * @param shoppingItem the item's details
     * @param action 1 = EDIT, 2 = DELETE
     * @param showToast whether to show a toast upon success (will still
     *                  be shown on failure)
     */
    @Override
    public void updateShoppingItem(int position, ShoppingItem shoppingItem, int action, boolean showToast) {
        if (action == EditShoppingItemDialogFragment.SAVE) {
            // updates an item in the database
            DatabaseReference itemRef = databaseReference.child(shoppingItem.getKey());
            itemRef.setValue(shoppingItem)
                    .addOnSuccessListener(showToast ?
                            aVoid -> Toast.makeText(this, "Item updated successfully!", Toast.LENGTH_SHORT).show()
                            : aVoid -> {})
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to update item", Toast.LENGTH_SHORT).show());
            adapter.notifyItemChanged(position);
        } else if (action == EditShoppingItemDialogFragment.DELETE) {
            // deletes an item from the database
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

    /**
     * Deletes an item from a recent purchase
     * @param position the position of the item in the adapter
     */
    public void deleteShoppingItemFromRecentlyPurchasedList(int position) {
        itemList.remove(position);
        databaseReference.setValue(itemList);
        adapter.notifyDataSetChanged();
    }

    /**
     * Adds a new item to a specific reference in the database
     * @param item the item to be added
     * @param reference the location
     */
    public void addShoppingItem(ShoppingItem item, String reference) {
        DatabaseReference temp = databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference(reference);
        addShoppingItem(item, false);
        databaseReference = temp;
    }

    /**
     * Handles pushing a purchase of the current items in the shopping list to the database
     * @param price the total price of the purchase
     */
    public void finalizePurchase(double price) {
        if (itemList.isEmpty()) {
            Toast.makeText(this, "Please add at least one item to the basket", Toast.LENGTH_LONG).show();
            return;
        }

        // Pushes purchase to the database
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
