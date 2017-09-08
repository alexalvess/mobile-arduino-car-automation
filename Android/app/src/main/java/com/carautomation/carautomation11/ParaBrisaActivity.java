package com.carautomation.carautomation11;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.carautomation.carautomation11.Controller.Bluetooth;
import com.carautomation.carautomation11.Controller.Control;

import java.io.IOException;

public class ParaBrisaActivity extends AppCompatActivity {
    private Bluetooth bt;
    private Control control;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_para_brisa);

        Receive();

        final Button btnMedia= (Button) findViewById(R.id.btnLimpadorMedio);

        try {
            this.bt = new Bluetooth();
            this.bt.ativaBluetooth();
        }catch (Exception ex){
            Log.i("Bluetooth -> ", "Erro - [" + ex.getMessage() + "]");
        }

        ControleBotao(btnMedia);

        btnMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(control.getFlagLimpador() == 0){
                    btnMedia.setBackgroundResource(R.color.Ativado);
                    control.setFlagLimpador(1);
                    Funcionalidade("para_brisa", "media");

                    toast = Toast.makeText(getBaseContext(), "Ativando Limpador de Para-Brisa...", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    btnMedia.setBackgroundResource(R.color.Desativado);
                    control.setFlagLimpador(0);
                    Funcionalidade("para_brisa", "desativar");

                    toast = Toast.makeText(getBaseContext(), "Desativando Limpador de Para-Brisa...", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        try{
            bt.fechaConexaoBluetooth();
        }catch (Exception ex){
            Log.i("Bluetooth", "Erro ao desativar bluetooth - [" + ex.getMessage() + "]");
        }finally {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("controle", control);
        setResult(1, intent);
        finish();
        super.onBackPressed();
    }

    public void ControleBotao(Button btnMedia){
        if(control.getFlagLimpador() == 1)
            btnMedia.setBackgroundResource(R.color.Ativado);
    }

    public void Receive(){
        Intent intent = getIntent();
        this.control = (Control) intent.getSerializableExtra("controle");
    }

    public void Funcionalidade(String parametro1, String parametro2){
        try {
            this.bt.enviaDados(parametro1, parametro2);
        } catch (Exception ex) {
            Log.i("Bluetooth -> ", "Erro - [" + ex.getMessage() + "]");
        }
    }
}