package pl.komunikator.komunikator.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import pl.komunikator.komunikator.R;
import pl.komunikator.komunikator.adapter.ChatAdapter;
import pl.komunikator.komunikator.entity.ChatMessage;

public class ChatFragment extends Fragment {

    private EditText messageET;
    private ListView messagesContainer;
    private ChatAdapter adapter;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        initControls(view);
        return view;


    }

    private void initControls(View view) {
        messagesContainer = (ListView) view.findViewById(R.id.messagesContainer);
        messageET = (EditText) view.findViewById(R.id.messageEdit);
        Button sendBtn = (Button) view.findViewById(R.id.chatSendButton);

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

        adapter = new ChatAdapter(getActivity().getApplicationContext(), new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        for(int i = 0; i < chatHistory.size(); i++) {
            ChatMessage message = chatHistory.get(i);
            displayMessage(message);
        }
    }
}
