package edu.uga.cs.cartcrew;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ShoppingListManagementActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "ManagementActivity";
    private TextView signedInTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_management);

        Log.d(DEBUG_TAG, "ShoppingListManagementActivity.onCreate()");

        // Initialize buttons and signed-in text view
        Button viewListButton = findViewById(R.id.buttonViewList);
        Button viewBasketButton = findViewById(R.id.buttonViewBasket);
        Button viewRecentPurchasesButton = findViewById(R.id.buttonViewRecentPurchases);
        Button logoutButton = findViewById(R.id.logoutButton);  // Initialize the logout button
        signedInTextView = findViewById(R.id.textViewSignedIn);

        // Set up button click listeners
        viewListButton.setOnClickListener(new ViewListButtonClickListener());
        viewBasketButton.setOnClickListener(new ViewBasketButtonClickListener());
        viewRecentPurchasesButton.setOnClickListener(new ViewPurchasesButtonClickListener());

        // Logout button click listener
        logoutButton.setOnClickListener(v -> logout());

        // Firebase authentication listener
        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null) {
                // User is signed in
                Log.d(DEBUG_TAG, "onAuthStateChanged:signed_in:" + currentUser.getUid());
                String userEmail = currentUser.getEmail();
                signedInTextView.setText("Signed in as: " + userEmail);
            } else {
                // User is signed out
                Log.d(DEBUG_TAG, "onAuthStateChanged:signed_out");
                signedInTextView.setText("Signed in as: not signed in");
            }
        });
    }

    // Logout method
    private void logout() {
        FirebaseAuth.getInstance().signOut(); // Sign out from Firebase
        Intent intent = new Intent(ShoppingListManagementActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
        startActivity(intent);
        finish(); // Close the current activity
    }

    // Button listener for viewing the shopping list
    private class ViewListButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), ShoppingListActivity.class);
            intent.putExtra("type", "list");
            startActivity(intent);
        }
    }

    // Button listener for viewing the shopping basket
    private class ViewBasketButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), ShoppingListActivity.class);
            intent.putExtra("type", "basket");
            startActivity(intent);
        }
    }

    // Button listener for viewing the recent purchases
    private class ViewPurchasesButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), RecentPurchasesActivity.class);
            intent.putExtra("type", "purchases");
            startActivity(intent);
        }
    }
}
