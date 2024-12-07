package com.example.conectamobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button registrarContacto;
    private EditText editTextUserId;
    private Button btnBuscarUsuario, btnListarUsuarios, btnEditar, btnEliminar;
    private TextView tvNombre, tvApellido, tvCorreo, tvContrasenia, tvNombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();

        editTextUserId = findViewById(R.id.editTextUserId);

        tvNombre = findViewById(R.id.tvNombre);
        tvApellido = findViewById(R.id.tvApellido);
        tvCorreo = findViewById(R.id.tvCorreo);
        tvContrasenia = findViewById(R.id.tvContrasenia);
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);

        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
        registrarContacto = findViewById(R.id.btnRegistrarContacto);
        btnListarUsuarios = findViewById(R.id.btnListarUsuarios);
        btnBuscarUsuario = findViewById(R.id.btnBuscarUsuario);
        ImageButton cerrarSesion = findViewById(R.id.btnCerrarSesion);

        btnBuscarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el ID del usuario desde el EditText
                String userId = editTextUserId.getText().toString().trim();

                if (userId.isEmpty()) {
                    Toast.makeText(Home.this, "Por favor, Ingrese un ID valido", Toast.LENGTH_SHORT).show();
                    tvNombre.setText("");
                    tvApellido.setText("");
                    tvCorreo.setText("");
                    tvContrasenia.setText("");
                    tvNombreUsuario.setText("");
                    return;
                }

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userId);

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Usuario usuario = dataSnapshot.getValue(Usuario.class);

                            // Actualizar los TextView con la información del usuario encontrado
                            if (usuario != null) {
                                Toast.makeText(Home.this, "Contacto encontrado con exito!!", Toast.LENGTH_SHORT).show();
                                tvNombre.setText(usuario.getNombre());
                                tvApellido.setText(usuario.getApellido());
                                tvCorreo.setText(usuario.getCorreo());
                                tvContrasenia.setText(usuario.getContrasenia());
                                tvNombreUsuario.setText(usuario.getNombreUsuario());
                            }
                        } else {
                            // Mostrar un mensaje si el usuario no se encuentra
                            Toast.makeText(Home.this, "Contacto no encontrado", Toast.LENGTH_SHORT).show();
                            tvNombre.setText("");
                            tvApellido.setText("");
                            tvCorreo.setText("");
                            tvContrasenia.setText("");
                            tvNombreUsuario.setText("");
                            editTextUserId.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Home.this, "Error al buscar el Contacto", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        registrarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, RegisterContacto.class);
                startActivity(intent);
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el ID del usuario desde el EditText
                String userId = editTextUserId.getText().toString().trim();

                if (TextUtils.isEmpty(userId)) {
                    Toast.makeText(Home.this, "Por favor, seleccione un Contacto para editar.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Obtener los nuevos valores de los TextView
                String nuevoNombre = tvNombre.getText().toString().trim();
                String nuevoApellido = tvApellido.getText().toString().trim();
                String nuevoCorreo = tvCorreo.getText().toString().trim();
                String nuevaContrasenia = tvContrasenia.getText().toString().trim();
                String nuevoNombreUsuario = tvNombreUsuario.getText().toString().trim();

                if (TextUtils.isEmpty(nuevoNombre) || TextUtils.isEmpty(nuevoApellido) ||
                        TextUtils.isEmpty(nuevoCorreo) || TextUtils.isEmpty(nuevaContrasenia) ||
                        TextUtils.isEmpty(nuevoNombreUsuario)) {
                    Toast.makeText(Home.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Crear el cuadro de diálogo para confirmar la edición
                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setTitle("Confirmación");
                builder.setMessage("¿Está seguro de editar estos datos?");

                // Agregar botones al cuadro de diálogo
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Actualizar los datos en Firebase
                        DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userId);
                        usuarioRef.child("nombre").setValue(nuevoNombre);
                        usuarioRef.child("apellido").setValue(nuevoApellido);
                        usuarioRef.child("correo").setValue(nuevoCorreo);
                        usuarioRef.child("contrasenia").setValue(nuevaContrasenia);
                        usuarioRef.child("nombreUsuario").setValue(nuevoNombreUsuario);

                        Toast.makeText(Home.this, "Cambios guardados exitosamente", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // No hacer nada si el usuario selecciona "No"
                        dialog.dismiss();
                    }
                });

                // Mostrar el cuadro de diálogo
                builder.show();
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el ID del usuario desde el EditText
                String userId = editTextUserId.getText().toString().trim();

                // Verificar si se ha seleccionado un usuario
                if (TextUtils.isEmpty(userId)) {
                    Toast.makeText(Home.this, "Por favor, seleccione un Contacto para eliminar.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Crear el cuadro de diálogo para confirmar la eliminación
                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setTitle("Confirmación");
                builder.setMessage("¿Está seguro de eliminar este Contacto?");

                // Agregar botones al cuadro de diálogo
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Eliminar el usuario de Firebase
                        DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userId);
                        usuarioRef.removeValue();

                        Toast.makeText(Home.this, "Contacto eliminado exitosamente", Toast.LENGTH_SHORT).show();

                        // Limpiar los TextView después de la eliminación
                        tvNombre.setText("");
                        tvApellido.setText("");
                        tvCorreo.setText("");
                        tvContrasenia.setText("");
                        tvNombreUsuario.setText("");
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // No hacer nada si el usuario selecciona "No"
                        dialog.dismiss();
                    }
                });

                // Mostrar el cuadro de diálogo
                builder.show();
            }
        });
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(Home.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                // Redirige al usuario a la pantalla de inicio de sesión
                Intent intent = new Intent(Home.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        btnListarUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, ListarContactos.class);
                startActivity(intent);
            }
        });
    }
}