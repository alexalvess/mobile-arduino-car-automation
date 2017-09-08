package com.carautomation.carautomation11;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class IndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        Button btnAcesso = (Button) findViewById(R.id.btnAcesso);
        Button btnSobre = (Button) findViewById(R.id.btnSobre);

        btnAcesso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Acesso();
            }
        });

        btnSobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sobre();
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        finish();
    }

    public void Acesso(){
        Intent acesso = new Intent(this, AccessActivity.class);
        startActivity(acesso);
    }

    public void Sobre(){
        Intent sobre = new Intent(this, SobreActivity.class);
        startActivity(sobre);
    }
}
