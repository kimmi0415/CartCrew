package edu.uga.cs.cartcrew;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    TextView textView;
    Button button;
    EditText emailField;
    EditText passwordField;
    EditText nameField;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        textView = findViewById(R.id.textView2);
        textView.setText("Sign Up");

        nameField = findViewById(R.id.editTextText);
        emailField = findViewById(R.id.editTextTextEmailAddress);
        passwordField = findViewById(R.id.editTextTextPassword);

        button = findViewById(R.id.mainButton);
        button.setText("Sign Up");
        button.setOnClickListener(v -> {
            // validate name
            String enteredName = nameField.getText().toString();
            if (enteredName.trim().isEmpty()) showError("please enter a name.");

            // retrieve email and password
            String enteredEmail = emailField.getText().toString();
            String enteredPassword = passwordField.getText().toString();

            // create user
            mAuth.createUserWithEmailAndPassword(enteredEmail, enteredPassword)
                    .addOnCompleteListener(this, new AuthenticationHandler());
        });
    }

    class AuthenticationHandler implements OnCompleteListener<AuthResult> {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();

                // if successful, add name to database
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference userCollection = db.collection("users");

                // create a map to store info (will become the document in the firestore)
                Map<String, Object> userDetails = new HashMap<>();
                userDetails.put("uid", user.getUid());
                userDetails.put("name", nameField.getText().toString());

                // add the document to firestore in the "users" collection
                userCollection.add(userDetails)
                        .addOnSuccessListener(docRef -> {
                            Log.d("CartCrew", "Added");
                            // transition to new activity here?

                            // Transition to the Management Page after a successful sign-up
                            Intent intent = new Intent(SignupActivity.this, ShoppingListManagementActivity.class);
                            startActivity(intent);
                            finish();


                        })
                        .addOnFailureListener(e -> showError(e.getMessage()));
            }
            else {
                Exception taskException = task.getException();
                showError(taskException == null ? "" : taskException.getMessage());
            }
        }
    }

    private void showError(String s) {
        Toast toast = Toast.makeText(this.getApplicationContext(),
                "Signup Failed: " + s,
                Toast.LENGTH_SHORT);
        toast.show();
    }
}
