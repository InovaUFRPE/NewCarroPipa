package com.inovaufrpe.carropipa;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private Button btnCancelar;

    private TextView olaUsuario;

    private JSONObject usuario;
    private JSONObject cliente;
    private JSONObject pedido;

    ProgressDialog loading;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_fisica_home);
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
        btnCancelar = findViewById(R.id.btnCancelarPedido2);


        sbLitros = findViewById(R.id.sbLitros);
        btnShowSeekBar = findViewById(R.id.btnShowSeekBar);
        btnCancelarPedido = findViewById(R.id.btnCancelarPedido);
        btnFazerPedido = findViewById(R.id.btnPedirAgua);

        tvLitros = findViewById(R.id.textViewlitros);
        sbLitros.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvLitros.setText(String.valueOf(seekBar.getProgress()*1000)+" Litros");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                tvLitros.setText(String.valueOf(seekBar.getProgress()*1000)+ " Litros");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tvLitros.setText(String.valueOf(seekBar.getProgress()*1000) + " Litros");
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
        lyPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(HomeFisicaActivity.this,MapsActivity.class);
                finish();
                startActivity(it);
            }
        });

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
        builder.setTitle("Deseja confirmar o pedido de "  + (qtd*1000) + " litros de água por R$" + qtd*25 + ",00 + taxa de entrega?");
        CharSequence[] itens = {"Localização atual", "Endereço Cadastrado"};
        builder.setSingleChoiceItems( itens, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setPositiveButton("confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    pedido.put("checkin",String.valueOf(which));
                    pedido.put("id_pessoa_cli",Integer.parseInt(cliente.getString("id_pessoa")));
                    pedido.put("id_pessoa_mot",0);
                    pedido.put("valor",qtd *25);
                    pedido.put("datahora","null");
                    pedido.put("imediatoprogramado","null");
                    pedido.put("confirmaprogramado","null");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new HomeFisicaActivity.RequestPedido().execute();
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

    //essa request faz o registro do pedido
    private class RequestPedido extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            Log.i("body: ",pedido.toString());
            String url = "http://api-carro-pipa.herokuapp.com/pedidos";
            return Conexao.pedido(url,pedido);
        }
        protected void onPostExecute(String result){
            if (result.equals("NOT FOUND")){
                Log.i("Erro: ", "erro");
            }else {
                Log.i("Sucesso: ", result);
                mostraPedido();
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
