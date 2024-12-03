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

public class LoginActivity extends AppCompatActivity {

    TextView textView;
    Button button;
    EditText emailField;
    EditText passwordField;

    private FirebaseAuth mAuth;

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
            mAuth.signInWithEmailAndPassword( enteredEmail, enteredPassword )
                    .addOnCompleteListener(this, new AuthenticationHandler());
        });
    }

    class AuthenticationHandler implements OnCompleteListener<AuthResult> {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                // launch main activity with user here?
                Intent intent = new Intent(LoginActivity.this, ShoppingListManagementActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                showError(task.getException());
            }
        }
    }

    private void showError(Exception e) {
        Toast toast = Toast.makeText(this.getApplicationContext(),
                "Authentication Failed: " + (e == null ? "" : e.getMessage()),
                Toast.LENGTH_SHORT);
        toast.show();
    }
}
