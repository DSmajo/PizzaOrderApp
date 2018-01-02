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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import app.fit.ba.vjezbanjeib130011.R;
import app.fit.ba.vjezbanjeib130011.api.UsersApi;
import app.fit.ba.vjezbanjeib130011.helper.AppContext;
import app.fit.ba.vjezbanjeib130011.helper.DateDecimalConverterHelper;
import app.fit.ba.vjezbanjeib130011.helper.InputValidationHelper;
import app.fit.ba.vjezbanjeib130011.helper.MyRunnable;
import app.fit.ba.vjezbanjeib130011.helper.Session;
import app.fit.ba.vjezbanjeib130011.model.ClientUserVM;


public class UserProfileFragment extends Fragment {

    private ClientUserVM userProfile = Session.getLoggedUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        TextView tvFirstName = (TextView) view.findViewById(R.id.tvFirstName);
        TextView tvBirthDate = (TextView) view.findViewById(R.id.tvBirthDate);
        TextView tvUsername = (TextView) view.findViewById(R.id.tvUsername);

        //String dtBirthDate = userProfile.BirthDate.substring(0, userProfile.BirthDate.length() - 7);

        tvFirstName.setText(userProfile.FirstName);
        tvBirthDate.setText(userProfile.BirthDate.substring(0, userProfile.BirthDate.length() - 7));
        tvUsername.setText(userProfile.Username);

        final EditText txtLastName = (EditText) view.findViewById(R.id.txtLastName);
        final EditText txtAddress = (EditText) view.findViewById(R.id.txtAddress);
        final EditText txtPhone = (EditText) view.findViewById(R.id.txtPhone);

        txtLastName.setText(userProfile.LastName);
        txtAddress.setText(userProfile.Address);
        txtPhone.setText(userProfile.Phone);

        Button btnUpdateProfile = (Button) view.findViewById(R.id.btnUpdateProfile);

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                do_btnUpdateProfile_click(txtLastName, txtAddress, txtPhone);
            }
        });

        return view;
    }

    private void do_btnUpdateProfile_click(EditText txtLastName, EditText txtAddress, EditText txtPhone)
    {
        String lastName = txtLastName.getText().toString();
        String address = txtAddress.getText().toString();
        String phone = txtPhone.getText().toString();

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Updating profile");
        dialog.setMessage("In progress...");
        dialog.show();

        int errorCounter = 0;


        if (InputValidationHelper.isNullOrEmpty(lastName)) {
            errorCounter++;
            dialog.dismiss();
            txtLastName.requestFocus();
            txtLastName.setError("Required field!");
        }

        if (InputValidationHelper.isNullOrEmpty(address)) {
            errorCounter++;
            dialog.dismiss();
            txtAddress.requestFocus();
            txtAddress.setError("Required field!");
        }

        if (InputValidationHelper.isNullOrEmpty(phone)) {
            errorCounter++;
            dialog.dismiss();
            txtPhone.requestFocus();
            txtPhone.setError("Required field!");
        } else if (!InputValidationHelper.isValidPhone(phone)) {
            errorCounter++;
            dialog.dismiss();
            txtPhone.requestFocus();
            txtPhone.setError("Invalid format!");
        }

        if (errorCounter == 0)
        {
            final ClientUserVM model = new ClientUserVM();

            model.ClientId = userProfile.ClientId;
            model.RegisterDate = userProfile.RegisterDate;

            model.UserId = userProfile.UserId;
            model.FirstName = userProfile.FirstName;
            model.LastName = lastName;
            model.BirthDate = userProfile.BirthDate;
            model.Username = userProfile.Username;
            model.Password = userProfile.Password;
            model.Address = address;
            model.Phone = phone;
            model.Active = userProfile.Active;

            UsersApi.UpdateUserProfile(model, new MyRunnable<UsersApi.UpdateProfileStatusMessageVM>() {
                @Override
                public void run(UsersApi.UpdateProfileStatusMessageVM successMessageVM) {
                    if (successMessageVM.successMessage != null)
                    {
                        dialog.dismiss();
                        Toast.makeText(AppContext.getContext(), successMessageVM.successMessage, Toast.LENGTH_LONG).show();

                        Session.setLoggedUser(model);

                        //refresh activity
                        Intent intent = getActivity().getIntent();
                        getActivity().finish();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        getActivity().overridePendingTransition(0, 0); //to override animation and just refresh activity
                        startActivity(intent);
                        getActivity().overridePendingTransition(0, 0);
                    }
                    else if (successMessageVM.errorMessage != null) {
                        dialog.dismiss();
                        Toast.makeText(AppContext.getContext(), successMessageVM.errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
