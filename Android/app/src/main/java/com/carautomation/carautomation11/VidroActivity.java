package com.carautomation.carautomation11;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.carautomation.carautomation11.Controller.Bluetooth;
import com.carautomation.carautomation11.Controller.Control;

import java.io.IOException;
import java.util.Locale;

public class VidroActivity extends AppCompatActivity{
    private Bluetooth bt;
    private Control control;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vidro);

        Receive();

        final Button btnAbrirVidro = (Button) findViewById(R.id.btnAbrir);
        final Button btnFecharVidro = (Button) findViewById(R.id.btnFechar);

        try {
            this.bt = new Bluetooth();
            this.bt.ativaBluetooth();
        } catch (Exception ex) {
            Log.i("Bluetooth -> ", "Erro - [" + ex.getMessage() + "]");
        }

        ControleBotao(btnAbrirVidro, btnFecharVidro);

        btnAbrirVidro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (control.getFlagVidroAberto() == 0 && control.getFlagVidroFechado() == 1) {
                    btnAbrirVidro.setBackgroundResource(R.color.Ativado);
                    control.setFlagVidroAberto(1);

                    btnFecharVidro.setBackgroundResource(R.color.Desativado);
                    control.setFlagVidroFechado(0);

                    AbrirVidro();

                    toast = Toast.makeText(getBaseContext(), "Abrindo Vidros...", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        btnFecharVidro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (control.getFlagVidroFechado() == 0 && control.getFlagVidroAberto() == 1) {
                    btnFecharVidro.setBackgroundResource(R.color.Ativado);
                    control.setFlagVidroFechado(1);

                    btnAbrirVidro.setBackgroundResource(R.color.Desativado);
                    control.setFlagVidroAberto(0);

                    FecharVidro();

                    toast = Toast.makeText(getBaseContext(), "Fechando Vidros...", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            bt.fechaConexaoBluetooth();
        }catch (Exception ex){
            Log.i("Bluetooth", "Erro ao desativar bluetooth - [" + ex.getMessage() + "]");
        }finally {
            finish();
        }
    }

    public void Receive(){
        Intent intent = getIntent();
        this.control = (Control) intent.getSerializableExtra("controle");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("controle", control);
        setResult(1, intent);
        finish();
        super.onBackPressed();
    }

    public void ControleBotao(Button btnAbrirVidro, Button btnFecharVidro){
        if(control.getFlagVidroFechado() == 1)
            btnFecharVidro.setBackgroundResource(R.color.Ativado);
        else
            btnAbrirVidro.setBackgroundResource(R.color.Ativado);
    }

    public void AbrirVidro() {
        try {
            this.bt.enviaDados("vidro", "abrir_vidro");
        } catch (Exception ex) {
            Log.i("Bluetooth -> ", "Erro - [" + ex.getMessage() + "]");
        }
    }

    public void FecharVidro() {
        try {
            this.bt.enviaDados("vidro", "fechar_vidro");
        } catch (Exception ex) {
            Log.i("Bluetooth -> ", "Erro - [" + ex.getMessage() + "]");
        }
    }
}