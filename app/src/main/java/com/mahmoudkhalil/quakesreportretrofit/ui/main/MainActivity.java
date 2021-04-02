package com.mahmoudkhalil.quakesreportretrofit.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.mahmoudkhalil.quakesreportretrofit.Logic;
import com.mahmoudkhalil.quakesreportretrofit.R;
import com.mahmoudkhalil.quakesreportretrofit.data.UsgsApi;
import com.mahmoudkhalil.quakesreportretrofit.models.Earthquake;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView quakesRecyclerView;
    private QuakeListAdapter quakeListAdapter;
    private ProgressBar progressBar;
    private TextView emptyText;
    private QuakesViewModel quakesViewModel;
    private int minMag = 5;
    private String orderBy = "time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        if(!Logic.isNetworkConnected(this)) {
            showSnackbar();
        } else {
            quakesViewModel.getQuakes(getString(R.string.api_format), minMag, 100, orderBy);
        }

        quakesViewModel.getQuakesMutableLiveData().observe(this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {
                List<Earthquake> earthquakeList = Logic.extractEarthQuakes(jsonObject.toString());
                quakeListAdapter.setQuakeList(earthquakeList);
                progressBar.setVisibility(View.GONE);
                if(earthquakeList.isEmpty()) {
                    emptyText.setText(R.string.no_earthquakes);
                }

                quakeListAdapter.setOnItemClickListener(new QuakeListAdapter.onItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(earthquakeList.get(position).getUrl()));
                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.filter) {
            final Dialog filterDialog = new Dialog(this);
            filterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            filterDialog.setContentView(R.layout.filter_dialog);
            filterDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            filterDialog.setCancelable(false);
            Button okButton = filterDialog.findViewById(R.id.ok);
            Button cancelButton = filterDialog.findViewById(R.id.cancel);
            final RadioButton magRadioButton = filterDialog.findViewById(R.id.magnitude_rb);
            final RadioButton mostRecentRadioButton = filterDialog.findViewById(R.id.mostRecent_rb);
            final EditText minMagEditText = filterDialog.findViewById(R.id.pageSize_EditText);

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(minMagEditText.getText().toString().isEmpty()) {
                        minMag = 5;
                    } else {
                        minMag = Integer.parseInt(minMagEditText.getText().toString());
                    }
                    if(magRadioButton.isChecked()) {
                        orderBy = getString(R.string.filter_mag);
                    } else if(mostRecentRadioButton.isChecked()){
                        orderBy = getString(R.string.filter_time);
                    }
                    quakesViewModel.getQuakes(getString(R.string.api_format), minMag, 100, orderBy);
                    filterDialog.dismiss();
                    progressBar.setVisibility(View.VISIBLE);
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    filterDialog.dismiss();
                }
            });

            filterDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        quakesRecyclerView = findViewById(R.id.list);
        progressBar = findViewById(R.id.loading_indicator);
        emptyText = findViewById(R.id.empty_view);
        quakeListAdapter = new QuakeListAdapter(this);
        quakesRecyclerView.setAdapter(quakeListAdapter);
        quakesViewModel = new ViewModelProvider(this).get(QuakesViewModel.class);
    }

    private void showSnackbar() {
        View parentView = findViewById(android.R.id.content);
        Snackbar.make(parentView, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("Exit", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).setActionTextColor(getResources().getColor(R.color.magnitude8)).show();
    }
}