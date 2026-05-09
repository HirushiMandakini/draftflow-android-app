package com.mandakini.draftflow.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mandakini.draftflow.R;
import com.mandakini.draftflow.models.Draft;

import java.util.ArrayList;
import java.util.List;

public class BulkDraftAdapter extends RecyclerView.Adapter<BulkDraftAdapter.BulkDraftViewHolder> {

    public interface OnSelectionChangedListener {
        void onSelectionChanged(int selectedCount);
    }

    private final List<Draft> draftList;
    private final List<Integer> selectedIds = new ArrayList<>();
    private final OnSelectionChangedListener listener;

    public BulkDraftAdapter(List<Draft> draftList, OnSelectionChangedListener listener) {
        this.draftList = draftList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BulkDraftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bulk_draft, parent, false);
        return new BulkDraftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BulkDraftViewHolder holder, int position) {
        Draft draft = draftList.get(position);

        holder.txtDraftTitle.setText(draft.getTitle());
        holder.txtDraftContent.setText(draft.getContent());

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(selectedIds.contains(draft.getId()));

        holder.itemView.setOnClickListener(v -> toggleSelection(draft.getId()));
        holder.checkBox.setOnClickListener(v -> toggleSelection(draft.getId()));
    }

    private void toggleSelection(int draftId) {
        if (selectedIds.contains(draftId)) {
            selectedIds.remove(Integer.valueOf(draftId));
        } else {
            selectedIds.add(draftId);
        }

        notifyDataSetChanged();

        if (listener != null) {
            listener.onSelectionChanged(selectedIds.size());
        }
    }

    public List<Integer> getSelectedIds() {
        return selectedIds;
    }

    @Override
    public int getItemCount() {
        return draftList.size();
    }

    static class BulkDraftViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        TextView txtDraftTitle, txtDraftContent;

        public BulkDraftViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkBoxDraft);
            txtDraftTitle = itemView.findViewById(R.id.txtDraftTitle);
            txtDraftContent = itemView.findViewById(R.id.txtDraftContent);
        }
    }
}