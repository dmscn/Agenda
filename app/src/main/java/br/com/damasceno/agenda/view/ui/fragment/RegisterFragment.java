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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import br.com.damasceno.agenda.constant.Constants;
import br.com.damasceno.agenda.service.model.User;
import br.com.damasceno.agenda.util.ToastUtils;
import br.com.damasceno.agenda.view.ui.activity.MainActivity;
import br.com.damasceno.agenda.view.ui.activity.R;
import br.com.damasceno.agenda.viewmodel.LoginViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterFragment extends Fragment implements Constants {

    View view;

    @BindView(R.id.ed_name)
    EditText mEdName;

    @BindView(R.id.ed_email)
    EditText mEdEmail;

    @BindView(R.id.ed_password)
    EditText mEdPassword;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.input_container)
    LinearLayout mInputContainer;

    LoginViewModel mViewModel;
    User user;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel =
                ViewModelProviders.of(this).get(LoginViewModel.class);
    }

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
        user.setEmail(mEdEmail.getText().toString());
        user.setName(mEdName.getText().toString());
        user.setPassword(mEdPassword.getText().toString());

        // Hiding inputs
        mInputContainer.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        hideKeyboard(view);

        mViewModel.register(user, response -> {
            switch (response) {

                case RESPONSE_SUCCESS:

                    ToastUtils.toast(getActivity(), getString(R.string.msg_success));

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                    break;

                case RESPONSE_INVALID_FIELDS:

                    ToastUtils.toast(getActivity(), getString(R.string.msg_error_invalid_fields));
                    break;

                case RESPONSE_EMAIL_ALREADY_EXISTS:

                    ToastUtils.toast(getActivity(), getString(R.string.msg_error_email_already_exists));
                    break;

                default:

                    ToastUtils.toast(getActivity(), getString(R.string.msg_error_timeout));
            }

            // Showing back the Inputs
            mInputContainer.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);

        });
    }

    // Hides the keyboard
    public void hideKeyboard(View view) {

        InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
