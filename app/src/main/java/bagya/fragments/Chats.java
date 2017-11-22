package bagya.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import bagya.chatapp.ChatAdapter;
import bagya.chatapp.ChatMessage;
import bagya.chatapp.CommonMethods;
import bagya.chatapp.MainActivity;
import bagya.chatapp.R;


public class Chats extends Fragment implements OnClickListener {
    private EditText contactEditText;
    private EditText msg_edittext;
    private String sender = "nuwanthi", receiver="";
    private Random random;
    public static ArrayList<ChatMessage> chatlist;
    public static ChatAdapter chatAdapter;
    ListView msgListView;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    private View view;
    private String message;
    private ChatMessage chatMessage;

    public void setReceiver(String user){
        this.receiver=user;
    }
    public String getReceiver(){
        return this.receiver;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chat_layout, container, false);
        msg_edittext = (EditText) view.findViewById(R.id.messageEditText);
        msgListView = (ListView) view.findViewById(R.id.msgListView);
        random = new Random();
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(
                "Chats");

        //get spinner
        spinner = (Spinner) view.findViewById(R.id.contactSpinner);
        ArrayAdapter<CharSequence> ar=ArrayAdapter.createFromResource(getActivity(),R.array.contacts,android.R.layout.simple_spinner_item);
        ar.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(ar);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view1, int position, long id)
            {
                String user = parent.getItemAtPosition(position).toString();
                setReceiver(user);
                Toast.makeText(getActivity(), "Selected contact: " + user, Toast.LENGTH_LONG).show();

                //Clear the previous messages from user when new contact selected
                chatlist = new ArrayList<>();
                chatAdapter = new ChatAdapter(getActivity(), chatlist);
                msgListView.setAdapter(chatAdapter);
                chatAdapter.notifyDataSetChanged();

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        ImageButton sendButton = (ImageButton) view
                .findViewById(R.id.sendMessageButton);
        sendButton.setOnClickListener(this);

        // ----Set autoscroll of listview when a new message arrives----//
        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);
        chatlist = new ArrayList<ChatMessage>();
        chatAdapter = new ChatAdapter(getActivity(), chatlist);
        msgListView.setAdapter(chatAdapter);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }


    public void sendTextMessage(View v) {
        //get message

        message = msg_edittext.getEditableText().toString();
        Toast.makeText(getActivity(), "sending message to " + getReceiver(), Toast.LENGTH_LONG).show();
        if (!message.equalsIgnoreCase("")) {
            chatMessage = new ChatMessage(sender, getReceiver(),
                    message, "" + random.nextInt(1000), true);
            chatMessage.setMsgID();
            chatMessage.body = message;
            chatMessage.Date = CommonMethods.getCurrentDate();
            chatMessage.Time = CommonMethods.getCurrentTime();
            msg_edittext.setText("");
            chatAdapter.add(chatMessage);
            chatAdapter.notifyDataSetChanged();
            MainActivity activity = ((MainActivity) getActivity());
            activity.getmService().xmpp.sendMessage(chatMessage);

        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendMessageButton:

                sendTextMessage(v);


        }
    }

}