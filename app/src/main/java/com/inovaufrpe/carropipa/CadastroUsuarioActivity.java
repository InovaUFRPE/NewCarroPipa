package com.inovaufrpe.carropipa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText edtNome,edtCpf,edtEmail,edtSenha,edtConfirmacao,edtTelefone;
    private Button btnCadastrar;
    private Switch swtTipo;

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
            if (swtTipo.isChecked()){
                cadastrarMotorista();
            } else {
                cadastrarCliente();
            }
        }
    }

    private void cadastrarCliente(){
        //chama aqui a função de jogar pra API
        Toast.makeText(this, "Cadastro de cliente concluído", Toast.LENGTH_SHORT).show();
        Intent it = new Intent(CadastroUsuarioActivity.this,LoginActivity.class);
        finish();
        startActivity(it);
    }
    private void cadastrarMotorista(){
        //chama aqui a função de jogar pra API;
        Toast.makeText(this, "Cadastro de motorista concluído", Toast.LENGTH_SHORT).show();
        Intent it = new Intent(CadastroUsuarioActivity.this,LoginActivity.class);
        finish();
        startActivity(it);
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(CadastroUsuarioActivity.this,LoginActivity.class);
        finish();
        startActivity(it);
    }
}
