package br.com.damasceno.agenda.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import br.com.damasceno.agenda.activity.R;
import br.com.damasceno.agenda.activity.WelcomeActivity;
import br.com.damasceno.agenda.constant.Constants;
import butterknife.BindView;
import butterknife.OnClick;

public class WelcomeFragment extends Fragment implements Constants {

    private Activity activity;

    @BindView(R.id.btnCallLogin) Button btnCallLogin;
    @BindView(R.id.btnCallLogin) Button btnCallRegister;

    public WelcomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            this.activity = (WelcomeActivity) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement fragment interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        return view;
    }

    @OnClick(R.id.btnCallLogin)
    public void callLoginFragment() {
        try {
            ((OnButtonClickListener) activity).OnButtonClicked(R.id.btnCallLogin);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btnCallRegister)
    public void callRegisterFragment() {
        try {
            ((OnButtonClickListener) activity).OnButtonClicked(R.id.btnCallRegister);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public interface OnButtonClickListener {
        public void OnButtonClicked(@IdRes int id);
    }

}
