package com.inovaufrpe.carropipa;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.content.Intent;
import android.location.LocationManager;
import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.inovaufrpe.carropipa.utils.Conexao;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeCaminhoneiroActivity extends AppCompatActivity {

    private JSONObject usuario;
    private JSONObject cliente;

    private TextView olaUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caminhoneiro_home);

        olaUsuario = (TextView) findViewById(R.id.txtViewOla);

        try {
            recuperaInformacoes();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //inicia a Thread que fica verificando os pedidos
        Thread t = new ThreadVerificarPedidos();
        t.start();
    }

    public void recuperaInformacoes() throws Exception{
        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        String info = dados.getString("dados login");
        usuario = new JSONObject(info);
        new HomeCaminhoneiroActivity.RequestRecupera().execute();

    }

    private class RequestRecupera extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String url = "http://api-carro-pipa.herokuapp.com/pessoas/";
            try {
                return Conexao.recuperainfo(url+ usuario.get("id_pessoa").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String result){
            if (result.equals("NOT FOUND")){
                Log.i("Erro: ","erro");
            }
            else{
                try {
                    cliente = new JSONObject(result);
                    olaUsuario.setText("Olá, " + cliente.getString("nomerazaosocial"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //essa classe quando roda fica fazendo a request a cada 5 segundos
    public class ThreadVerificarPedidos extends Thread {
        public ThreadVerificarPedidos() {}
        @Override
        public void run() {
            while(true){
                new HomeCaminhoneiroActivity.RequestVerificarPedidos().execute();
                SystemClock.sleep(5000);
            }
        }
    }

    //request que retorna os pedidos (na teoria)
    private class RequestVerificarPedidos extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            //botar a url de verificar os pedidos
            String url = "http://achai.herokuapp.com/pedidos/";
            return Conexao.recuperainfo(url);
        }
        protected void onPostExecute(String result){
            if (result.equals("NOT FOUND")){
                Log.i("Erro: ","erro");
            }
            else{
                //AQUI ELE PEGA O RETORNO DA REQUEST DOS PEDIDOS, MANDAR PRA LISTVIEW
                Log.i("sucesso",result);
            }
        }
    }

}
