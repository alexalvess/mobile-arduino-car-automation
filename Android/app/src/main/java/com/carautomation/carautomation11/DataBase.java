package com.carautomation.carautomation11;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Alex Alves on 09/09/2016.
 */
public class DataBase extends SQLiteOpenHelper {
    private static final String DATA_BASE = "autocar.db";
    private static final int VERSAO = 1;

    public DataBase(Context context){
        super(context, DATA_BASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE VEICULO (" +
                " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " nome TEXT NOT NULL," +
                " marca TEXT NOT NULL," +
                " anoModelo TEXT NOT NULL," +
                " anoFabricacao TEXT NOT NULL);";

        db.execSQL(sql);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CLIENT");
        onCreate(db);
    }

    public ArrayList<String> Tables(){
        ArrayList<String> table = new ArrayList<String>();
        String sql = "";

        sql = " CREATE TABLE VEICULO (" +
              " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
              " nome TEXT NOT NULL," +
              " marca TEXT NOT NULL," +
              " anoModelo TEXT NOT NULL," +
              " anoFabricacao TEXT NOT NULL);";

        table.add(sql);

        sql = " CREATE TABLE BALANCEAMENTO (" +
              " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
              " idVeiculo INTEGER FOREIGN KEY" +
              " data DATETIME NOT NULL," +
              " local TEXT NOT NULL," +
              " observacao TEXT NOT NULL);";

        table.add(sql);

        sql = " CREATE TABLE ALINHAMENTO (" +
                " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " idVeiculo INTEGER FOREIGN KEY" +
                " data DATETIME NOT NULL," +
                " local TEXT NOT NULL," +
                " observacao TEXT NOT NULL);";

        table.add(sql);

        sql = " CREATE TABLE OLEO (" +
                " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " idVeiculo INTEGER FOREIGN KEY" +
                " data DATETIME NOT NULL," +
                " local TEXT NOT NULL," +
                " observacao TEXT NOT NULL);";

        table.add(sql);

        sql = " CREATE TABLE REVISAO (" +
                " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " idVeiculo INTEGER FOREIGN KEY" +
                " data DATETIME NOT NULL," +
                " local TEXT NOT NULL," +
                " observacao TEXT NOT NULL);";

        table.add(sql);

        sql = " CREATE TABLE OUTROS (" +
                " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " idVeiculo INTEGER FOREIGN KEY" +
                " data DATETIME NOT NULL," +
                " local TEXT NOT NULL," +
                " observacao TEXT NOT NULL);";

        table.add(sql);

        return table;
    }
}