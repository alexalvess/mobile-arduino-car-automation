package com.carautomation.carautomation11;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.carautomation.carautomation11.Controller.Control;

public class AccessActivity extends AppCompatActivity {
    private EditText txtLogin;
    private EditText txtSenhta;

    private Control control = new Control();

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        Button btnAcessar = (Button) findViewById(R.id.btnAcessar);
        txtLogin= (EditText) findViewById(R.id.txtLogin);
        txtSenhta = (EditText) findViewById(R.id.txtSenha);

        btnAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Acessar();
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        finish();
    }

    public void Acessar(){
        try{
            //Validar();

            Intent acessar = new Intent(this, HomeActivity.class);
            acessar.putExtra("controle", control);
            startActivity(acessar);
            finish();

            toast = Toast.makeText(getBaseContext(), "Acesso Efetuado com Sucesso!", Toast.LENGTH_SHORT);
            toast.show();
        }catch (Exception ex){
            Log.i("Acesso", ex.getMessage());

            toast = Toast.makeText(getBaseContext(), ex.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void Validar() throws Exception {
        if(!txtLogin.getText().toString().equals("bytech") || !txtSenhta.getText().toString().equals("2016"))
            throw new Exception("Login e/ou Senha Inválido(s)!");
        Log.i("Acesso", "[" + txtLogin.getText().toString() + " " + txtSenhta.getText().toString() + "]");
    }
}
