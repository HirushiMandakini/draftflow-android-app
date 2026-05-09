package com.mandakini.draftflow.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mandakini.draftflow.R;
import com.mandakini.draftflow.activities.DraftDetailActivity;
import com.mandakini.draftflow.models.Draft;

import java.util.List;

public class DraftAdapter extends RecyclerView.Adapter<DraftAdapter.DraftViewHolder> {

    private final List<Draft> draftList;

    public DraftAdapter(List<Draft> draftList) {
        this.draftList = draftList;
    }

    @NonNull
    @Override
    public DraftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_draft, parent, false);
        return new DraftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DraftViewHolder holder, int position) {
        Draft draft = draftList.get(position);

        holder.txtDraftTitle.setText(draft.getTitle());
        holder.txtDraftContent.setText(draft.getContent());
        holder.txtDraftDate.setText("Updated: " + draft.getUpdatedAt());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DraftDetailActivity.class);
            intent.putExtra("draft_id", draft.getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return draftList.size();
    }

    static class DraftViewHolder extends RecyclerView.ViewHolder {

        TextView txtDraftTitle;
        TextView txtDraftContent;
        TextView txtDraftDate;

        public DraftViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDraftTitle = itemView.findViewById(R.id.txtDraftTitle);
            txtDraftContent = itemView.findViewById(R.id.txtDraftContent);
            txtDraftDate = itemView.findViewById(R.id.txtDraftDate);
        }
    }
}