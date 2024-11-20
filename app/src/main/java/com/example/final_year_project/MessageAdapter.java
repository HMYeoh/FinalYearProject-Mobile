// MessageAdapter.java
package com.example.final_year_project;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Date;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<MessageModel> messages;

    public MessageAdapter(List<MessageModel> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageModel message = messages.get(position);
        holder.subjectTextView.setText(message.getSubject());

        // Format the createdAt timestamp
        if (message.getCreatedAt() != null) {
            Timestamp timestamp = message.getCreatedAt();
            Date date = timestamp.toDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
            String formattedDate = dateFormat.format(date);
            holder.createdAtView.setText(formattedDate);
        } else {
            holder.createdAtView.setText("Unknown date");
        }

        // Set an OnClickListener for the item view
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MessageDetail.class);
            intent.putExtra("subject", message.getSubject());
            intent.putExtra("createdAt", holder.createdAtView.getText().toString());
            intent.putExtra("message", message.getMessage());
            intent.putExtra("originalMessage", message.getOriginalMessage());
            intent.putExtra("username", message.getUsername());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView subjectTextView;
        TextView createdAtView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectTextView = itemView.findViewById(R.id.subjectTextView);
            createdAtView = itemView.findViewById(R.id.createdAtView);
        }
    }
}
