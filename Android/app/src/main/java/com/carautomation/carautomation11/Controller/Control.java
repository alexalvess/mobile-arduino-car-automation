package com.carautomation.carautomation11.Controller;

import java.io.Serializable;

/**
 * Created by Alex on 17/08/2016.
 */
public class Control implements Serializable{
    private int flagLimpador;
    private int flagFarolAlto;
    private int flagFarolBaixo;
    private int flagVidroAberto;
    private int flagVidroFechado;
    private int flagTrava;
    private int flagDestrava;
    private int flagIgnicao;
    private int flagPartida;

    public Control(){
        flagLimpador = 0;
        flagFarolAlto = 0;
        flagFarolBaixo = 0;
        flagVidroAberto = 0;
        flagVidroFechado = 1;
        flagTrava = 1;
        flagDestrava = 0;
        flagIgnicao = 0;
        flagPartida = 0;
    }

    public int getFlagLimpador() {
        return flagLimpador;
    }

    public void setFlagLimpador(int flagLimpador) {
        this.flagLimpador = flagLimpador;
    }

    public int getFlagFarolAlto() {
        return flagFarolAlto;
    }

    public void setFlagFarolAlto(int flagFarolAlto) {
        this.flagFarolAlto = flagFarolAlto;
    }

    public int getFlagFarolBaixo() {
        return flagFarolBaixo;
    }

    public void setFlagFarolBaixo(int flagFarolBaixo) {
        this.flagFarolBaixo = flagFarolBaixo;
    }

    public int getFlagVidroAberto() {
        return flagVidroAberto;
    }

    public void setFlagVidroAberto(int flagVidroAberto) {
        this.flagVidroAberto = flagVidroAberto;
    }

    public int getFlagVidroFechado() {
        return flagVidroFechado;
    }

    public void setFlagVidroFechado(int flagVidroFechado) {
        this.flagVidroFechado = flagVidroFechado;
    }

    public int getFlagTrava() {
        return flagTrava;
    }

    public void setFlagTrava(int flagTrava) {
        this.flagTrava = flagTrava;
    }

    public int getFlagDestrava() {
        return flagDestrava;
    }

    public void setFlagDestrava(int flagDestrava) {
        this.flagDestrava = flagDestrava;
    }

    public int getFlagIgnicao() {
        return flagIgnicao;
    }

    public void setFlagIgnicao(int flagIgnicao) {
        this.flagIgnicao = flagIgnicao;
    }

    public int getFlagPartida() {
        return flagPartida;
    }

    public void setFlagPartida(int flagPartida) {
        this.flagPartida = flagPartida;
    }
}
