package com.example.conectamobile;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<Message> messageList;
    private EditText editMessage;
    private ImageButton btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recyclerViewMessages);
        editMessage = findViewById(R.id.editMessage);
        btnSend = findViewById(R.id.btnSend);

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = editMessage.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    // Agregar mensaje enviado por el usuario
                    messageList.add(new Message(messageText, "Usuario"));
                    chatAdapter.notifyItemInserted(messageList.size() - 1);
                    recyclerView.scrollToPosition(messageList.size() - 1);

                    // Simular respuesta automática
                    simulateAutoResponse();
                    editMessage.setText("");
                } else {
                    Toast.makeText(Chat.this, "Por favor, ingresa un mensaje.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void simulateAutoResponse() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message("Respuesta automática", "Bot"));
                chatAdapter.notifyItemInserted(messageList.size() - 1);
                recyclerView.scrollToPosition(messageList.size() - 1);
            }
        }, 1000);
    }
}
