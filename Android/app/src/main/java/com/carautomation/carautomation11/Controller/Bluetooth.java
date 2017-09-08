package com.carautomation.carautomation11.Controller;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Alex on 29/06/2016.
 */
public class Bluetooth {
    private static final int REQUEST_ENABLE_BT = 0;
    private static final boolean DEBUG = false;
    private static final String TAG = "CarAutomation";

    private BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothSocket btSocket;
    private BluetoothDevice btDevice;
    private OutputStream btOutputStream;
    private InputStream btInputStream;
    private Thread worker;
    private byte[] readBuffer;
    private int readBufferPosition;
    private volatile boolean stopWorker;
    private Thread workerThread;
    private OutputStream mmOutputStream;
    private InputStream mmInputStream;

    public void ativaBluetooth(){
        habilitarBluetooth();
        parearDispositivo();
        try {
            abreConexaoBluetooth();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void habilitarBluetooth(){
        if (this.btAdapter == null) {
            Log.i("Bluetooth -> ", "Erro ao Ativar");
        }
        else {
            if(!btAdapter.isEnabled()) {
                this.btAdapter.enable();
                Log.i("Bluetooth -> ", "Bluetooth Ativado");
            } else {
                Log.i("Bluetooth -> ", "Bluetooth Ativado");
            }
        }
    }

    public void parearDispositivo(){
        try{
            Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
            if(pairedDevices.size() > 0){
                Log.i("Bluetooth -> ", "Buscando dispositivos...");
                for (BluetoothDevice device : pairedDevices){
                    if (device.getName().equals("HC-05")){
                        Log.i("Bluetooth -> ", "Bluetooth Pareado");
                        this.btDevice = device;
                        break;
                    }
                }
            }
        }catch (Exception ex){
            Log.i("Bluetooth -> ", "Erro ao Parear Bluetooth - [" + ex.getMessage() + "]");
        }
    }

    public void abreConexaoBluetooth() throws IOException{
        try{
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            this.btSocket = this.btDevice.createRfcommSocketToServiceRecord(uuid);

            this.btAdapter.cancelDiscovery();

            verificaConexao();

            this.btOutputStream = this.btSocket.getOutputStream();
            this.btInputStream = this.btSocket.getInputStream();
            Log.i("Bluetooth -> ", "Conexão Bluetooth Aberta");
        } catch (IOException ex) {
            Log.i("Bluetooth -> ", "Erro ao abrir conexão - [" + ex.getMessage() + "]");
        }
    }

    public void verificaConexao(){
        if(!btSocket.isConnected()) {
            try {
                this.btSocket.connect();
                Log.i("Bluetooth -> ", "Abrindo Conexão");
            } catch (IOException ex) {
                Log.i("Bluetooth -> ", "Erro ao Abrir Conexão - [" + ex.getMessage() + "]");
            }
        }
    }

    public String enviaDados(String funcionalidade, String mensagem){
        ArrayList<String> variaveis = verificaFuncionalidade(funcionalidade, mensagem);

        try {
            this.btOutputStream.write(variaveis.get(0).getBytes());
            Log.i("Bluetooth -> ", "Enviando " + variaveis.get(1) + "- [" + variaveis.get(2) + "]");
            return variaveis.get(2);
        } catch (IOException ex) {
            Log.i("Bluetooth -> ", "Erro ao Enviar Dados - [" + variaveis.get(1) + "] - [" + ex.getMessage() + "]");
            return "Erro ao Executar Ação - [" + ex.getMessage() + "]";
        }
    }

    public ArrayList<String> verificaFuncionalidade(String funcionalidade, String mensagem){
        ArrayList<String> variaveis = new ArrayList<String>();
        String dados = "";
        String valor = "";
        String acao = "";

        switch (funcionalidade){
            case "vidro":
                switch (mensagem){
                    case "abrir_vidro":
                        dados = "1\n";
                        valor = "1";
                        acao = "Abrir Vidros";
                        break;
                    case "fechar_vidro":
                        dados = "2\n";
                        valor = "2";
                        acao = "Fechar Vidros";
                        break;
                }
                break;
            case "trava":
                switch (mensagem){
                    case "travar":
                        dados = "3\n";
                        valor = "3";
                        acao = "Travar Veículo";
                        break;
                    case "destravar":
                        dados = "4\n";
                        valor = "4";
                        acao = "Destravar Veículo";
                        break;
                }
                break;
            case "para_brisa":
                switch (mensagem){
                    case "media":
                        dados = "5\n";
                        valor = "5";
                        acao = "Ativar Limpador - Velocidade Média";
                        break;
                    case "desativar":
                        dados = "5\n";
                        valor = "5";
                        acao = "Desativar Limpador";
                        break;
                }
                break;
            case "ignicao":
                switch (mensagem){
                    case "desligar":
                        dados = "7\n";
                        valor = "7";
                        acao = "Desativando Ignição";
                        break;
                    case "ligar":
                        dados = "8\n";
                        valor = "8";
                        acao = "Ativando Partida";
                        break;
                    case "chave":
                        dados = "9\n";
                        valor = "9";
                        acao = "Acionando Ignição";
                        break;
                }
                break;
            case "farol":
                switch (mensagem){
                    case "alto":
                        dados = "A\n";
                        valor = "A";
                        acao = "Farol Alto";
                        break;
                    case "normal":
                        dados = "B\n";
                        valor = "B";
                        acao = "Farol Normal";
                        break;
                    case "desativarAlto":
                        dados = "C\n";
                        valor = "C";
                        acao = "Desativar Farol Alta";
                        break;
                    case "desativarBaixo":
                        dados = "D\n";
                        valor = "D";
                        acao = "Desativar Farol Baixo";
                        break;
                }
                break;
            case "busina":
                switch (mensagem){
                    case "acionar":
                        dados = "E\n";
                        valor = "E";
                        acao = "Ativando Busina";
                        break;
                }
        }

        variaveis.add(dados);
        variaveis.add(valor);
        variaveis.add(acao);

        return variaveis;
    }

    public void server() {
        final Handler handler = new Handler();
        final byte delimiter = 10;

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable() {
            public void run() {
                while(!Thread.currentThread().isInterrupted() && !stopWorker) {
                    try {
                        int bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++) {
                                byte b = packetBytes[i];
                                if(b == delimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable() {
                                        public void run() {
                                            Log.e(TAG,data);
                                        }
                                    });
                                }
                                else readBuffer[readBufferPosition++] = b;
                            }
                        }
                    } catch (IOException ex) {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }

    public void fechaConexaoBluetooth() throws IOException, InterruptedException{
        try{
            btOutputStream.close();
            btInputStream.close();
            Thread.sleep(1000);
            Log.i("Bluetooth -> ", "Conexão Fechada");
        }catch (Exception ex){
            Log.i("Bluetooth -> ", "Erro ao Fechar Conexão - [" + ex.getMessage() + "]");
        }
    }

    public void desabilitarBluetooth(){
        if(btAdapter.isEnabled()){
            this.btAdapter.disable();
            Log.i("Bluetooth -> ", "Bluetooth Desativado");
        }
    }
}