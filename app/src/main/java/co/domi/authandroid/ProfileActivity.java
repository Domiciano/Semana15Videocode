package co.domi.authandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView welcomeTV;
    private Button signOutBtn;
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        if(auth.getCurrentUser() == null){
            goToLogin();
        } else{
            welcomeTV = findViewById(R.id.welcomeTV);
            signOutBtn = findViewById(R.id.signOutBtn);
            recoverUser();
            signOutBtn.setOnClickListener(this);
        }

    }

    private void goToLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void recoverUser() {
        if(auth.getCurrentUser() != null){
            String id = auth.getCurrentUser().getUid();
            db.getReference().child("semana14").child("users").child(id).addListenerForSingleValueEvent( //ONCE
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            user = snapshot.getValue(User.class);
                            welcomeTV.setText("Hola " + user.getNombre() + " de "+ user.getCiudad());
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    }
            );
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signOutBtn:
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("Cerrar sesión")
                        .setMessage("¿Esta seguro que desea cerrar sesión?")
                        .setNegativeButton("NO", (dialog, id) -> {
                            dialog.dismiss();
                        })
                        .setPositiveButton("SI", (dialog, id) -> {
                            auth.signOut();
                            goToLogin();
                        });
                builder.show();
                break;
        }
    }
}