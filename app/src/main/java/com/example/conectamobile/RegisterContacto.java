package com.example.conectamobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterContacto extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private EditText txtNombre, txtApellido, txtContrasenia, txtCorreo, txtUsuario;
    private Button btnRegistrarContacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_contacto);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        ImageButton volver = findViewById(R.id.btnVolver);
        txtNombre = findViewById(R.id.txtNombre);
        txtApellido = findViewById(R.id.txtApellido);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtContrasenia = findViewById(R.id.txtContrasenia);
        txtUsuario = findViewById(R.id.txtUsuario);
        btnRegistrarContacto = findViewById(R.id.btnRegistrar);

        btnRegistrarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = txtNombre.getText().toString().trim();
                String apellido = txtApellido.getText().toString().trim();
                String correo = txtCorreo.getText().toString().trim();
                String contrasenia = txtContrasenia.getText().toString().trim();
                String usuario = txtUsuario.getText().toString().trim();

                if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contrasenia.isEmpty() || usuario.isEmpty()) {
                    Toast.makeText(RegisterContacto.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // Obtener el último ID de usuario
                    DatabaseReference usuariosRef = databaseReference.child("Usuarios");
                    usuariosRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long ultimoID = 0;

                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                ultimoID = Long.parseLong(childSnapshot.getKey());
                            }

                            // Incrementar el último ID para obtener el nuevo ID
                            long nuevoID = ultimoID + 1;
                            String nuevoIDUsuario = String.valueOf(nuevoID);

                            // Crear un objeto Usuario con los datos obtenidos
                            Usuario nuevoUsuario = new Usuario(null, nombre, apellido, correo, contrasenia, usuario);

                            // Agregar el usuario a la base de datos bajo el nuevo ID
                            databaseReference.child("Usuarios").child(nuevoIDUsuario).setValue(nuevoUsuario);

                            Toast.makeText(RegisterContacto.this, "Contacto registrado exitosamente", Toast.LENGTH_SHORT).show();

                            // Limpiar los EditTexts después de agregar los datos
                            txtNombre.setText("");
                            txtApellido.setText("");
                            txtCorreo.setText("");
                            txtContrasenia.setText("");
                            txtUsuario.setText("");
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
        });
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}