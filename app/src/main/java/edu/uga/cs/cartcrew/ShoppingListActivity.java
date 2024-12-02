package edu.uga.cs.cartcrew;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity
        implements AddShoppingItemDialogFragment.AddShoppingItemDialogListener {

    private RecyclerView recyclerView;
    private ShoppingListAdapter adapter;
    private List<ShoppingItem> itemList = new ArrayList<>();
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShoppingListAdapter(itemList, this);
        recyclerView.setAdapter(adapter);

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("shoppingList");

        // FloatingActionButton to add a new item
        FloatingActionButton addButton = findViewById(R.id.floatingActionButton);
        addButton.setOnClickListener(v -> {
            AddShoppingItemDialogFragment dialog = new AddShoppingItemDialogFragment();
            dialog.show(getSupportFragmentManager(), "AddItemDialog");
        });

        // Load existing shopping list items
        loadShoppingList();
    }

    private void loadShoppingList() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ShoppingItem item = postSnapshot.getValue(ShoppingItem.class);
                    itemList.add(item);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ShoppingListActivity", "Database read failed: " + error.getMessage());
            }
        });
    }

    @Override
    public void addShoppingItem(ShoppingItem shoppingItem) {
        databaseReference.push().setValue(shoppingItem)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Item added successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show());
    }
}
