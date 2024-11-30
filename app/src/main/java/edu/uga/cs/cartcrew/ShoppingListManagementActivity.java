package edu.uga.cs.cartcrew;

import androidx.annotation.NonNull;
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
        Button addItemButton = findViewById(R.id.buttonAddItem);
        Button viewListButton = findViewById(R.id.buttonViewList);
        signedInTextView = findViewById(R.id.textViewSignedIn);

        // Set up button click listeners
        addItemButton.setOnClickListener(new AddItemButtonClickListener());
        viewListButton.setOnClickListener(new ViewListButtonClickListener());

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

    // Button listener for adding a new item to the shopping list
    private class AddItemButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), AddShoppingItemDialogFragment.class);
            startActivity(intent);
        }
    }

    // Button listener for viewing the shopping list
    private class ViewListButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), ShoppingListActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(DEBUG_TAG, "ShoppingListManagementActivity.onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(DEBUG_TAG, "ShoppingListManagementActivity.onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(DEBUG_TAG, "ShoppingListManagementActivity.onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(DEBUG_TAG, "ShoppingListManagementActivity.onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(DEBUG_TAG, "ShoppingListManagementActivity.onDestroy()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(DEBUG_TAG, "ShoppingListManagementActivity.onRestart()");
    }
}
