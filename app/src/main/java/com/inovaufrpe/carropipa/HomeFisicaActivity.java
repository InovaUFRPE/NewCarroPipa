package com.inovaufrpe.carropipa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class HomeFisicaActivity extends AppCompatActivity {

    private LinearLayout lySeekBarLitros;
    private LinearLayout lyPedirAgua;
    private Button btnShowSeekBar;
    private Button btnCancelarPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.fisica_activity_home);
        lySeekBarLitros = findViewById(R.id.lySeekBarLitros);
        lyPedirAgua = findViewById(R.id.lyPedirAgua);
        btnShowSeekBar = findViewById(R.id.btnShowSeekBar);
        btnCancelarPedido = findViewById(R.id.btnCancelarPedido);

        btnShowSeekBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alternaLayoutPedirEscolher(view);
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
}
