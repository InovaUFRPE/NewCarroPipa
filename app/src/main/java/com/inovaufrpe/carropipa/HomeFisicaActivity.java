package com.inovaufrpe.carropipa;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.inovaufrpe.carropipa.utils.Conexao;
import com.inovaufrpe.carropipa.utils.Sessao;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;


public class HomeFisicaActivity extends AppCompatActivity {

    private LinearLayout lySeekBarLitros;
    private LinearLayout lyPedirAgua;
    private LinearLayout lyPedido;
    private Button btnShowSeekBar;
    private Button btnCancelarPedido;
    private Button btnFazerPedido;
    private SeekBar sbLitros;

    private TextView tvLitros;
    private TextView tvPreco;
    private TextView tvQtd;
    private TextView tvStatus;
    private TextView tvDinheiro;
    private Button btnCancelar;
    private Button btnVerMapa;

    private TextView olaUsuario;

    private JSONObject usuario;
    private JSONObject cliente;
    private JSONObject pedido;

    Thread threadPedido;

    ProgressDialog loading;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_fisica_home);
        checkPermission();
        try {
            recuperaInformacoes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        loading = new ProgressDialog(HomeFisicaActivity.this);
        loading.setMessage("Solicitando...");
        olaUsuario = findViewById(R.id.txtViewOla);

        lySeekBarLitros = findViewById(R.id.lySeekBarLitros);
        lyPedirAgua = findViewById(R.id.lyPedirAgua);
        lyPedido = findViewById(R.id.LyPedidoFeito);

        tvQtd = findViewById(R.id.tvQtd);
        tvPreco = findViewById(R.id.tvPreço);
        tvStatus = findViewById(R.id.tvStatus);
        tvDinheiro = findViewById(R.id.txtViewDinheiro);
        btnCancelar = findViewById(R.id.btnCancelarPedido2);


        sbLitros = findViewById(R.id.sbLitros);
        btnShowSeekBar = findViewById(R.id.btnShowSeekBar);
        btnCancelarPedido = findViewById(R.id.btnCancelarPedido);
        btnFazerPedido = findViewById(R.id.btnPedirAgua);
        btnVerMapa = findViewById(R.id.btnVerMapa);

        tvLitros = findViewById(R.id.textViewlitros);
        sbLitros.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvLitros.setText(String.valueOf(seekBar.getProgress() * 1000) + " Litros");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                tvLitros.setText(String.valueOf(seekBar.getProgress() * 1000) + " Litros");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tvLitros.setText(String.valueOf(seekBar.getProgress() * 1000) + " Litros");
            }
        });

        btnShowSeekBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alternaLayoutPedirEscolher(view);
            }
        });

        btnFazerPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarSolicitacao();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelarSolicitacao();
            }
        });
        btnVerMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(HomeFisicaActivity.this, MapsActivity.class);
                finish();
                startActivity(it);
            }
        });


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

    public void recuperaInformacoes() throws Exception{
        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        String info = dados.getString("dados login");
        usuario = new JSONObject(info);
        new HomeFisicaActivity.RequestRecupera().execute();
    }

    public void alternaLayoutPedirEscolher(View v){
        if (lyPedirAgua.getVisibility() == View.VISIBLE){
            lyPedirAgua.setVisibility(View.GONE);
            lySeekBarLitros.setVisibility(View.VISIBLE);
        } else {
            lyPedirAgua.setVisibility(View.VISIBLE);
            lySeekBarLitros.setVisibility(View.GONE);
        }
    }

    private void confirmarSolicitacao(){
        pedido = new JSONObject();
        final Integer qtd = sbLitros.getProgress();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar pedido de "  + (qtd*1000) + " litros por R$" + qtd*25 + ",00 + taxa de entrega?");
        CharSequence[] itens = {"Localização atual"};
        builder.setSingleChoiceItems( itens, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setPositiveButton("confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkPermission();
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                String bestProvider = locationManager.getBestProvider(new Criteria(),true);
                Location location = locationManager.getLastKnownLocation(bestProvider);
                String latlong;
                if (location != null){

                    Log.i("lat", String.valueOf(location.getLatitude()));
                    Log.i("lon", String.valueOf(location.getLongitude()));
                    latlong = "lat="+String.valueOf(location.getLatitude())+"&lon="+String.valueOf(location.getLongitude());

                } else {
                    latlong = "null";
                }
                try {
                    pedido.put("checkIn",latlong);
                    pedido.put("id_pessoa_cli",Integer.parseInt(cliente.getString("id_pessoa")));
                    pedido.put("id_pessoa_mot",0);
                    pedido.put("valor",qtd *25);
                    pedido.put("dataHora",new Timestamp(System.currentTimeMillis()).getTime());
                    pedido.put("imediatoProgramado",true);
                    pedido.put("confirmadoProgramado",false);
                    pedido.put("valorFrete",0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new HomeFisicaActivity.RequestPedido().execute();
                btnCancelarPedido.setVisibility(View.VISIBLE);
                loading.show();


            }
        });
        builder.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }


    //https://nominatim.openstreetmap.org/reverse?format=json&lat=-8.01765&lon=-34.9444&zoom=18&addressdetails=1


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
                    tvDinheiro.setText("R$ "+ (int) cliente.getDouble("dinheiro") +",00");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class ThreadVerificaPedido extends Thread {
        public ThreadVerificaPedido() {}
        @Override
        public void run() {
            while(true){
                new HomeFisicaActivity.RequestVerificaPedido().execute();
                SystemClock.sleep(5000);
            }
        }
    }

    private class RequestVerificaPedido extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            //botar a url de verificar os pedidos
            String url = null;
            try {
                url = "http://api-carro-pipa.herokuapp.com/pedidos/"+ pedido.getInt("id_pedido");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Conexao.recuperainfo(url);
        }
        protected void onPostExecute(String result){
            if (result.equals("NOT FOUND")){
                Log.i("Erro: ","erro");
            }
            else{
                //AQUI ELE PEGA O RETORNO DA REQUEST DOS PEDIDOS, MANDAR PRA LISTVIEW
                try {
                    JSONObject pedido = new JSONObject(result);
                    if (pedido.getInt("id_pessoa_mot") == 0){
                        Log.i("PEDIDO",pedido.toString());
                    } else {
                        aceitouPedido();
                        threadPedido.interrupt();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void aceitouPedido(){
        btnVerMapa.setVisibility(View.VISIBLE);
        btnCancelar.setVisibility(View.GONE);
        tvStatus.setText("Status: A caminho");
    }

    //essa request faz o registro do pedido
    private class RequestPedido extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            //Log.i("body: ",pedido.toString());
            String url = "http://api-carro-pipa.herokuapp.com/pedidos";
            return Conexao.pedido(url,pedido);
        }
        protected void onPostExecute(String result){
            if (result.equals("NOT FOUND")){
                //Log.i("Erro: ", "erro");
            }else {
                try {
                    JSONObject json = new JSONObject(result);
                    Log.i("Sucesso: ", json.getString("id_pedido"));
                    pedido.put("id_pedido",json.getString("id_pedido"));
                    Sessao.pedido = pedido;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mostraPedido();
                threadPedido = new HomeFisicaActivity.ThreadVerificaPedido();
                threadPedido.start();
            }

        }
    }

    public void mostraPedido(){
        lyPedido.setVisibility(View.VISIBLE);
        lyPedirAgua.setVisibility(View.GONE);
        lySeekBarLitros.setVisibility(View.GONE);
        try {
            tvPreco.setText("R$"+pedido.getString("valor")+",00");
            int qtd = (pedido.getInt("valor")/25)*1000;
            tvQtd.setText(String.valueOf(qtd)+ " Litros") ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        loading.dismiss();
    }

    public void cancelarSolicitacao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Você deseja realmente cancelar o pedido?");
        builder.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                lyPedido.setVisibility(View.GONE);
                lyPedirAgua.setVisibility(View.VISIBLE);
                lySeekBarLitros.setVisibility(View.GONE);
            }
        });
        builder.setNegativeButton("Manter pedido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }


}
