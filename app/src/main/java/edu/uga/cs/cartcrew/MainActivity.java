package edu.uga.cs.cartcrew;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 *  The main screen of the app with sign-in and login buttons
 */
public class MainActivity extends AppCompatActivity {
    Button signUpButton; // button to launch signup actvity
    Button loginButton; // button to launch login activity

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
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signUpButton = findViewById(R.id.signUpButton);
        loginButton = findViewById(R.id.loginButton);

        signUpButton.setOnClickListener(v -> {
            startAuthenticationActivity(v, "Sign Up");
        });

        loginButton.setOnClickListener(v -> {
            startAuthenticationActivity(v, "Login");
        });
    }

    /**
     * Starts either the login or signup activity
     * @param v the view which started the method
     * @param type "Sign Up" or "Login
     */
    private void startAuthenticationActivity(View v, String type) {
        Intent intent = new Intent(v.getContext(), (type.equals("Login") ? LoginActivity.class : SignupActivity.class));
        intent.putExtra("TYPE", type);
        startActivity(intent);
    }
}
