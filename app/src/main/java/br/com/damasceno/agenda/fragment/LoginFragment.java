package br.com.damasceno.agenda.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.com.damasceno.agenda.activity.MainActivity;
import br.com.damasceno.agenda.activity.R;
import br.com.damasceno.agenda.constant.Constants;
import br.com.damasceno.agenda.model.User;
import br.com.damasceno.agenda.util.SharedPreferencesUtil;
import br.com.damasceno.agenda.util.ToastUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment implements Constants {

    @BindView(R.id.edUsername)
    @NotEmpty
    @Email(messageResId = R.string.msg_error_email_format)
    EditText edUsername;

    @BindView(R.id.edPassword)
    @NotEmpty
    @Password
    EditText edPassword;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    // Volley Attributes
    private RequestQueue requestQueue;
    private JsonObjectRequest jsonRequest;

    User user;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.btnLogin)
    public void attemptLogin() {
        sendRequestLogin();
    }

    private void sendRequestLogin() {

        // request url to login
        String url = URL_BASE + URL_AUTH;

        // Hide Inputs
        edUsername.setVisibility(View.GONE);
        edPassword.setVisibility(View.GONE);
        btnLogin.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);


        // Data Params to JSON
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("access_token", WEB_SERVER_MASTER_KEY);

        requestQueue = Volley.newRequestQueue(getActivity());

        // Attempting Request
        jsonRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    user = new User();

                    // Save User Token
                    user.setToken(response.get("token").toString());

                    // JSON String User
                    String jsonUser = response.getJSONObject("user").toString();

                    // Mapping the JSON and filling the User
                    ObjectMapper mapper = new ObjectMapper();
                    user = mapper.readValue(jsonUser, User.class);

                    // Generating the credentials token
                    String credentialsToken = "Basic " + Base64.encodeToString((edUsername.getText().toString() + ":" + edPassword.getText().toString()).getBytes(), Base64.DEFAULT);

                    // Storing the credentials
                    SharedPreferencesUtil.storeCredentials(getActivity().getApplicationContext(), credentialsToken);

                    // Redirecting to Main Activity
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                ToastUtil.toast(getActivity(), getString(R.string.msg_success));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ToastUtil.toast(getActivity(), getString(R.string.msg_username_password_incorrect));
                Log.i(TAG_LOG, "Error : " + error.toString());

                // Show Inputs
                edUsername.setVisibility(View.VISIBLE);
                edUsername.setText("");
                edPassword.setVisibility(View.VISIBLE);
                edPassword.setText("");
                btnLogin.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }) {

            // Setting up a new Header
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String,String> params = new HashMap<String, String>();

                String encoded = "Basic " + Base64.encodeToString((edUsername.getText().toString() + ":" + edPassword.getText().toString()).getBytes(), Base64.DEFAULT);
                params.put("Authorization", encoded);

                return params;
            }
        };

        jsonRequest.setTag(TAG_REQUEST_LOGIN);
        requestQueue.add(jsonRequest);
    }

    @Override
    public void onStop () {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG_REQUEST_LOGIN);
        }
    }
}
