package br.com.damasceno.agenda.view.ui.fragment;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import br.com.damasceno.agenda.constant.Constants;
import br.com.damasceno.agenda.util.ToastUtils;
import br.com.damasceno.agenda.view.ui.activity.MainActivity;
import br.com.damasceno.agenda.view.ui.activity.R;
import br.com.damasceno.agenda.viewmodel.LoginViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment implements Constants {

    View view;

    @BindView(R.id.input_container)
    LinearLayout mInputContainer;

    @BindView(R.id.ed_username)
    @NotEmpty
    @Email(messageResId = R.string.msg_error_email_format)
    EditText mEdUsername;

    @BindView(R.id.ed_password)
    @NotEmpty
    @Password
    EditText mEdPassword;

    @BindView(R.id.btnLogin)
    Button mBtnLogin;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    LoginViewModel mViewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel =
                ViewModelProviders.of(this).get(LoginViewModel.class);
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.btnLogin)
    public void attemptLogin() {

        // Hide Inputs
        mInputContainer.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        hideKeyboard(view);

        String username = mEdUsername.getText().toString();
        String password = mEdPassword.getText().toString();

        mViewModel.login(username, password, new Response.Listener<Integer>() {

            @Override
            public void onResponse(Integer response) {

                switch (response) {

                    case RESPONSE_SUCCESS:

                        ToastUtils.toast(getActivity(), getString(R.string.msg_success));

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();

                        break;

                    case RESPONSE_INVALID_CREDENTIALS:

                        ToastUtils.toast(getActivity(), getString(R.string.msg_username_password_incorrect));

                        break;

                    default:
                        ToastUtils.toast(getActivity(), getString(R.string.msg_error_timeout));
                }

                // Show Inputs
                mInputContainer.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }

        });
    }

    // Hides the keyboard
    public void hideKeyboard(View view) {

        InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
