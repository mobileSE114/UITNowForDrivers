package com.uit.uitnowfordrivers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int LEFT_VIEW = 1;
    private static final int RIGHT_VIEW = 2;

    public class LeftViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;

        public LeftViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }

    public class RightViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;

        public RightViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }

    private List<Message> messages;
    private String me;

    public MessageAdapter(List<Message> messages, String me) {
        this.messages = messages;
        this.me = me;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == LEFT_VIEW) {
            View view = inflater.inflate(R.layout.row_message_left, parent, false);
            return new LeftViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.row_message_right, parent, false);
            return new RightViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).senderId.equals(me))
            return RIGHT_VIEW;
        return LEFT_VIEW;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Message msg = messages.get(position);
        if (getItemViewType(position) == LEFT_VIEW) {
            ((LeftViewHolder) holder).tvMessage.setText(msg.message);
        } else {
            ((RightViewHolder) holder).tvMessage.setText(msg.message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}