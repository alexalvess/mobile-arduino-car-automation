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

public class TravaActivity extends AppCompatActivity {
    private Bluetooth bt;
    private Control control;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trava);

        Receive();

        final Button btnTrava = (Button) findViewById(R.id.btnTrava);
        final Button btnDestrava = (Button) findViewById(R.id.btnDestrava);

        try {
            this.bt = new Bluetooth();
            this.bt.ativaBluetooth();
        } catch (Exception ex) {
            Log.i("Bluetooth -> ", "Erro - [" + ex.getMessage() + "]");
        }

        ControleBotao(btnTrava, btnDestrava);

        btnTrava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(control.getFlagTrava() == 0 && control.getFlagDestrava() == 1){
                    btnTrava.setBackgroundResource(R.color.Ativado);
                    control.setFlagTrava(1);

                    btnDestrava.setBackgroundResource(R.color.Desativado);
                    control.setFlagDestrava(0);

                    Trava();

                    toast = Toast.makeText(getBaseContext(), "Travando Veículo...", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        btnDestrava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(control.getFlagDestrava() == 0 && control.getFlagTrava() == 1){
                    btnDestrava.setBackgroundResource(R.color.Ativado);
                    control.setFlagDestrava(1);

                    btnTrava.setBackgroundResource(R.color.Desativado);
                    control.setFlagTrava(0);

                    Destrava();

                    toast = Toast.makeText(getBaseContext(), "Destravando Veículo...", Toast.LENGTH_SHORT);
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

    public void ControleBotao(Button btnTrava, Button btnDestrava){
        if(control.getFlagTrava() == 1)
            btnTrava.setBackgroundResource(R.color.Ativado);
        else
            btnDestrava.setBackgroundResource(R.color.Ativado);
    }

    public void Receive(){
        Intent intent = getIntent();
        this.control = (Control) intent.getSerializableExtra("controle");
    }

    public void Trava(){
        try {
            this.bt.enviaDados("trava", "travar");
        } catch (Exception ex) {
            Log.i("Bluetooth -> ", "Erro - [" + ex.getMessage() + "]");
        }
    }

    public void Destrava(){
        try {
            this.bt.enviaDados("trava", "destravar");
        } catch (Exception ex) {
            Log.i("Bluetooth -> ", "Erro - [" + ex.getMessage() + "]");
        }
    }
}
