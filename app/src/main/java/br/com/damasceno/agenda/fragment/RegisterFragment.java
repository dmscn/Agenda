package br.com.damasceno.agenda.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import br.com.damasceno.agenda.activity.MainActivity;
import br.com.damasceno.agenda.activity.R;
import br.com.damasceno.agenda.constant.Constants;
import br.com.damasceno.agenda.model.User;
import br.com.damasceno.agenda.util.SharedPreferencesUtils;
import br.com.damasceno.agenda.util.ToastUtils;
import br.com.damasceno.agenda.util.VolleyResponseListener;
import br.com.damasceno.agenda.util.VolleyUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterFragment extends Fragment implements Constants {

    View view;

    @BindView(R.id.ed_name)
    EditText edName;

    @BindView(R.id.ed_email)
    EditText edEmail;

    @BindView(R.id.ed_password)
    EditText edPassword;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.input_container)
    LinearLayout inputContainer;

    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_register, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.btn_register)
    public void attemptRegister() {

        // Filling the User with input values
        user = new User();
        user.setEmail(edEmail.getText().toString());
        user.setName(edName.getText().toString());
        user.setPassword(edPassword.getText().toString());

        // Hiding inputs
        inputContainer.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        hideKeyboard(view);

        VolleyUtils.requestRegister(getActivity(), user, new VolleyResponseListener() {

            @Override
            public void onResponse(@Nullable JSONObject response) {

                ToastUtils.toast(getActivity(), getString(R.string.msg_success));

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onError(@Nullable String statusCode) {

                switch (statusCode) {

                    case "400":
                        ToastUtils.toast(getActivity(), getString(R.string.msg_error_invalid_fields));
                        break;

                    case "409":
                        ToastUtils.toast(getActivity(), getString(R.string.msg_error_email_already_exists));
                        break;

                    default:
                        Log.i(TAG_LOG, "Status Code : " + statusCode);
                }

                // Showing back the Inputs
                inputContainer.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    // Hides the keyboard
    public void hideKeyboard(View view) {
        InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
