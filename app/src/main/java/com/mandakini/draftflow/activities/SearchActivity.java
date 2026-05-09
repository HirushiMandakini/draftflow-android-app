package com.mandakini.draftflow.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mandakini.draftflow.R;
import com.mandakini.draftflow.adapters.DraftAdapter;
import com.mandakini.draftflow.database.DraftDao;
import com.mandakini.draftflow.models.Draft;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private TextView btnBack;
    private EditText edtSearch;
    private RecyclerView recyclerSearchResults;

    private DraftDao draftDao;
    private DraftAdapter draftAdapter;
    private List<Draft> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        draftDao = new DraftDao(this);

        btnBack = findViewById(R.id.btnBack);
        edtSearch = findViewById(R.id.edtSearch);
        recyclerSearchResults = findViewById(R.id.recyclerSearchResults);

        resultList = new ArrayList<>();
        draftAdapter = new DraftAdapter(resultList);

        recyclerSearchResults.setLayoutManager(new LinearLayoutManager(this));
        recyclerSearchResults.setAdapter(draftAdapter);

        btnBack.setOnClickListener(v -> finish());

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchDrafts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void searchDrafts(String query) {
        resultList.clear();

        if (!query.trim().isEmpty()) {
            resultList.addAll(draftDao.searchDrafts(query.trim()));
        }

        draftAdapter.notifyDataSetChanged();
    }
}