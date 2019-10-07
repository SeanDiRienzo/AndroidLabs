package com.example.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Message> mMessages;

    public ChatAdapter(Context context, ArrayList<Message> messages) {
        super();
        this.mContext = context;
        this.mMessages = messages;

    }


    @Override
    public int getCount() {
        return mMessages.size();
    }

    @Override
    public Message getItem(int position) {
        return mMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = this.getItem(position);
        ViewHolder holder;
        holder = new ViewHolder();

        if (message.isMine) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.message_sent, parent, false);
            holder.message = convertView.findViewById(R.id.sendMessageText);
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.message_received, parent, false);
            holder.message = convertView.findViewById(R.id.receiveMessageText);

        }


        convertView.setTag(holder);
        holder.message.setText(message.getMessage());
        return convertView;


    }

    private static class ViewHolder {
        TextView message;
    }
}
