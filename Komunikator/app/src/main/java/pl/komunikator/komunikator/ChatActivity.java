package pl.komunikator.komunikator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    private EditText messageET;
    private ListView messagesContainer;
    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initControls();
    }

    private void initControls() {
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        Button sendBtn = (Button) findViewById(R.id.chatSendButton);

        loadDummyHistory();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.id = 122;//dummy
                chatMessage.message = messageText;
                chatMessage.dateTime = DateFormat.getDateTimeInstance().format(new Date());
                chatMessage.isMe = true;

                messageET.setText("");

                displayMessage(chatMessage);
            }
        });
    }

    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void loadDummyHistory(){

        ArrayList<ChatMessage> chatHistory = new ArrayList<>();

        ChatMessage msg = new ChatMessage();
        msg.id = 1;
        msg.isMe = false;
        msg.message = "Cześć!";
        msg.dateTime = DateFormat.getDateTimeInstance().format(new Date());
        chatHistory.add(msg);
        ChatMessage msg1 = new ChatMessage();
        msg1.id = 2;
        msg1.isMe = false;
        msg1.message = "Jak się masz?";
        msg1.dateTime = DateFormat.getDateTimeInstance().format(new Date());
        chatHistory.add(msg1);

        adapter = new ChatAdapter(ChatActivity.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        for(int i = 0; i < chatHistory.size(); i++) {
            ChatMessage message = chatHistory.get(i);
            displayMessage(message);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.conversations_menu, menu);

        setTitle(R.string.contact_placeholder);
        return true;
    }
}
