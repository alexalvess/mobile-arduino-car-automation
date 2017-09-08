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

public class FarolActivity extends AppCompatActivity {
    private Bluetooth bt;
    private Control control;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farol);

        Receive();

        final Button btnFarolAlto = (Button) findViewById(R.id.btnFarolAlto);
        final Button btnFarol = (Button) findViewById(R.id.btnFarol);

        try {
            this.bt = new Bluetooth();
            this.bt.ativaBluetooth();
        }catch (Exception ex){
            Log.i("Bluetooth -> ", "Erro - [" + ex.getMessage() + "]");
        }

        ControleButao(btnFarolAlto, btnFarol);

        btnFarolAlto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(control.getFlagFarolAlto() == 0){
                    btnFarolAlto.setBackgroundResource(R.color.Ativado);
                    control.setFlagFarolAlto(1);
                    Funcionalidade("farol", "alto");

                    toast = Toast.makeText(getBaseContext(), "Ativando Farol Alto...", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    btnFarolAlto.setBackgroundResource(R.color.Desativado);
                    control.setFlagFarolAlto(0);
                    Funcionalidade("farol", "desativarAlto");

                    toast = Toast.makeText(getBaseContext(), "Desativando Farol...", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        btnFarol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(control.getFlagFarolBaixo() == 0){
                    btnFarol.setBackgroundResource(R.color.Ativado);
                    control.setFlagFarolBaixo(1);
                    Funcionalidade("farol", "normal");

                    toast = Toast.makeText(getBaseContext(), "Ativando Farol Baixo...", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    btnFarol.setBackgroundResource(R.color.Desativado);
                    control.setFlagFarolBaixo(0);
                    Funcionalidade("farol", "desativarBaixo");

                    toast = Toast.makeText(getBaseContext(), "Desativando Farol...", Toast.LENGTH_SHORT);
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

    public void ControleButao(Button btnFarolAlto, Button btnFarol){
        if(control.getFlagFarolAlto() == 1)
            btnFarolAlto.setBackgroundResource(R.color.Ativado);
        if(control.getFlagFarolBaixo() == 1)
            btnFarol.setBackgroundResource(R.color.Ativado);
    }

    public void Receive(){
        Intent intent = getIntent();
        this.control = (Control) intent.getSerializableExtra("controle");
    }

    public void Funcionalidade(String parametro1, String paramentro2){
        try {
            this.bt.enviaDados(parametro1, paramentro2);
        } catch (Exception ex) {
            Log.i("Bluetooth -> ", "Erro - [" + ex.getMessage() + "]");
        }
    }
}
