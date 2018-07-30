package com.inovaufrpe.carropipa;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import java.util.ArrayList;
import java.util.Calendar;


public class HomeFisicaActivity extends AppCompatActivity {

    private LinearLayout lySeekBarLitros;
    private LinearLayout lyPedirAgua;
    private Button btnShowSeekBar;
    private Button btnCancelarPedido;
    private Button btnFazerPedido;
    private SeekBar sbLitros;
    private TextView tvLitros;

    private TextView olaUsuario;

    private JSONObject usuario;
    private JSONObject cliente;
    private JSONObject pedido;

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

        olaUsuario = findViewById(R.id.txtViewOla);

        lySeekBarLitros = findViewById(R.id.lySeekBarLitros);
        lyPedirAgua = findViewById(R.id.lyPedirAgua);



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
        final Integer qtd = sbLitros.getProgress()*1000;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Você deseja confirmar o pedido de " + qtd.toString() + " litros de água?");

        //mostra a opção de destino, se n quiser só comentar
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
                    pedido.put("checkin",null);
                    pedido.put("id_pessoa_cli",Integer.parseInt(cliente.getString("id_pessoa")));
                    pedido.put("id_pessoa_mot",null);
                    pedido.put("valor",qtd);
                    pedido.put("datahora",null);
                    pedido.put("imediatoprogramado",null);
                    pedido.put("confirmaprogramado",null);
                    // ta null pq algumas infos vão ser preenchidas quando o motorista aceitar
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                solicitar(pedido);
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


    private void solicitar(JSONObject pedido){
        //aqui chamaria a função request pedido que ia
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

    private class RequestPedido extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String url = "http://api-carro-pipa.herokuapp.com/pedidos/";
            return Conexao.pedido(url,pedido);

        }
        protected void onPostExecute(String result){
            Log.i("res: ",result);
            if (result.equals("NOT FOUND")){
            }else {

            }

        }
    }
}
