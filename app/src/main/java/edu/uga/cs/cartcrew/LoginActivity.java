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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Activity which handles logging in.
 */
public class LoginActivity extends AppCompatActivity {

    TextView textView; // text displaying intent of activity
    Button button; // signup button
    EditText emailField; // email text entry
    EditText passwordField; // password text entry

    private FirebaseAuth mAuth; // authentication handler

    /**
     * Creates and initializes the activity.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        textView = findViewById(R.id.textView2);
        textView.setText("Login");

        emailField = findViewById(R.id.editTextTextEmailAddress);
        passwordField = findViewById(R.id.editTextTextPassword);

        button = findViewById(R.id.mainButton);
        button.setText("Login");
        button.setOnClickListener(v -> {
            String enteredEmail = emailField.getText().toString();
            String enteredPassword = passwordField.getText().toString();
            // sign in using firebase auth
            mAuth.signInWithEmailAndPassword( enteredEmail, enteredPassword )
                    .addOnCompleteListener(this, new AuthenticationHandler());
        });
    }

    /**
     * Custom listener to handle transitioning to the main activity after logging in
     */
    class AuthenticationHandler implements OnCompleteListener<AuthResult> {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                // launch main activity with user here
                Intent intent = new Intent(LoginActivity.this, ShoppingListManagementActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                showError(task.getException());
            }
        }
    }

    /**
     * Creates a toast based on an exception and shows it to the user.
     * @param e the exception to show
     */
    private void showError(Exception e) {
        Toast toast = Toast.makeText(this.getApplicationContext(),
                "Authentication Failed: " + (e == null ? "" : e.getMessage()),
                Toast.LENGTH_SHORT);
        toast.show();
    }
}
