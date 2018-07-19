package com.inovaufrpe.carropipa;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class HomeFisicaActivity extends AppCompatActivity {

    private LinearLayout lySeekBarLitros;
    private LinearLayout lyPedirAgua;
    private Button btnShowSeekBar;
    private Button btnCancelarPedido;
    private Button btnFazerPedido;
    private SeekBar sbLitros;
    private TextView tvLitros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_fisica_home);
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
                tvLitros.setText(String.valueOf(seekBar.getProgress())+" Litros");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                tvLitros.setText(String.valueOf(seekBar.getProgress())+ " Litros");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tvLitros.setText(String.valueOf(seekBar.getProgress()) + " Litros");
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
                createDialog();
            }
        });
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

    private void createDialog(){
        final Integer valor = sbLitros.getProgress();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Você deseja confirmar o pedido de " + valor.toString() + " litros de água?");
        builder.setPositiveButton("confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                solicitarCaminhao(valor);
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

    private void solicitarCaminhao(Integer litros){
        Toast.makeText(this, litros.toString(), Toast.LENGTH_SHORT).show();
    }
}
