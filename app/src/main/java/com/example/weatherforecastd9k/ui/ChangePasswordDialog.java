package com.example.weatherforecastd9k.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.weatherforecastd9k.R;
import com.google.android.material.textfield.TextInputEditText;

public class ChangePasswordDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        
        View view = inflater.inflate(R.layout.dialog_change_password, null);
        TextInputEditText oldPassword = view.findViewById(R.id.oldPassword);
        TextInputEditText newPassword = view.findViewById(R.id.newPassword);
        TextInputEditText confirmPassword = view.findViewById(R.id.confirmPassword);

        builder.setView(view)
                .setTitle("Change Password")
                .setPositiveButton("Confirm", (dialog, id) -> {
                    String oldPwd = oldPassword.getText().toString();
                    String newPwd = newPassword.getText().toString();
                    String confirmPwd = confirmPassword.getText().toString();

                    if (validatePasswords(oldPwd, newPwd, confirmPwd)) {
                        // TODO: Implement password change logic
                        Toast.makeText(requireContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        return builder.create();
    }

    private boolean validatePasswords(String oldPwd, String newPwd, String confirmPwd) {
        if (oldPwd.isEmpty() || newPwd.isEmpty() || confirmPwd.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all password fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!newPwd.equals(confirmPwd)) {
            Toast.makeText(requireContext(), "New password and confirmation do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        // TODO: Verify if the old password is correct
        return true;
    }
} 