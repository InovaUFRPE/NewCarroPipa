package com.inovaufrpe.carropipa.utils;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.inovaufrpe.carropipa.LoginActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Felipe on 26/11/2017.
 * cria conexão ao servidor externo
 */

public class Conexao {

    public static String cadastro(String urlLogin, JSONObject json){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try{
            URL url = new URL(urlLogin);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/json");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.connect();


            OutputStreamWriter streamWriter = new OutputStreamWriter(urlConnection.getOutputStream());
            streamWriter.write(json.toString());
            streamWriter.flush();

            StringBuilder stringBuilder = new StringBuilder();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStreamReader streamReader = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                String response = null;
                while((response = bufferedReader.readLine())!=null){
                    stringBuilder.append(response+"\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            } else {
                Log.i("teste1",urlConnection.getResponseMessage());
                //Toast.makeText(LoginActivity.this, "Informações incorretas", Toast.LENGTH_SHORT).show();
                return urlConnection.getResponseMessage();
            }
        } catch (Exception e) {
            Log.i("teste2",e.toString());
            return null;
        }
    }

    public static String login(String urlLogin, JSONObject json){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try{
            URL url = new URL(urlLogin);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/json");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.connect();


            OutputStreamWriter streamWriter = new OutputStreamWriter(urlConnection.getOutputStream());
            streamWriter.write(json.toString());
            streamWriter.flush();

            StringBuilder stringBuilder = new StringBuilder();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStreamReader streamReader = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                String response = null;
                while((response = bufferedReader.readLine())!=null){
                    stringBuilder.append(response+"\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            } else {
                Log.i("teste1",urlConnection.getResponseMessage());
                //Toast.makeText(LoginActivity.this, "Informações incorretas", Toast.LENGTH_SHORT).show();
                return urlConnection.getResponseMessage();
            }
        } catch (Exception e) {
            Log.i("teste2",e.toString());
            return null;
        }
    }

    public static String recuperainfo(String urlLogin){
        HttpURLConnection urlConnection;
        BufferedReader reader;
        try{
            URL url = new URL(urlLogin);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setAllowUserInteraction(false);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            StringBuilder stringBuilder = new StringBuilder();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStreamReader streamReader = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                String response = null;
                while((response = bufferedReader.readLine())!=null){
                    stringBuilder.append(response+"\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            } else {
                Log.i("sucesso", String.valueOf(urlConnection.getResponseCode()));
                return urlConnection.getResponseMessage();
            }
        } catch (Exception e) {
            Log.i("erro",e.toString());
            return null;
        }
    }
}
