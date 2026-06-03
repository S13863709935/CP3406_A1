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
                .setTitle("修改密码")
                .setPositiveButton("确定", (dialog, id) -> {
                    String oldPwd = oldPassword.getText().toString();
                    String newPwd = newPassword.getText().toString();
                    String confirmPwd = confirmPassword.getText().toString();

                    if (validatePasswords(oldPwd, newPwd, confirmPwd)) {
                        // TODO: 实现密码修改逻辑
                        Toast.makeText(requireContext(), "密码修改成功", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", (dialog, id) -> dialog.cancel());

        return builder.create();
    }

    private boolean validatePasswords(String oldPwd, String newPwd, String confirmPwd) {
        if (oldPwd.isEmpty() || newPwd.isEmpty() || confirmPwd.isEmpty()) {
            Toast.makeText(requireContext(), "请填写所有密码字段", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!newPwd.equals(confirmPwd)) {
            Toast.makeText(requireContext(), "新密码与确认密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        }
        // TODO: 验证旧密码是否正确
        return true;
    }
} 