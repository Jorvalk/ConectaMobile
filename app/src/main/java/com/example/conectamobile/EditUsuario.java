package com.example.conectamobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EditUsuario extends AppCompatActivity {
    private ImageView imgProfile;
    private EditText edtEmail, edtPassword;
    private Button btnChangePhoto, btnSaveChanges, btnSignOut;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_usuario);

        imgProfile = findViewById(R.id.imgProfile);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnChangePhoto = findViewById(R.id.btnChangePhoto);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnSignOut = findViewById(R.id.btnSignOut);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        loadUserData();

        btnChangePhoto.setOnClickListener(v -> chooseImage());
        btnSaveChanges.setOnClickListener(v -> saveUserData());
        btnSignOut.setOnClickListener(v -> signOut());

        ImageButton volver = findViewById(R.id.btnVolver2);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void loadUserData() {
        if (currentUser != null) {
            edtEmail.setText(currentUser.getEmail());
            if (currentUser.getPhotoUrl() != null) {
                Glide.with(this).load(currentUser.getPhotoUrl()).into(imgProfile);
            }
        }
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imgProfile.setImageURI(selectedImageUri); // Mostrar la imagen seleccionada
        }
    }

    private void saveUserData() {
        String newEmail = edtEmail.getText().toString();
        String newPassword = edtPassword.getText().toString();

        if (!newEmail.isEmpty()) {
            currentUser.updateEmail(newEmail).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Correo actualizado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error al actualizar el correo", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (!newPassword.isEmpty()) {
            currentUser.updatePassword(newPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (selectedImageUri != null) {
            StorageReference profileRef = storageReference.child("profileImages/" + currentUser.getUid() + ".jpg");
            profileRef.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot -> {
                profileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    updateProfilePhoto(uri);
                });
            });
        }
    }

    private void updateProfilePhoto(Uri photoUri) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(photoUri)
                .build();

        currentUser.updateProfile(profileUpdates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Foto de perfil actualizada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al actualizar la foto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signOut() {
        mAuth.signOut();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}
