package com.uit.uitnowfordrivers;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{
    App app;
    RecyclerView rvMessage;
    EditText txtMessage;
    ImageView ivSend;
    MessageAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        app = (App) getApplication();
        rvMessage = findViewById(R.id.rvMessage);
        txtMessage = findViewById(R.id.txtMessage);
        ivSend = findViewById(R.id.ivSend);
        ivSend.setOnClickListener(this);
        adapter = new MessageAdapter(new ArrayList<Message>(), app.driver.id);
        rvMessage.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMessages();
    }

    private void getMessages() {
        final CollectionReference colRef = db.collection("Channels").document(app.currentRequest.getUserId() + "_" + app.driver.id).collection("Messages");
        colRef.orderBy("dateTime").limit(100).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("Test", "Listen failed.", e);
                    return;
                }

                if (queryDocumentSnapshots != null) {
                    List<Message> messages = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Message msg = document.toObject(Message.class);
                        messages.add(msg);
                    }
                    adapter.setMessages(messages);
                } else {
                    Log.d("Test", "Current data: null");
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivSend) {
            String msg = txtMessage.getText().toString().trim();
            if (!TextUtils.isEmpty(msg)) {
                Message message = new Message(app.driver.id, msg, System.currentTimeMillis());
                db.collection("Channels").document(app.currentRequest.getUserId() + "_" + app.driver.id).collection("Messages").add(message);
            }
            txtMessage.setText("");
        }
    }
}
