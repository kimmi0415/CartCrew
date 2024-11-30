package edu.uga.cs.cartcrew;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

public class ShoppingListActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "ShoppingListActivity";
    private RecyclerView recyclerView;
    private ShoppingListAdapter adapter;
    private List<ShoppingItem> itemList = new ArrayList<>();
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton addButton = findViewById(R.id.floatingActionButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShoppingListAdapter(itemList, this);
        recyclerView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("shoppingList");

        // Fetch the data
        ref.addValueEventListener(new ValueEventListener() {
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
                Log.e(DEBUG_TAG, "Database read failed: " + error.getMessage());
            }
        });

        // Add new item dialog
        addButton.setOnClickListener(view -> {
            AddShoppingItemDialogFragment dialog = new AddShoppingItemDialogFragment();
            dialog.show(getSupportFragmentManager(), "AddItemDialog");
        });
    }
}