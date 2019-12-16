package com.uit.uitnowfordrivers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SignInActivity  extends AppCompatActivity {
    App app;
    Button login;
    EditText txtEmail,txtPassword;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        login=findViewById(R.id.btnSignIn);
        txtEmail=findViewById(R.id.txtEmail);
        txtPassword=findViewById(R.id.txtPassword);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=txtEmail.getText().toString();
                String pass=txtPassword.getText().toString();
                signIn(email,pass);
            }
        });
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        app=(App)getApplication();
    }

    @Override
    protected void onStart() {
        super.onStart();
        final String email=PrefUtil.loadPref(this,"email");
        if(email!=null)
        {
            db= FirebaseFirestore.getInstance();
            db.collection("Drivers").whereEqualTo("email",email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Driver d=document.toObject(Driver.class);
                            app.driver=d;
                            PrefUtil.savePref(SignInActivity.this,"email",email);
                            PrefUtil.savePref(SignInActivity.this,"phone",d.getPhone());
                            PrefUtil.savePref(SignInActivity.this,"id",d.getId());
                            PrefUtil.savePref(SignInActivity.this,"name",d.getName());
                            PrefUtil.savePref(SignInActivity.this,"photo",d.getPhoto());
                            getInMainActivity();
                        }
                    } else {
                        Log.d("Test", "Error getting documents: ", task.getException());
                    }
                }
            });
        }
    }

    private void signIn(final String email, String pass)
    {
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
// đăng nhập thành công, cập nhật UI với thông tin của người dùng
//                            Log.d("Test", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            db.collection("Drivers").whereEqualTo("email",email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Driver d=document.toObject(Driver.class);
                                            app.driver=d;
                                            PrefUtil.savePref(SignInActivity.this,"email",email);
                                            PrefUtil.savePref(SignInActivity.this,"phone",d.getPhone());
                                            PrefUtil.savePref(SignInActivity.this,"id",d.getId());
                                            PrefUtil.savePref(SignInActivity.this,"name",d.getName());
                                            PrefUtil.savePref(SignInActivity.this,"photo",d.getPhoto());
                                            getInMainActivity();
                                        }
                                    } else {
                                        Log.d("Test", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                        } else {
// nếu đăng nhập thất bại, thông báo lỗi đến người dùng
                            Log.w("Test", "SignInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Đăng nhập thất bại",
                                    Toast.LENGTH_SHORT).show();

                        }
// ...
                    }
                });
    }

    private void getInMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
