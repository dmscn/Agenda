package br.com.damasceno.agenda.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import br.com.damasceno.agenda.constant.Constants;
import br.com.damasceno.agenda.database.AppDatabase;
import br.com.damasceno.agenda.helper.VolleyResponseListener;
import br.com.damasceno.agenda.model.Task;
import br.com.damasceno.agenda.util.SharedPreferencesUtils;
import br.com.damasceno.agenda.util.VolleyUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NewTaskActivity extends AppCompatActivity implements Constants, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.ed_title)
    EditText edTitle;

    @BindView(R.id.ed_description)
    EditText edDescription;

    @BindView(R.id.lay_label)
    LinearLayout label;

    @BindView(R.id.date)
    LinearLayout date;

    @BindView(R.id.tv_label)
    TextView tvLabel;

    @BindView(R.id.tv_date)
    TextView tvDate;

    Context context = NewTaskActivity.this;

    Date taskDate = new GregorianCalendar().getTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Defining IconicsLayoutInflater to enable automatic xml icons detection
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));

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


                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(NewTaskActivity.this);
                alertBuilder.setTitle("Label");
                alertBuilder.setItems(LABELS, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvLabel.setText(LABELS[which]);
                    }
                });

                alertBuilder.create().show();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar currentDate = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        // If it is Current Date then set text to "Today"
                        if(year == currentDate.get(Calendar.YEAR)
                                && month == currentDate.get(Calendar.MONTH)
                                && dayOfMonth == currentDate.get(Calendar.DAY_OF_MONTH)) {

                            tvDate.setText(getString(R.string.str_today));

                        } else {

                            tvDate.setText(new DateFormatSymbols().getMonths()[month] + " " + dayOfMonth + ",  " + year);

                        }

                        taskDate = new GregorianCalendar(year, month, dayOfMonth).getTime();

                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });
    }

    // Inflate Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;

    }

    // Handle Menu Buttons
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_save:
                performCreateTask();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void performCreateTask() {

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

        Task newTask = new Task();

        newTask.setTitle(edTitle.getText().toString());
        newTask.setLabel(tvLabel.getText().toString());
        newTask.setText(edDescription.getText().toString());
        newTask.setDate(dateFormat.format(taskDate));

        String userAccessToken = SharedPreferencesUtils.getUserAccessToken(context);

        // Saving in WebService
        VolleyUtils.requestAddNewTask(context, userAccessToken, newTask, new VolleyResponseListener<Task>() {

            @Override
            public void onResponse(@Nullable Task response) {

                final Task TASK = response;

                // Saving in Database
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase.getInstance(context).taskDAO().insertTask(TASK);
                    }
                });

            }

            @Override
            public void onError(@Nullable String error) {

            }
        });

        finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}
