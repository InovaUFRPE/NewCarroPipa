package com.inovaufrpe.carropipa;

import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inovaufrpe.carropipa.adapter.PedidosAdapter;
import com.inovaufrpe.carropipa.model.Pedido;
import com.inovaufrpe.carropipa.utils.Conexao;
import com.inovaufrpe.carropipa.utils.Sessao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeCaminhoneiroActivity extends AppCompatActivity {

    private JSONObject usuario;
    private JSONObject cliente;
    private RecyclerView recyclerView;
    private PedidosAdapter adapter;
    private TextView olaUsuario;
    private TextView strSemServico;
    private TextView tvDinheiro;
    private Thread verificarPedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caminhoneiro_home);
        checkPermission();

        recyclerView = findViewById(R.id.recycle);
        olaUsuario = (TextView) findViewById(R.id.txtViewOla);
        strSemServico = findViewById(R.id.tvNenhumPedido);
        tvDinheiro = findViewById(R.id.txtViewDinheiro);


        try {
            recuperaInformacoes();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //inicia a Thread que fica verificando os pedidos
        verificarPedidos = new ThreadVerificarPedidos();
        verificarPedidos.start();
        ArrayList<Pedido> ar = new ArrayList<Pedido>();
        initRecycler(ar);
    }

    public void recuperaInformacoes() throws Exception{
        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        String info = dados.getString("dados login");
        usuario = new JSONObject(info);
        Sessao.usuario = usuario;
        Log.i("id",usuario.toString());
        new HomeCaminhoneiroActivity.RequestRecupera().execute();

    }

    public void checkPermission(){
        boolean fineLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED;
        boolean coarseLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED;
        boolean internet = ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED;
        boolean exStorage = ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String[] permissoes = {Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
            if (fineLocation || coarseLocation || internet || exStorage){
                requestPermissions(permissoes,1);
            }
        }
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                String[] permissoes = {Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissoes, 1);
            }
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // Se a solicitação de permissão foi cancelada o array vem vazio.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.recreate();

                }

            }
        }
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
                    Sessao.cliente = cliente;
                    Log.i("id",cliente.toString());
                    olaUsuario.setText("Olá, " + cliente.getString("nomerazaosocial"));
                    tvDinheiro.setText("R$ "+ (int) cliente.getDouble("dinheiro") +",00");
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
            String url = "http://api-carro-pipa.herokuapp.com/pedidosnaoaceitos/";
            return Conexao.recuperainfo(url);
        }
        protected void onPostExecute(String result){
            if (result.equals("NOT FOUND")){
                Log.i("Erro: ","erro");
            }
            else{
                //AQUI ELE PEGA O RETORNO DA REQUEST DOS PEDIDOS, MANDAR PRA LISTVIEW
                //Log.i("sucesso", result);

                try {
                    JSONArray jsonArray = new JSONArray(result);
                    initRecycler(criaLista(jsonArray));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private ArrayList<Pedido> criaLista(JSONArray jsonArray){
        ArrayList<Pedido> list = new ArrayList<Pedido>();
        for(int i = 0; i<jsonArray.length(); i++){
            try {
                JSONObject pedido = jsonArray.getJSONObject(i);
                String checkin = pedido.getString("checkIn");


                //-26.196223,-52.689523
                Log.i("endereco",checkin);
                new HomeCaminhoneiroActivity.RequestEndereco(checkin).execute();
                list.add(new Pedido(
                        pedido.getInt("id_pedido"),
                        pedido.getInt("id_pessoa_cli"),
                        pedido.getInt("id_pessoa_mot"),
                        pedido.getInt("dataHora"),
                        pedido.getDouble("valor"),
                        pedido.getDouble("valorFrete"),
                        pedido.getString("checkIn"),
                        pedido.getBoolean("imediatoProgramado"),
                        pedido.getBoolean("confirmadoProgramado")
                        )
                );

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return list;
    }

    private class RequestEndereco extends AsyncTask<Void, Void, String> {
        String param;
        public RequestEndereco(String param){
            this.param = param;
        }
        @Override
        protected String doInBackground(Void... voids) {
            String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+param;
            return Conexao.recuperaEnd(url);
        }
        protected void onPostExecute(String result){
            if (result.equals("NOT FOUND")){
                Log.i("Erro: ","erro");
            }
            else{
                Log.i("sucesso", result);
            }
        }
    }



    private void initRecycler (ArrayList pedidos) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        String id = null;
        try {
            id= usuario.getString("id_pessoa");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new PedidosAdapter(pedidos);
        recyclerView.setAdapter(adapter);
        if (adapter.getItemCount() > 0) {
            strSemServico.setVisibility(View.GONE);
        } else {
            strSemServico.setVisibility(View.VISIBLE);
        }
    }

}
