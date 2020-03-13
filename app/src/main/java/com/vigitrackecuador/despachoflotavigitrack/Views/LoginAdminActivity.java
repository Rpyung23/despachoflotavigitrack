package com.vigitrackecuador.despachoflotavigitrack.Views;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vigitrackecuador.despachoflotavigitrack.R;

public class LoginAdminActivity extends AppCompatActivity
{
    TextInputEditText textInputEditTextusuario;
    TextInputEditText textInputEditTextpass;
    Button btn_login_Admin;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);
        textInputEditTextpass=findViewById(R.id.txt_pass_admin);
        toolbar=findViewById(R.id.toolbr_login_admin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Vigitrack Administrador");
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        textInputEditTextusuario=findViewById(R.id.txt_user_admin);
        btn_login_Admin=findViewById(R.id.btn_ingresar_admin);
        btn_login_Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textInputEditTextpass.getText().equals("")||textInputEditTextusuario.getText().equals(""))
                {
                    Toast.makeText(LoginAdminActivity.this, "Error Datos Vacios", Toast.LENGTH_SHORT).show();
                }else
                    {
                        if (textInputEditTextusuario.getText().equals("admin") && textInputEditTextpass.getText().equals("vigitrack1985"))
                        {
                            Toast.makeText(LoginAdminActivity.this, "Bienvenido Admin ... ", Toast.LENGTH_SHORT).show();
                        }
                    }
            }
        });
    }

}
