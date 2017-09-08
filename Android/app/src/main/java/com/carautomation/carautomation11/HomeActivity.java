package com.carautomation.carautomation11;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.carautomation.carautomation11.Controller.Bluetooth;
import com.carautomation.carautomation11.Controller.Control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private Bluetooth bluetooth;
    private Control control;
    private Toast toast;

    private int VOICE_ACTION_REQUEST_CODE = 0;

    private Button btnVoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bluetooth = new Bluetooth();
        bluetooth.habilitarBluetooth();

        Receive();

        Button btnVidros = (Button) findViewById(R.id.btnVidro);
        Button btnTravas = (Button) findViewById(R.id.btnTrava);
        Button btnLimpador = (Button) findViewById(R.id.btnParaBrisa);
        Button btnFarol = (Button) findViewById(R.id.btnFarol);
        Button btnIgnicao = (Button) findViewById(R.id.btnIgnicao);
        btnVoice = (Button) findViewById(R.id.btnVoice);

        btnVidros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vidros();
            }
        });

        btnTravas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Travas();
            }
        });

        btnLimpador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Limpador();
            }
        });

        btnFarol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Farol();
            }
        });

        btnIgnicao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ignicao();
            }
        });

        btnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVoiceAction();
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        try{
            bluetooth.desabilitarBluetooth();
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

    public void Vidros(){
        Intent vidros = new Intent(this, VidroActivity.class);
        vidros.putExtra("controle", control);
        startActivityForResult(vidros, 1);
    }

    public void Travas(){
        Intent travas = new Intent(this, TravaActivity.class);
        travas.putExtra("controle", control);
        startActivityForResult(travas, 1);
    }

    public void Limpador(){
        Intent limpador = new Intent(this, ParaBrisaActivity.class);
        limpador.putExtra("controle", control);
        startActivityForResult(limpador, 1);
    }

    public void Farol(){
        Intent farol = new Intent(this, FarolActivity.class);
        farol.putExtra("controle", control);
        startActivityForResult(farol, 1);
    }

    public void Ignicao(){
        Intent ignicao = new Intent(this, IgnicaoActivity.class);
        ignicao.putExtra("controle", control);
        startActivityForResult(ignicao, 1);
    }

    private void getVoiceAction() {
        try {
            btnVoice.setBackgroundResource(R.color.AcionaVoz);
            this.bluetooth = new Bluetooth();
            this.bluetooth.ativaBluetooth();

            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Reconhecimento Voz");
            startActivityForResult(intent, VOICE_ACTION_REQUEST_CODE);
        } catch (Exception ex) {
            toast = Toast.makeText(getBaseContext(), "Erro - [" + ex.getMessage() + "]", Toast.LENGTH_SHORT);
            toast.show();

            btnVoice.setBackgroundResource(R.color.Desativado);

            Log.i("Bluetooth -> ", "Erro - [" + ex.getMessage() + "]");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == VOICE_ACTION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String spokenText = results.get(0);
                final ArrayList<String> lista = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (lista.size() > 1) {

                    for (Iterator<String> iterator = lista.iterator(); iterator.hasNext(); ) {
                        String comando = iterator.next().toLowerCase();

                        if(VerificarComandoVozAbrirVidro(comando)) break;
                        else if(VerificarComandoVozFecharVidro(comando)) break;
                        else if(VerificarComandoVozTravar(comando)) break;
                        else if(VerificarComandoVozDestravar(comando)) break;
                        else if(VerificarComandoVozAtivarLimpador(comando)) break;
                        else if(VerificarComandoVozDesativarLimpador(comando)) break;
                        else if(VerificarComandoVozAtivarFarolAlto(comando)) break;
                        else if(VerificarComandoVozDesativarFarolAlto(comando)) break;
                        else if(VerificarComandoVozAtivarFarolBaixo(comando)) break;
                        else if(VerificarComandoVozDesativarFarolBaixo(comando)) break;
                        else if(VerificarComandoVozLigarCarro(comando)) break;
                        else if(VerificarComandoVozDesligarCarro(comando))break;
                        else{
                            toast = Toast.makeText(getBaseContext(), "Comando de voz não encontrado!", Toast.LENGTH_SHORT);
                            toast.show();
                        }

                        Log.i("Comando de Voz", comando);
                    }
                }
            }
            try{
                btnVoice.setBackgroundResource(R.color.Desativado);

                bluetooth.fechaConexaoBluetooth();
            }catch (Exception ex){
                Log.i("Bluetooth", ex.getMessage());
            }
        }
        else if(requestCode == 1) {
            this.control = (Control) data.getSerializableExtra("controle");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean VerificarComandoVozAbrirVidro(String comando){
        ArrayList<Boolean> verificar = new ArrayList<Boolean>();

        verificar.add(comando.contains("abrir vidro"));
        verificar.add(comando.contains("abrir vidros"));
        verificar.add(comando.contains("ativar vidro"));
        verificar.add(comando.contains("ativar vidros"));
        verificar.add(comando.contains("descer vidro"));
        verificar.add(comando.contains("descer vidros"));
        verificar.add(comando.contains("abrir janela"));
        verificar.add(comando.contains("abrir janelas"));
        verificar.add(comando.contains("ativar janela"));
        verificar.add(comando.contains("ativar janelas"));
        verificar.add(comando.contains("descer janela"));
        verificar.add(comando.contains("descer janelas"));

        Log.i("Comando de Voz", comando);

        for (int i = 0; i < verificar.size(); i++){
            if(verificar.get(i)) {
                btnVoice.setBackgroundResource(R.color.Desativado);

                bluetooth.enviaDados("vidro", "abrir_vidro");

                toast = Toast.makeText(getBaseContext(), "Abrindo Vidros...", Toast.LENGTH_SHORT);
                toast.show();

                Log.i("Bluetooth -> Voz", "Abrindo vidros");

                return true;
            }
        }

        return false;
    }

    public boolean VerificarComandoVozFecharVidro(String comando){
        ArrayList<Boolean> verificar = new ArrayList<Boolean>();

        verificar.add(comando.contains("fechar vidro"));
        verificar.add(comando.contains("fechar vidros"));
        verificar.add(comando.contains("desativar vidro"));
        verificar.add(comando.contains("desativar vidros"));
        verificar.add(comando.contains("subir vidro"));
        verificar.add(comando.contains("subir vidros"));
        verificar.add(comando.contains("fechar janela"));
        verificar.add(comando.contains("fechar janelas"));
        verificar.add(comando.contains("desativar janela"));
        verificar.add(comando.contains("desativar janelas"));
        verificar.add(comando.contains("subir janela"));
        verificar.add(comando.contains("subir janelas"));

        Log.i("Comando de Voz", comando);

        for (int i = 0; i < verificar.size(); i++){
            if(verificar.get(i)) {
                btnVoice.setBackgroundResource(R.color.Desativado);

                bluetooth.enviaDados("vidro", "fechar_vidro");

                toast = Toast.makeText(getBaseContext(), "Fechando Vidros...", Toast.LENGTH_SHORT);
                toast.show();

                Log.i("Bluetooth -> Voz", "Fechando vidros");

                return true;
            }
        }

        return false;
    }

    public boolean VerificarComandoVozTravar(String comando){
        ArrayList<Boolean> verificar = new ArrayList<Boolean>();

        verificar.add(comando.contains("travar veiculo"));
        verificar.add(comando.contains("travar carro"));
        verificar.add(comando.contains("travar automovel"));
        verificar.add(comando.contains("travar gol"));
        verificar.add(comando.contains("travar"));
        verificar.add(comando.contains("trancar veiculo"));
        verificar.add(comando.contains("trancar carro"));
        verificar.add(comando.contains("trancar automovel"));
        verificar.add(comando.contains("trancar gol"));
        verificar.add(comando.contains("trancar"));
        verificar.add(comando.contains("fechar veiculo"));
        verificar.add(comando.contains("fechar carro"));
        verificar.add(comando.contains("fechar automovel"));
        verificar.add(comando.contains("fechar gol"));
        verificar.add(comando.contains("fechar"));

        Log.i("Comando de Voz", comando);

        for (int i = 0; i < verificar.size(); i++){
            if(verificar.get(i)) {
                btnVoice.setBackgroundResource(R.color.Desativado);

                bluetooth.enviaDados("trava", "travar");

                toast = Toast.makeText(getBaseContext(), "Travando Veículo...", Toast.LENGTH_SHORT);
                toast.show();

                Log.i("Bluetooth -> Voz", "Travando veículo");

                return true;
            }
        }
        return false;
    }

    public boolean VerificarComandoVozDestravar(String comando){
        ArrayList<Boolean> verificar = new ArrayList<Boolean>();

        verificar.add(comando.contains("destravar veiculo"));
        verificar.add(comando.contains("destravar carro"));
        verificar.add(comando.contains("destravar automovel"));
        verificar.add(comando.contains("destravar gol"));
        verificar.add(comando.contains("destravar"));
        verificar.add(comando.contains("destrancar veiculo"));
        verificar.add(comando.contains("destrancar carro"));
        verificar.add(comando.contains("destrancar automovel"));
        verificar.add(comando.contains("destrancar gol"));
        verificar.add(comando.contains("destrancar"));
        verificar.add(comando.contains("abrir veiculo"));
        verificar.add(comando.contains("abrir carro"));
        verificar.add(comando.contains("abrir automovel"));
        verificar.add(comando.contains("abrir gol"));
        verificar.add(comando.contains("abrir"));

        Log.i("Comando de Voz", comando);

        for (int i = 0; i < verificar.size(); i++){
            if(verificar.get(i)) {
                btnVoice.setBackgroundResource(R.color.Desativado);

                bluetooth.enviaDados("trava", "destravar");

                toast = Toast.makeText(getBaseContext(), "Destravando Veículo...", Toast.LENGTH_SHORT);
                toast.show();

                Log.i("Bluetooth -> Voz", "Destravando veículo");

                return true;
            }
        }
        return false;
    }

    public boolean VerificarComandoVozAtivarLimpador(String comando){
        ArrayList<Boolean> verificar = new ArrayList<Boolean>();

        verificar.add(comando.contains("ativar limpador para-brisa"));
        verificar.add(comando.contains("ativar limpador para-brisas"));
        verificar.add(comando.contains("ativar limpador para brisa"));
        verificar.add(comando.contains("ativar limpador para brisas"));
        verificar.add(comando.contains("ativar limpador parabrisa"));
        verificar.add(comando.contains("ativar limpador parabrisas"));
        verificar.add(comando.contains("ativar para-brisa"));
        verificar.add(comando.contains("ativar para-brisas"));
        verificar.add(comando.contains("ativar para brisa"));
        verificar.add(comando.contains("ativar para brisas"));
        verificar.add(comando.contains("ativar parabrisa"));
        verificar.add(comando.contains("ativar parabrisas"));
        verificar.add(comando.contains("ativar limpador"));
        verificar.add(comando.contains("ligar limpador para-brisa"));
        verificar.add(comando.contains("ligar limpador para-brisas"));
        verificar.add(comando.contains("ligar limpador para brisa"));
        verificar.add(comando.contains("ligar limpador para brisas"));
        verificar.add(comando.contains("ligar limpador parabrisa"));
        verificar.add(comando.contains("ligar limpador parabrisas"));
        verificar.add(comando.contains("ligar para-brisa"));
        verificar.add(comando.contains("ligar para-brisas"));
        verificar.add(comando.contains("ligar para brisa"));
        verificar.add(comando.contains("ligar para brisas"));
        verificar.add(comando.contains("ligar parabrisa"));
        verificar.add(comando.contains("ligar parabrisas"));
        verificar.add(comando.contains("ligar limpador"));
        verificar.add(comando.contains("acionar limpador para-brisa"));
        verificar.add(comando.contains("acionar limpador para-brisas"));
        verificar.add(comando.contains("acionar limpador para brisa"));
        verificar.add(comando.contains("acionar limpador para brisas"));
        verificar.add(comando.contains("acionar limpador parabrisa"));
        verificar.add(comando.contains("acionar limpador parabrisas"));
        verificar.add(comando.contains("acionar para-brisa"));
        verificar.add(comando.contains("acionar para-brisas"));
        verificar.add(comando.contains("acionar para brisa"));
        verificar.add(comando.contains("acionar para brisas"));
        verificar.add(comando.contains("acionar parabrisa"));
        verificar.add(comando.contains("acionar parabrisas"));
        verificar.add(comando.contains("acionar limpador"));

        Log.i("Comando de Voz", comando);

        for (int i = 0; i < verificar.size(); i++){
            if(verificar.get(i)) {
                btnVoice.setBackgroundResource(R.color.Desativado);

                bluetooth.enviaDados("para_brisa", "media");

                toast = Toast.makeText(getBaseContext(), "Ativando Limpador Para-Brisa...", Toast.LENGTH_SHORT);
                toast.show();

                this.control.setFlagLimpador(1);

                Log.i("Bluetooth -> Voz", "Ativando Limpador Para-Brisa");

                return true;
            }
        }
        return false;
    }

    public boolean VerificarComandoVozDesativarLimpador(String comando){
        ArrayList<Boolean> verificar = new ArrayList<Boolean>();

        verificar.add(comando.contains("desativar limpador para-brisa"));
        verificar.add(comando.contains("desativar limpador para-brisas"));
        verificar.add(comando.contains("desativar limpador para brisa"));
        verificar.add(comando.contains("desativar limpador para brisas"));
        verificar.add(comando.contains("desativar limpador parabrisa"));
        verificar.add(comando.contains("desativar limpador parabrisas"));
        verificar.add(comando.contains("desativar para-brisa"));
        verificar.add(comando.contains("desativar para-brisas"));
        verificar.add(comando.contains("desativar para brisa"));
        verificar.add(comando.contains("desativar para brisas"));
        verificar.add(comando.contains("desativar parabrisa"));
        verificar.add(comando.contains("desativar parabrisas"));
        verificar.add(comando.contains("desativar limpador"));
        verificar.add(comando.contains("desligar limpador para-brisa"));
        verificar.add(comando.contains("desligar limpador para-brisas"));
        verificar.add(comando.contains("desligar limpador para brisa"));
        verificar.add(comando.contains("desligar limpador para brisas"));
        verificar.add(comando.contains("desligar limpador parabrisa"));
        verificar.add(comando.contains("desligar limpador parabrisas"));
        verificar.add(comando.contains("desligar para-brisa"));
        verificar.add(comando.contains("desligar para-brisas"));
        verificar.add(comando.contains("desligar para brisa"));
        verificar.add(comando.contains("desligar para brisas"));
        verificar.add(comando.contains("desligar parabrisa"));
        verificar.add(comando.contains("desligar parabrisas"));
        verificar.add(comando.contains("desligar limpador"));
        verificar.add(comando.contains("parar limpador para-brisa"));
        verificar.add(comando.contains("parar limpador para-brisas"));
        verificar.add(comando.contains("parar limpador para brisa"));
        verificar.add(comando.contains("parar limpador para brisas"));
        verificar.add(comando.contains("parar limpador parabrisa"));
        verificar.add(comando.contains("parar limpador parabrisas"));
        verificar.add(comando.contains("parar para-brisa"));
        verificar.add(comando.contains("parar para-brisas"));
        verificar.add(comando.contains("parar para brisa"));
        verificar.add(comando.contains("parar para brisas"));
        verificar.add(comando.contains("parar parabrisa"));
        verificar.add(comando.contains("parar parabrisas"));
        verificar.add(comando.contains("parar limpador"));

        Log.i("Comando de Voz", comando);

        for (int i = 0; i < verificar.size(); i++){
            if(verificar.get(i)) {
                btnVoice.setBackgroundResource(R.color.Desativado);

                bluetooth.enviaDados("para_brisa", "desativar");

                toast = Toast.makeText(getBaseContext(), "Desativando Limpador Para-Brisa...", Toast.LENGTH_SHORT);
                toast.show();

                this.control.setFlagLimpador(0);

                Log.i("Bluetooth -> Voz", "Desativando Limpador Para-Brisa");

                return true;
            }
        }
        return false;
    }

    public boolean VerificarComandoVozAtivarFarolAlto(String comando){
        ArrayList<Boolean> verificar = new ArrayList<Boolean>();

        verificar.add(comando.contains("ativar farol alto"));
        verificar.add(comando.contains("ativar farol-alto"));
        verificar.add(comando.contains("ativar farolalto"));
        verificar.add(comando.contains("acionar farol alto"));
        verificar.add(comando.contains("acionar farol-alto"));
        verificar.add(comando.contains("acionar farolalto"));
        verificar.add(comando.contains("ligar farol alto"));
        verificar.add(comando.contains("ligar farol-alto"));
        verificar.add(comando.contains("ligar farolalto"));
        verificar.add(comando.contains("ascender farol alto"));
        verificar.add(comando.contains("ascender farol-alto"));
        verificar.add(comando.contains("ascender farolalto"));

        Log.i("Comando de Voz", comando);

        for (int i = 0; i < verificar.size(); i++){
            if(verificar.get(i)) {
                btnVoice.setBackgroundResource(R.color.Desativado);

                bluetooth.enviaDados("farol", "alto");

                toast = Toast.makeText(getBaseContext(), "Ligando Farol Alto...", Toast.LENGTH_SHORT);
                toast.show();

                this.control.setFlagFarolAlto(1);

                Log.i("Bluetooth -> Voz", "Ligando Farol Alto");

                return true;
            }
        }
        return false;
    }

    public boolean VerificarComandoVozDesativarFarolAlto(String comando){
        ArrayList<Boolean> verificar = new ArrayList<Boolean>();

        verificar.add(comando.contains("desativar farol alto"));
        verificar.add(comando.contains("desativar farol-alto"));
        verificar.add(comando.contains("desativar farolalto"));
        verificar.add(comando.contains("apagar farol alto"));
        verificar.add(comando.contains("apagar farol-alto"));
        verificar.add(comando.contains("apagar farolalto"));
        verificar.add(comando.contains("desligar farol alto"));
        verificar.add(comando.contains("desligar farol-alto"));
        verificar.add(comando.contains("desligar farolalto"));

        Log.i("Comando de Voz", comando);

        for (int i = 0; i < verificar.size(); i++){
            if(verificar.get(i)) {
                btnVoice.setBackgroundResource(R.color.Desativado);

                bluetooth.enviaDados("farol", "desativarAlto");

                toast = Toast.makeText(getBaseContext(), "Desligando Farol Alto...", Toast.LENGTH_SHORT);
                toast.show();

                this.control.setFlagFarolAlto(0);

                Log.i("Bluetooth -> Voz", "Desligando Farol Alto");

                return true;
            }
        }
        return false;
    }

    public boolean VerificarComandoVozAtivarFarolBaixo(String comando){
        ArrayList<Boolean> verificar = new ArrayList<Boolean>();

        verificar.add(comando.contains("ativar farol baixo"));
        verificar.add(comando.contains("ativar farol-baixo"));
        verificar.add(comando.contains("ativar farolbaixo"));
        verificar.add(comando.contains("acionar farol baixo"));
        verificar.add(comando.contains("acionar farol-baixo"));
        verificar.add(comando.contains("acionar farolbaixo"));
        verificar.add(comando.contains("ligar farol baixo"));
        verificar.add(comando.contains("ligar farol-baixo"));
        verificar.add(comando.contains("ligar farolbaixo"));
        verificar.add(comando.contains("ascender farol baixo"));
        verificar.add(comando.contains("ascender farol-baixo"));
        verificar.add(comando.contains("ascender farolbaixo"));

        Log.i("Comando de Voz", comando);

        for (int i = 0; i < verificar.size(); i++){
            if(verificar.get(i)) {
                btnVoice.setBackgroundResource(R.color.Desativado);

                bluetooth.enviaDados("farol", "normal");

                toast = Toast.makeText(getBaseContext(), "Ligando Farol Baixo...", Toast.LENGTH_SHORT);
                toast.show();

                this.control.setFlagFarolBaixo(1);

                Log.i("Bluetooth -> Voz", "Ligando Farol Baixo");

                return true;
            }
        }
        return false;
    }

    public boolean VerificarComandoVozDesativarFarolBaixo(String comando){
        ArrayList<Boolean> verificar = new ArrayList<Boolean>();

        verificar.add(comando.contains("desativar farol baixo"));
        verificar.add(comando.contains("desativar farol-baixo"));
        verificar.add(comando.contains("desativar farolbaixo"));
        verificar.add(comando.contains("desligar farol baixo"));
        verificar.add(comando.contains("desligar farol-baixo"));
        verificar.add(comando.contains("desligar farolbaixo"));
        verificar.add(comando.contains("apagar farol baixo"));
        verificar.add(comando.contains("apagar farol-baixo"));
        verificar.add(comando.contains("apagar farolbaixo"));

        Log.i("Comando de Voz", comando);

        for (int i = 0; i < verificar.size(); i++){
            if(verificar.get(i)) {
                btnVoice.setBackgroundResource(R.color.Desativado);

                bluetooth.enviaDados("ignicao", "desligar");

                toast = Toast.makeText(getBaseContext(), "Desligando Carro...", Toast.LENGTH_SHORT);
                toast.show();

                this.control.setFlagIgnicao(0);

                Log.i("Bluetooth -> Voz", "Desligando Carro");

                return true;
            }
        }
        return false;
    }

    public boolean VerificarComandoVozLigarCarro(String comando){
        ArrayList<Boolean> verificar = new ArrayList<Boolean>();

        verificar.add(comando.contains("ligar carro"));
        verificar.add(comando.contains("ativar carro"));
        verificar.add(comando.contains("acionar carro"));
        verificar.add(comando.contains("ligar veiculo"));
        verificar.add(comando.contains("ativar veiculo"));
        verificar.add(comando.contains("acionar veiculo"));
        verificar.add(comando.contains("ligar automovel"));
        verificar.add(comando.contains("ativar automovel"));
        verificar.add(comando.contains("acionar automovel"));

        Log.i("Comando de Voz", comando);

        for (int i = 0; i < verificar.size(); i++){
            if(verificar.get(i)) {
                btnVoice.setBackgroundResource(R.color.Desativado);

                try{
                    bluetooth.enviaDados("ignicao", "chave");

                    Log.i("Bluetooth -> Voz", "Acionando ignição");

                    toast = Toast.makeText(getBaseContext(), "Acionando Ignição...", Toast.LENGTH_SHORT);
                    toast.show();

                    this.control.setFlagIgnicao(1);

                    Thread.sleep(7000);

                    bluetooth.enviaDados("ignicao", "ligar");

                    Log.i("Bluetooth -> Voz", "Ligando veículo");

                    toast = Toast.makeText(getBaseContext(), "Ligando Veículo...", Toast.LENGTH_SHORT);
                    toast.show();

                    return true;
                }catch (Exception ex){
                    Log.e("Erro Ligar Carro", ex.getMessage());
                    return false;
                }
            }
        }
        return false;
    }

    public boolean VerificarComandoVozDesligarCarro(String comando){
        ArrayList<Boolean> verificar = new ArrayList<Boolean>();

        verificar.add(comando.contains("desligar carro"));
        verificar.add(comando.contains("desativar carro"));
        verificar.add(comando.contains("desligar veiculo"));
        verificar.add(comando.contains("desativar veiculo"));
        verificar.add(comando.contains("desligar automovel"));
        verificar.add(comando.contains("desativar automovel"));

        Log.i("Comando de Voz", comando);

        for (int i = 0; i < verificar.size(); i++){
            if(verificar.get(i)) {
                btnVoice.setBackgroundResource(R.color.Desativado);

                bluetooth.enviaDados("farol", "desativarBaixo");

                toast = Toast.makeText(getBaseContext(), "Desligando Farol Baixo...", Toast.LENGTH_SHORT);
                toast.show();

                this.control.setFlagFarolBaixo(0);

                Log.i("Bluetooth -> Voz", "Desligando Farol Baixo");

                return true;
            }
        }
        return false;
    }
}