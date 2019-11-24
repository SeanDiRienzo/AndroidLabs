package com.example.androidlabs;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class MessageFragment extends Fragment {

    private boolean isTablet;
    private Bundle dataFromActivity;
    private long id;
    private String message;
    private int position;
    private TextView messageView;
    private TextView idView;

    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        this.id = dataFromActivity.getLong("id");
        this.message = dataFromActivity.getString("message");
        this.position = dataFromActivity.getInt("position");


        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_message, container, false);

        //show the message
        messageView = (TextView) result.findViewById(R.id.message_textView);
        messageView.setText(this.message);

        //show the id:
        idView = (TextView) result.findViewById(R.id.id_textView);
        idView.setText("" + this.id);

        // get the delete button, and add a click listener:
        Button deleteButton = (Button) result.findViewById(R.id.deletebutton);
        deleteButton.setOnClickListener(clk -> {

            if (isTablet) { //both the list and details are on the screen:
                ChatRoomActivity parent = (ChatRoomActivity) getActivity();
                parent.deleteMessageId(id, position); //this deletes the item and updates the list


                //now remove the fragment since you deleted it from the database:
                // this is the object to be removed, so remove(this):
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            //for Phone:
            else //You are only looking at the details, you need to go back to the previous list page
            {
                MessageContainer parent = (MessageContainer) getActivity();
                Intent backToFragmentExample = new Intent();
                backToFragmentExample.putExtra("id", this.id);

                parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back
            }
        });
        return result;
    }
}
