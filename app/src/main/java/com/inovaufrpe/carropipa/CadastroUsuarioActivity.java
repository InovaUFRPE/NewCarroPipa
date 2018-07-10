package com.inovaufrpe.carropipa;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.inovaufrpe.carropipa.utils.Conexao;
import com.inovaufrpe.carropipa.utils.Masks;
import com.inovaufrpe.carropipa.utils.Validacao;

import org.json.JSONException;
import org.json.JSONObject;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText edtNome,edtCpf,edtEmail,edtSenha,edtConfirmacao,edtTelefone;
    private Button btnCadastrar;
    private Switch swtTipo;
    JSONObject usuario;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_cadastro_usuario);

        edtNome = findViewById(R.id.editTextNome);
        edtEmail = findViewById(R.id.editTextEmail);
        edtTelefone = findViewById(R.id.editTextTelefone);
        edtCpf = findViewById(R.id.editTextCpf);
        edtSenha = findViewById(R.id.editTextSenha);
        edtConfirmacao = findViewById(R.id.editTextConfirmacao);
        edtTelefone.addTextChangedListener(Masks.insert(edtTelefone,Masks.maskTELEFONE));

        edtCpf.setFilters(new InputFilter[] {new InputFilter.LengthFilter(14)});
        edtCpf.addTextChangedListener(Masks.insert(edtCpf));

        swtTipo = findViewById(R.id.switch1);
        swtTipo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mudarCampos(isChecked);
            }
        });

        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarCampos();
            }
        });

    }

    //Muda os campos de acordo com a escolha entre Cliente e motorista
    private void mudarCampos(boolean status){
        if (status){
            edtNome.setHint("Razão Social");
            edtCpf.setHint("CNPJ");
            edtCpf.setFilters(new InputFilter[] {new InputFilter.LengthFilter(18)});

        }else{
            edtNome.setHint("Nome");
            edtCpf.setHint("CPF");
            edtCpf.setText("");
            edtCpf.setFilters(new InputFilter[] {new InputFilter.LengthFilter(14)});
        }
    }

    //Valida todos os campos
    private void validarCampos(){
        boolean isValid = true;
        Validacao validacao = new Validacao();

        String nome = edtNome.getText().toString();
        String cpf = edtCpf.getText().toString();
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();
        String telefone = edtTelefone.getText().toString();
        String confirmacao = edtConfirmacao.getText().toString();

        if(!validacao.validaCaracteres(nome) && swtTipo.isChecked()){
            edtNome.setError("Razão social inválida");
            isValid = false;
        }
        if(!validacao.validaCaracteres(nome) && !swtTipo.isChecked()){
            edtNome.setError("Nome inválido");
            isValid = false;
        }
        if (telefone.length() != 15){
            edtTelefone.setError("Telefone inválido");
            isValid = false;
        }
        if(swtTipo.isChecked()){
            if(cpf.length() != 18){
                edtCpf.setError("CNPJ inválido");
                isValid = false;
            }
        }else {
            if(cpf.length() != 14){
                edtCpf.setError("CPF inválido");
                isValid = false;
            }
        }
        if(!validacao.validaEmail(email)){
            edtEmail.setError("Email inválido");
            isValid = false;
        }
        if(!senha.equals(confirmacao)){
            edtSenha.setError("Senha e confirmação não coincidem");
            isValid = false;
        } else {
            if (senha.length() < 6){
                edtSenha.setError("Senha muito curta");
                isValid = false;
            }
        }
        if (isValid){
            try {
                cadastrar(nome,cpf,telefone,email,senha);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void cadastrar(String nome, String cpf, String telefone, String email, String senha) throws JSONException {

        if(!connected()){
            Toast.makeText(this, "Conecte-se a internet", Toast.LENGTH_SHORT).show();
            return;
        }
        usuario = new JSONObject();
        usuario.put("email",email);
        usuario.put("senha",senha);
        usuario.put("nomerazaosocial",nome);
        usuario.put("foto","");
        usuario.put("telefone",telefone);
        usuario.put("tipopessoa","");
        usuario.put("cpfcnpj",cpf);
        usuario.put("logradouro","");
        usuario.put("complemento","");
        usuario.put("bairro","");
        usuario.put("cidade","");
        usuario.put("cep","");
        usuario.put("uf","");

        if (swtTipo.isChecked()){
            usuario.put("tipopessoa","motorista");
            url = "http://api-carro-pipa.herokuapp.com/motoristas";
        } else {
            usuario.put("tipopessoa","cliente");
            url = "http://api-carro-pipa.herokuapp.com/clientes";

        }

        new Request().execute();
    }

    public boolean connected(){
        ConnectivityManager conexao = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = conexao.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private class Request extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return Conexao.cadastro(url,usuario);
        }

        protected void onPostExecute(String result){
            //mensagem "cliente cadastrado com sucesso , sucesso:true"
            //mensagem "cliente inexistente", sucesso:false"
            Log.i("AQUI",result);
            if(result.equals("CREATED")){
                if (swtTipo.isChecked()){
                    Toast.makeText(CadastroUsuarioActivity.this, "Motorista cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CadastroUsuarioActivity.this, "Cliente cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                }

                Intent it = new Intent(CadastroUsuarioActivity.this,LoginActivity.class);
                finish();
                startActivity(it);
            } else {
                Toast.makeText(CadastroUsuarioActivity.this, "Informações invalidas", Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(CadastroUsuarioActivity.this,LoginActivity.class);
        finish();
        startActivity(it);
    }
}
