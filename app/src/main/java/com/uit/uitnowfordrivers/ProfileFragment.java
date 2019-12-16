package com.uit.uitnowfordrivers;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    CircleImageView ivAvatar;
    TextView tvEmail;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseFirestore db;
    Button btnUpdate, btnChangePass, btnSignOut;
    EditText txtName, txtPhone;
    Dialog dialog;

    public ProfileFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        db = FirebaseFirestore.getInstance();
        btnChangePass = view.findViewById(R.id.btnChangePassword);
        btnChangePass.setOnClickListener(this);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);
        btnSignOut = view.findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(this);
        ivAvatar = view.findViewById(R.id.ivAvatar);
        ivAvatar.setOnClickListener(this);
        tvEmail = view.findViewById(R.id.tvEmail);
        txtName = view.findViewById(R.id.tvName);
        txtPhone = view.findViewById(R.id.tvPhone);
        txtName.setText(PrefUtil.loadPref(getActivity(), "name"));
        txtPhone.setText(PrefUtil.loadPref(getActivity(), "phone"));
        tvEmail.setText(PrefUtil.loadPref(getActivity(), "email"));
        String photoPath = PrefUtil.loadPref(getActivity(), "photo");
        if (!photoPath.isEmpty())
            Picasso.get().load(PrefUtil.loadPref(getActivity(), "photo")).into(ivAvatar);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String photoPath = PrefUtil.loadPref(getActivity(), "photo");
        if (!photoPath.isEmpty())
            Picasso.get().load(PrefUtil.loadPref(getActivity(), "photo")).into(ivAvatar);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnSignOut) {
            // Đăng xuất
            FirebaseAuth.getInstance().signOut();
            PrefUtil.clearPref(getActivity(), "email");
            PrefUtil.clearPref(getActivity(), "id");
            PrefUtil.clearPref(getActivity(), "photo");
            PrefUtil.clearPref(getActivity(), "name");
            PrefUtil.clearPref(getActivity(), "phone");
            Intent intent = new Intent(getActivity(), SignInActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            Toast.makeText(getActivity(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();
        } else {
            if (id == R.id.btnUpdate) {
                final String name = txtName.getText().toString();
                final String phone = txtPhone.getText().toString();
                Map<String, Object> data = new HashMap<>();
                data.put("name", name);
                data.put("phone", phone);
                DocumentReference docRef = db.collection("Drivers").document(PrefUtil.loadPref(getActivity(), "id"));
// cập nhật field “capital” của document “DC”
                docRef
                        .update(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Đã cập nhật", Toast.LENGTH_SHORT).show();
                                PrefUtil.savePref(getActivity(), "name", name);
                                PrefUtil.savePref(getActivity(), "phone", phone);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                if (id == R.id.btnChangePassword) {
                    showChangePasswordDialog();
                }
            }
        }

    }

    private void showChangePasswordDialog() {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_changepass);
        final EditText txtOldPasswordDialog = dialog.findViewById(R.id.txtOldPassword);
        final EditText txtNewPasswordDialog = dialog.findViewById(R.id.txtNewPassword);
        final EditText txtNewPasswordConfirmDialog = dialog.findViewById(R.id.txtNewPasswordConfirm);
        final Button btnOK = dialog.findViewById(R.id.btnOK);
        final Button btnCancel = dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = txtOldPasswordDialog.getText().toString();
                String newPass = txtNewPasswordDialog.getText().toString();
                String newPassconfirm = txtNewPasswordConfirmDialog.getText().toString();
                if (newPass.equals(newPassconfirm))
                    changePass(oldPass, newPass);
                else
                    Toast.makeText(getActivity(), "Password Confirm Fail", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void changePass(String oldPass, final String newPass) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPass);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.e("Test", "Update Password Successful");
                                Toast.makeText(getActivity(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            } else
                                Log.e("Test", "Update Password Failed");
                        }
                    });
                } else
                    Log.e("Test", "Old Password Wrong");
            }
        });
    }

}
