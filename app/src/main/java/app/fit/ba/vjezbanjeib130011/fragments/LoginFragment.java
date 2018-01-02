package app.fit.ba.vjezbanjeib130011.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import app.fit.ba.vjezbanjeib130011.R;
import app.fit.ba.vjezbanjeib130011.api.UsersApi;
import app.fit.ba.vjezbanjeib130011.helper.AppContext;
import app.fit.ba.vjezbanjeib130011.helper.InputValidationHelper;
import app.fit.ba.vjezbanjeib130011.helper.MyRunnable;
import app.fit.ba.vjezbanjeib130011.helper.Session;
import app.fit.ba.vjezbanjeib130011.model.ClientUserVM;


public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button btnSignIn = (Button) view.findViewById(R.id.btnSignIn);
        Button btnRegister = (Button) view.findViewById(R.id.btnRegister);
        Button btnExit = (Button) view.findViewById(R.id.btnExitApp);

        final EditText txtUsername = (EditText) view.findViewById(R.id.txtUsername);
        final EditText txtPassword = (EditText) view.findViewById(R.id.txtPassword);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                do_btnLogin_click(txtUsername, txtPassword);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                do_btnRegisterClick();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AppContext.getContext(), "See you soon!", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

        return view;
    }

    private void do_btnRegisterClick()
    {
        Fragment fragment = new RegisterFragment();
        openFragmentAsReplace(fragment);
    }

    private void do_btnLogin_click(EditText txtUsername, EditText txtPassword)
    {
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Data access");
        dialog.setMessage("In progress...");
        dialog.show();

        if(InputValidationHelper.isNullOrEmpty(username))
        {
            dialog.dismiss();
            txtUsername.requestFocus();
            txtUsername.setError("Required field!");
        }

        if (InputValidationHelper.isNullOrEmpty(password))
        {
            dialog.dismiss();
            txtPassword.requestFocus();
            txtPassword.setError("Required field!");
        }

        if(username.length() != 0 && password.length() != 0)
        {
            UsersApi.Authentication(username, password, new MyRunnable<ClientUserVM>() {
                public void run(ClientUserVM result) {
                    if (result != null) {
                        dialog.dismiss();
                        Toast.makeText(AppContext.getContext(), "Welcome " + result.FirstName + " " + result.LastName, Toast.LENGTH_LONG).show();
                        Session.setLoggedUser(result);

                        //refresh activity
                        Intent intent = getActivity().getIntent();
                        getActivity().finish();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        getActivity().overridePendingTransition(0,0); //to override animation and just refresh activity
                        startActivity(intent);
                        getActivity().overridePendingTransition(0,0);
                    } else {
                        dialog.dismiss();
                        Toast.makeText(AppContext.getContext(), "Wrong username and/or password!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void openFragmentAsReplace(Fragment fragment)
    {
        final FragmentManager fm = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentPlace, fragment).addToBackStack("appFragments");
        fragmentTransaction.commit();
    }
}
