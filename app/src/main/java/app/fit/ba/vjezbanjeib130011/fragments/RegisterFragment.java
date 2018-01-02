package app.fit.ba.vjezbanjeib130011.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import app.fit.ba.vjezbanjeib130011.model.ClientUserVM;

public class RegisterFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        final EditText txtFirstName = (EditText) view.findViewById(R.id.txtFirstName);
        final EditText txtLastName = (EditText) view.findViewById(R.id.txtLastName);
        final EditText txtBirthDate = (EditText) view.findViewById(R.id.txtBirthDate);
        final EditText txtUsername = (EditText) view.findViewById(R.id.txtUsername);
        final EditText txtPassword = (EditText) view.findViewById(R.id.txtPassword);
        final EditText txtAddress = (EditText) view.findViewById(R.id.txtAddress);
        final EditText txtPhone = (EditText) view.findViewById(R.id.txtPhone);

        Button btnRegister = (Button) view.findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                do_btnRegister_click(txtFirstName, txtLastName, txtBirthDate, txtUsername, txtPassword, txtAddress, txtPhone);
            }
        });

        return view;
    }

    private void do_btnRegister_click(EditText txtFirstName, EditText txtLastName, EditText txtBirthDate, EditText txtUsername, EditText txtPassword, EditText txtAddress, EditText txtPhone)
    {
        String firstName = txtFirstName.getText().toString();
        String lastName = txtLastName.getText().toString();
        String strBirthDate = txtBirthDate.getText().toString();
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        String address = txtAddress.getText().toString();
        String phone = txtPhone.getText().toString();

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Registration");
        dialog.setMessage("In progress...");
        dialog.show();

        int errorCounter = 0;

        if (InputValidationHelper.isNullOrEmpty(firstName)) {
            errorCounter++;
            dialog.dismiss();
            txtFirstName.requestFocus();
            txtFirstName.setError("Required field!");
        }

        if (InputValidationHelper.isNullOrEmpty(lastName))
        {
            errorCounter++;
            dialog.dismiss();
            txtLastName.requestFocus();
            txtLastName.setError("Required field!");
        }

        if (InputValidationHelper.isNullOrEmpty(strBirthDate))
        {
            errorCounter++;
            dialog.dismiss();
            txtBirthDate.requestFocus();
            txtBirthDate.setError("Required field!");
        }
        else if(!InputValidationHelper.isValidDate(strBirthDate))
        {
            errorCounter++;
            dialog.dismiss();
            txtBirthDate.requestFocus();
            txtBirthDate.setError("Invalid format!");
        }

        if (InputValidationHelper.isNullOrEmpty(username))
        {
            errorCounter++;
            dialog.dismiss();
            txtUsername.requestFocus();
            txtUsername.setError("Required field!");
        }

        if (InputValidationHelper.isNullOrEmpty(password))
        {
            errorCounter++;
            dialog.dismiss();
            txtPassword.requestFocus();
            txtPassword.setError("Required field!");
        }

        if (InputValidationHelper.isNullOrEmpty(address))
        {
            errorCounter++;
            dialog.dismiss();
            txtAddress.requestFocus();
            txtAddress.setError("Required field!");
        }

        if (InputValidationHelper.isNullOrEmpty(phone))
        {
            errorCounter++;
            dialog.dismiss();
            txtPhone.requestFocus();
            txtPhone.setError("Required field!");
        }
        else if(!InputValidationHelper.isValidPhone(phone))
        {
            errorCounter++;
            dialog.dismiss();
            txtPhone.requestFocus();
            txtPhone.setError("Invalid format!");
        }

        if(errorCounter == 0)
        {
            ClientUserVM model = new ClientUserVM();
            model.FirstName = firstName;
            model.LastName = lastName;
            model.BirthDate = strBirthDate;
            model.Username = username;
            model.Password = password;
            model.Address = address;
            model.Phone = phone;

            UsersApi.Registration(model, new MyRunnable<UsersApi.RegistrationStatusMessageVM>() {
                @Override
                public void run(UsersApi.RegistrationStatusMessageVM successMessageVM) {
                    if(successMessageVM.successMessage != null)
                    {
                        dialog.dismiss();
                        Toast.makeText(AppContext.getContext(), successMessageVM.successMessage, Toast.LENGTH_LONG).show();

                        //refresh activity
                        Intent intent = getActivity().getIntent();
                        getActivity().finish();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        getActivity().overridePendingTransition(0,0); //to override animation and just refresh activity
                        startActivity(intent);
                        getActivity().overridePendingTransition(0,0);
                    }
                    else if(successMessageVM.errorMessage != null){
                        dialog.dismiss();
                        Toast.makeText(AppContext.getContext(), successMessageVM.errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}