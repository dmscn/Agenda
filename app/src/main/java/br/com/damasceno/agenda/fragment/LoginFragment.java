package br.com.damasceno.agenda.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import br.com.damasceno.agenda.activity.R;
import br.com.damasceno.agenda.constant.Constants;
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

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);


        return view;
    }

    @OnClick(R.id.btnLogin)
    public void attemptLogin() {

        // attempt login
    }
}
