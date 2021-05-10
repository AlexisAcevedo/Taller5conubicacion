package com.example.taller5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText usuario, password;
    Button btnlogin, btnvolverRegistro;
    daoUsuario dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuario = (EditText) findViewById(R.id.usuariologin);
        password = (EditText) findViewById(R.id.passwordlogin);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        btnvolverRegistro = (Button) findViewById(R.id.btnvolverRegistro);
        dao = new daoUsuario(this);

        btnvolverRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String us=usuario.getText().toString();
                String pass=password.getText().toString();

                if(us.equals("")&& pass.equals("")){ 
                    Toast.makeText(getApplicationContext(), "Campos Vacios", Toast.LENGTH_SHORT).show();
                } else if(dao.login(us,pass) == 1){
                    Usuario ux= dao.getUsuario(us,pass);
                    Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_SHORT).show();
                    Intent i2= new Intent(getApplicationContext(), HomeActivity.class);
                    i2.putExtra("id",ux.getId());
                    startActivity(i2);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "Datos incorrectos", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}