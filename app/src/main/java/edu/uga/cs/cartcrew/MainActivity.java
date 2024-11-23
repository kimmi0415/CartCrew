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

public class MainActivity extends AppCompatActivity {
    Button signUpButton;
    Button loginButton;

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

    private void startAuthenticationActivity(View v, String type) {
        Intent intent = new Intent(v.getContext(), (type.equals("Login") ? LoginActivity.class : SignupActivity.class));
        intent.putExtra("TYPE", type);
        startActivity(intent);
    }
}
