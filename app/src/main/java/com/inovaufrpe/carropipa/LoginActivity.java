package com.inovaufrpe.carropipa;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.inovaufrpe.carropipa.utils.Conexao;
import com.inovaufrpe.carropipa.utils.Validacao;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {


    private EditText edtLogin, edtSenha;
    private Button btnEntrar,btnCadastrar;

    private Switch swtTipo;
    private static final int NOTIFICATION_PERMISSION_CODE = 123;
    JSONObject json;

    String url;

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
                validar();
                /*Intent it = new Intent(LoginActivity.this,MapsActivity2.class);
                finish();
                startActivity(it);*/
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
        requestNotificationPermission();
    }

    private void requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY)) {

        }

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY}, NOTIFICATION_PERMISSION_CODE );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == NOTIFICATION_PERMISSION_CODE ) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void validar(){
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
            try {
                enviaInfo(login, senha);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void enviaInfo(String login, String senha) throws JSONException {
        if(!connected()){
            Toast.makeText(this, "Conecte-se a internet", Toast.LENGTH_SHORT).show();
            return;
        }

        json = new JSONObject();
        json.put("email",edtLogin.getText().toString());
        json.put("senha",edtSenha.getText().toString());
        url = "http://api-carro-pipa.herokuapp.com/login";
        if (swtTipo.isChecked()){
            json.put("tipopessoa","motorista");
        } else {
            json.put("tipopessoa","cliente");
        }
        new Request().execute();
    }

    private class Request extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return Conexao.login(url,json);
        }

        protected void onPostExecute(String result){
            if (result.equals("NOT FOUND")){
                Toast.makeText(LoginActivity.this, "Informações incorretas", Toast.LENGTH_SHORT).show();
            }
            else{
                Bundle bundle = new Bundle();
                bundle.putString("dados login",result);
                try {
                    JSONObject json = new JSONObject(result);
                    new LoginActivity.Request2(json.getString("id_pessoa")).execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*if (swtTipo.isChecked()){
                    Intent it = new Intent(LoginActivity.this,HomeCaminhoneiroActivity.class);
                    it.putExtras(bundle);
                    finish();
                    startActivity(it);
                } else {
                    Intent it = new Intent(LoginActivity.this,HomeFisicaActivity.class);
                    it.putExtras(bundle);
                    finish();
                    startActivity(it);
                }*/
            }
        }
    }

    private class Request2 extends AsyncTask<Void, Void, String> {

        String params;

        public Request2(String url){
            this.params = url;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return Conexao.localizacao("http://api-carro-pipa.herokuapp.com/pessoas/"+this.params);
        }

        protected void onPostExecute(String result){
            if (result.equals("NOT FOUND")){
                Toast.makeText(LoginActivity.this, "Informações incorretas", Toast.LENGTH_SHORT).show();
            }
            else{
                Bundle bundle = new Bundle();
                bundle.putString("dados login",result);
                try {
                    JSONObject json = new JSONObject(result);
                    String tipo = json.getString("tipopessoa");
                    if (swtTipo.isChecked() && tipo.equals("motorista")){
                        Intent it = new Intent(LoginActivity.this,HomeCaminhoneiroActivity.class);
                        it.putExtras(bundle);
                        finish();
                        startActivity(it);
                    } else if (!swtTipo.isChecked() && tipo.equals("cliente")) {
                        Intent it = new Intent(LoginActivity.this,HomeFisicaActivity.class);
                        it.putExtras(bundle);
                        finish();
                        startActivity(it);
                    } else {
                        Toast.makeText(LoginActivity.this, "Informações incorretas", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    public boolean connected(){
        ConnectivityManager conexao = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = conexao.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
