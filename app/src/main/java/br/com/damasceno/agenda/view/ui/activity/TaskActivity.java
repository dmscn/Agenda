package br.com.damasceno.agenda.view.ui.activity;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import br.com.damasceno.agenda.constant.Constants;
import br.com.damasceno.agenda.service.model.Task;
import br.com.damasceno.agenda.util.SharedPreferencesUtils;
import br.com.damasceno.agenda.viewmodel.TaskViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskActivity extends AppCompatActivity implements Constants {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ed_title)
    EditText mEdTitle;

    @BindView(R.id.ed_description)
    EditText mEdDescription;

    @BindView(R.id.lay_label)
    LinearLayout mLabel;

    @BindView(R.id.date)
    LinearLayout mDate;

    @BindView(R.id.tv_label)
    TextView mTvLabel;

    @BindView(R.id.tv_date)
    TextView mTvDate;

    @BindView(R.id.fab)
    FloatingActionButton mFab;

    TaskViewModel mViewModel;
    Context context = TaskActivity.this;
    Task mTask = null;
    Date taskDate = new GregorianCalendar().getTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Defining IconicsLayoutInflater to enable automatic xml icons detection
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);


        // Setting up ViewModel
        mViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

        /*
         *  TOOLBAR
         */
        mToolbar.setTitle(getString(R.string.str_new_task));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        // Handle clicks

        mLabel.setOnClickListener(v -> {


            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(TaskActivity.this);
            alertBuilder.setTitle("Label");
            alertBuilder.setItems(LABELS, (dialog, which) -> mTvLabel.setText(LABELS[which]));

            alertBuilder.create().show();
        });

        mDate.setOnClickListener(v -> {

            final Calendar currentDate = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {

                // If it is Current Date then set text to "Today"
                if(year == currentDate.get(Calendar.YEAR)
                        && month == currentDate.get(Calendar.MONTH)
                        && dayOfMonth == currentDate.get(Calendar.DAY_OF_MONTH)) {

                    mTvDate.setText(getString(R.string.str_today));

                } else {

                    mTvDate.setText(new DateFormatSymbols().getMonths()[month] + " " + dayOfMonth + ",  " + year);

                }

                taskDate = new GregorianCalendar(year, month, dayOfMonth).getTime();

            }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.show();
        });



        /*
         *  FAB
         */

        mFab.setImageDrawable(new IconicsDrawable(this, CommunityMaterial.Icon.cmd_check).color(getResources().getColor(R.color.colorWhite)).paddingDp(2));
        mFab.setOnClickListener(v -> {
            if(getIntent().hasExtra(PARAM_TASK)) {
                performUpdate();
            } else {
                performCreateTask();
            }
        });


        /*
         *  EDIT TASK
         */

        // Check if has extras
        if(getIntent().hasExtra(PARAM_TASK)) {

            // Setting field values

            mTask = (Task) getIntent().getSerializableExtra(PARAM_TASK);

            mEdTitle.setText(mTask.getTitle());
            mEdDescription.setText(mTask.getText());
            mTvLabel.setText(mTask.getLabel());
            mTvDate.setText(mTask.getDate());

        }

    }

    // Handle Menu Buttons
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void performCreateTask() {

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

        Task newTask = new Task();

        newTask.setTitle(mEdTitle.getText().toString());
        newTask.setLabel(mTvLabel.getText().toString());
        newTask.setText(mEdDescription.getText().toString());
        newTask.setDate(dateFormat.format(taskDate));

        String userAccessToken = SharedPreferencesUtils.getUserAccessToken(context);

        mViewModel.createTask(newTask);

        finish();
    }

    private void performUpdate() {
        
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

        mTask.setTitle(mEdTitle.getText().toString());
        mTask.setLabel(mTvLabel.getText().toString());
        mTask.setText(mEdDescription.getText().toString());
        mTask.setDate(dateFormat.format(taskDate));

        String userAccessToken = SharedPreferencesUtils.getUserAccessToken(context);

        mViewModel.updateTask(mTask);

        finish();

    }
}
