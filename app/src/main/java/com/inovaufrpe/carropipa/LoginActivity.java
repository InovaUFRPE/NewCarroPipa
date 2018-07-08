package com.inovaufrpe.carropipa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText edtLogin, edtSenha;
    private Button btnEntrar,btnCadastrar;
    private Switch swtTipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        edtLogin = findViewById(R.id.editText);
        edtSenha = findViewById(R.id.editText2);
        swtTipo = findViewById(R.id.switch1);

        btnEntrar = findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logar();
            }
        });

        btnCadastrar = findViewById(R.id.btnCriar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this,CadastroUsuarioActivity.class);
                finish();
                startActivity(it);
            }
        });
    }

    private void logar(){
        Validacao validacao = new Validacao();
        String login = edtLogin.getText().toString();
        String senha = edtSenha.getText().toString();
        boolean isValid = true;
        if(login.isEmpty() || !validacao.validaEmail(login)){
            edtLogin.setError("Login inválido");
            isValid = false;
        }
        if(senha.isEmpty()){
            edtSenha.setError("Este campo não pode ser vazio");
            isValid = false;
        }
        if (isValid){
            //Toast.makeText(this, "logou", Toast.LENGTH_SHORT).show();
            Intent it = new Intent(LoginActivity.this,HomeFisicaActivity.class);
            finish();
            startActivity(it);
        }
    }


}
