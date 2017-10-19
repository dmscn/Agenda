package br.com.damasceno.agenda.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import br.com.damasceno.agenda.activity.R;
import br.com.damasceno.agenda.constant.Constants;
import br.com.damasceno.agenda.model.User;
import br.com.damasceno.agenda.util.SharedPreferencesUtil;
import br.com.damasceno.agenda.util.ToastUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterFragment extends Fragment implements Constants {

    @BindView(R.id.edName)
    @NotEmpty
    EditText edName;

    @BindView(R.id.edEmail)
    @NotEmpty
    @Email(messageResId = R.string.msg_error_email_format)
    EditText edEmail;

    @BindView(R.id.edPassword)
    @NotEmpty
    @Password(min = 6, messageResId = R.string.msg_error_password_min_lenght)
    EditText edPassword;

    // Volley Attributes
    private RequestQueue requestQueue;
    private JsonObjectRequest jsonRequest;

    User user;

    @BindView(R.id.btnRegister)
    Button btnRegister;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.btnRegister)
    public void attemptRegister() {
        user.setEmail(edEmail.getText().toString());
        user.setName(edName.getText().toString());
        sendNewUserRequest();
    }

    public void sendNewUserRequest() {

        String url = URL_BASE + URL_USER;

        // Data Params to JSON
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("access_token", WEB_SERVER_MASTER_KEY);
        params.put("email", edEmail.getText().toString());
        params.put("password", edPassword.getText().toString());
        params.put("name", edName.getText().toString());

        requestQueue = Volley.newRequestQueue(getActivity());

        jsonRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String jsonUser = response.getJSONObject("user").toString();

                            ObjectMapper mapper = new ObjectMapper();
                            user = mapper.readValue(jsonUser, User.class);

                            String credentialsToken = "Basic " + Base64.encodeToString((edEmail.getText().toString() + ":" + edPassword.getText().toString()).getBytes(), Base64.DEFAULT);

                            SharedPreferencesUtil.setPreferenciaString(getActivity(), PREF_USER, KEY_CREDENTIALS_TOKEN, credentialsToken);
                            SharedPreferencesUtil.setPreferenciaString(getActivity(), PREF_USER, KEY_USER_JSON, jsonUser);

                            // TODO: Redirect User

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        ToastUtil.toast(getActivity(), getString(R.string.msg_success));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtil.toast(getActivity(), getString(R.string.msg_error));
                    }
                });
    }

}
