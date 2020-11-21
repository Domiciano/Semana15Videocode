package co.domi.authandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameET, lastnameET, cityET, emailET, passET;
    private TextView loginLink;
    private Button signupBtn;
    private FirebaseAuth auth;
    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameET = findViewById(R.id.nameET);
        lastnameET = findViewById(R.id.lastnameET);
        cityET = findViewById(R.id.cityET);
        emailET = findViewById(R.id.emailET);
        passET = findViewById(R.id.passET);
        signupBtn = findViewById(R.id.signupBtn);
        loginLink = findViewById(R.id.loginLink);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        signupBtn.setOnClickListener(this);
        loginLink.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signupBtn:
                auth.createUserWithEmailAndPassword(emailET.getText().toString(), passET.getText().toString())
                        .addOnCompleteListener(
                                task -> {
                                    if(task.isSuccessful()){
                                        String id = auth.getCurrentUser().getUid();
                                        User user = new User(
                                                id,
                                                nameET.getText().toString(),
                                                lastnameET.getText().toString(),
                                                cityET.getText().toString(),
                                                emailET.getText().toString(),
                                                passET.getText().toString()
                                        );
                                        db.getReference().child("semana14").child("users").child(id).setValue(user).addOnCompleteListener(
                                                taskdb -> {
                                                    if (taskdb.isSuccessful()){
                                                        Intent intent = new Intent(this, ProfileActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }
                                        );

                                    }else{
                                        Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                        );
                break;
            case R.id.loginLink:
                finish();
                break;
        }
    }
}