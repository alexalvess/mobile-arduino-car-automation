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

public class IgnicaoActivity extends AppCompatActivity {
    private Bluetooth bt;
    private Control control;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ignicao);

        Receive();

        final Button btnIgnicao = (Button) findViewById(R.id.btnChave);
        final Button btnPartida = (Button) findViewById(R.id.btnLigar);

        try {
            this.bt = new Bluetooth();
            this.bt.ativaBluetooth();
        }catch (Exception ex){
            Log.i("Bluetooth -> ", "Erro - [" + ex.getMessage() + "]");
        }

        ControleBotao(btnIgnicao);

        btnIgnicao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(control.getFlagIgnicao() == 1){
                    btnIgnicao.setBackgroundResource(R.color.Desativado);
                    control.setFlagIgnicao(0);
                    control.setFlagPartida(0);
                    Funcionalidade("ignicao", "desligar");

                    toast = Toast.makeText(getBaseContext(), "Delisgando Veículo...", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    btnIgnicao.setBackgroundResource(R.color.Ativado);
                    control.setFlagIgnicao(1);
                    Funcionalidade("ignicao", "chave");

                    toast = Toast.makeText(getBaseContext(), "Acionando Ignição...", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        btnPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (control.getFlagPartida() == 0 && control.getFlagIgnicao() == 1) {
                    control.setFlagPartida(1);
                    Funcionalidade("ignicao", "ligar");

                    toast = Toast.makeText(getBaseContext(), "Executando Partida...", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    toast = Toast.makeText(getBaseContext(), "Acione a ignição primeiro...", Toast.LENGTH_SHORT);
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


    public void ControleBotao(Button btnIgnicao){
        if(control.getFlagIgnicao() == 1) btnIgnicao.setBackgroundResource(R.color.Ativado);
        else btnIgnicao.setBackgroundResource(R.color.Desativado);
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
