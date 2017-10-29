package br.com.damasceno.agenda.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewTaskActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.ed_task)
    EditText edTask;

    @BindView(R.id.ed_description)
    EditText edDescription;

    @BindView(R.id.label)
    LinearLayout label;

    @BindView(R.id.date)
    LinearLayout date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        ButterKnife.bind(this);

        /*
         *  TOOLBAR
         */
        toolbar.setTitle(getString(R.string.str_new_task));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        // Handle clicks in Label and Date
        label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // popup list of options to label
                // default is regular tasks
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // popup calendar
                // default is current date
            }
        });
    }

    // Handle Back Button in Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
