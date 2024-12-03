package edu.uga.cs.cartcrew;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Map;

public class SettlementDetailsActivity extends AppCompatActivity {

    private TextView settlementDetailsTextView;
    private Button clearPurchasesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement_details);

        settlementDetailsTextView = findViewById(R.id.settlementDetailsTextView);
        clearPurchasesButton = findViewById(R.id.clearPurchasesButton);

        // Retrieve the details passed from the previous activity
        String settlementDetails = getIntent().getStringExtra("settlementDetails");
        settlementDetailsTextView.setText(settlementDetails);

        clearPurchasesButton.setOnClickListener(v -> clearPurchases());
    }

    private void clearPurchases() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("purchaseList");
        databaseReference.setValue(null)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Purchases cleared successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to clear purchases.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
