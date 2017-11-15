package br.com.damasceno.agenda.view.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import br.com.damasceno.agenda.view.ui.activity.MainActivity;
import br.com.damasceno.agenda.view.ui.activity.R;
import br.com.damasceno.agenda.constant.Constants;
import br.com.damasceno.agenda.util.ToastUtils;
import br.com.damasceno.agenda.service.repository.webservice.VolleyResponseListener;
import br.com.damasceno.agenda.service.repository.webservice.VolleyUtils;
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

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        mEdUsername.setCompoundDrawables( new IconicsDrawable(getActivity(), CommunityMaterial.Icon.cmd_account).color(getResources().getColor(R.color.colorWhite)),null ,null ,null);
        mEdPassword.setCompoundDrawables( new IconicsDrawable(getActivity(), CommunityMaterial.Icon.cmd_lock),null ,null ,null);

        return view;
    }



    @OnClick(R.id.btnLogin)
    public void attemptLogin() {

        // Hide Inputs
        mInputContainer.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        hideKeyboard(view);

        // Generating the credentials token
        String credentialsToken = "Basic " + Base64.encodeToString((mEdUsername.getText().toString() + ":" + mEdPassword.getText().toString()).getBytes(), Base64.DEFAULT);

        VolleyUtils.requestAuth(getActivity(), credentialsToken, new VolleyResponseListener() {
            @Override
            public void onResponse(@Nullable Object response) {

                ToastUtils.toast(getActivity(), getString(R.string.msg_success));

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();

            }

            @Override
            public void onError(@Nullable String error) {

                if(error != null) {

                    if(error.equals("401")) {
                        ToastUtils.toast(getActivity(), getString(R.string.msg_username_password_incorrect));
                    } else {
                        ToastUtils.toast(getActivity(), getString(R.string.msg_error_timeout));
                    }
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
