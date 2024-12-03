package edu.uga.cs.cartcrew;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.Map;
import java.util.Objects;

public class RecentPurchasesActivity extends AppCompatActivity
        implements UpdatePriceDialogFragment.UpdatePriceDialogFragmentListener {

    private static final String DEBUG_TAG = "ShoppingListActivity";

    private RecyclerView recyclerView;
    private RecentPurchasesAdapter adapter;
    private List<Purchase> itemList = new ArrayList<>();
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
        adapter = new RecentPurchasesAdapter(itemList, this, this, type);
        recyclerView.setAdapter(adapter);
        FloatingActionButton addButton = findViewById(R.id.floatingActionButton);
        addButton.setImageResource(R.drawable.check);
        TextView header = findViewById(R.id.textView);
        header.setText("Recent Purchases");
        databaseReference = FirebaseDatabase.getInstance().getReference("purchaseList");


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settleCosts();
                // TODO - settle the cost
                // iterate through itemList - each object holds
                // total cost of purchase and who bought it,
                // show information as described, then push
                // null to the purchaseList reference to clear it
            }
        });

        loadShoppingList();
    }

    private void settleCosts() {
        if (itemList.isEmpty()) {
            Toast.makeText(this, "No purchases to settle.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Step 1: Calculate total cost and individual roommate expenditures
        double totalCost = 0.0;
        Map<String, Double> roommateExpenses = new HashMap<>();

        for (Purchase purchase : itemList) {
            totalCost += purchase.getPrice();
            String buyer = purchase.getBuyer();
            roommateExpenses.put(buyer, roommateExpenses.getOrDefault(buyer, 0.0) + purchase.getPrice());
        }

        // Step 2: Calculate the average amount spent
        int numberOfRoommates = roommateExpenses.size();
        double averageSpent = totalCost / numberOfRoommates;

        // Step 3: Prepare the settlement details
        StringBuilder result = new StringBuilder("Settlement Details:\n");
        for (Map.Entry<String, Double> entry : roommateExpenses.entrySet()) {
            String roommate = entry.getKey();
            double spent = entry.getValue();
            double difference = spent - averageSpent;

            result.append(String.format("%s spent: $%.2f, Difference: $%.2f\n", roommate, spent, difference));
        }
        result.append(String.format("Total Spent: $%.2f, Average Spent: $%.2f", totalCost, averageSpent));

        // Step 4: Launch SettlementDetailsActivity
        Intent intent = new Intent(this, SettlementDetailsActivity.class);
        intent.putExtra("settlementDetails", result.toString());
        startActivity(intent);
    }


    private void loadShoppingList() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Purchase item = postSnapshot.getValue(Purchase.class);
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

    public void updatePrice(String key, int position, double newPrice) {
        DatabaseReference itemRef = databaseReference.child(key).child("price");
        itemRef.setValue(newPrice)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Item updated successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update item", Toast.LENGTH_SHORT).show());
        adapter.notifyItemChanged(position);
    }
}


