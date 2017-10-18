package br.com.damasceno.agenda.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import br.com.damasceno.agenda.activity.R;
import butterknife.BindView;
import butterknife.OnClick;

public class RegisterFragment extends Fragment {

    @BindView(R.id.edNome)
    @NotEmpty
    EditText edNome;

    @BindView(R.id.edEmail)
    @NotEmpty
    @Email(messageResId = R.string.msg_error_email_format)
    EditText edEmail;

    @BindView(R.id.edPhone)
    @NotEmpty
    EditText edPhone;

    @BindView(R.id.edPassword)
    @NotEmpty
    @Password(min = 6, messageResId = R.string.msg_error_password_min_lenght)
    EditText edPassword;

    @BindView(R.id.btnRegister) EditText btnRegister;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        return view;
    }

    @OnClick(R.id.btnRegister)
    public void attemptRegister() {

        // attemptRegister
    }

}
