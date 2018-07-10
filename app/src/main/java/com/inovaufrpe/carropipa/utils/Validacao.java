package com.inovaufrpe.carropipa.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validacao {
    //crio um padrão que acha qualquer caracter que não seja de A-Z
    public Pattern letras = Pattern.compile("[^A-Za-z ]{1,1}");
    //crio um padrão que acha espaço em branco
    public Pattern espaco = Pattern.compile(" {1,1}");


    //TODAS AS FUNÇÔES RETORNAM TRUE CASO O CAMPO ESTEJA VALIDO, E FALSE CASO CONTRARIO


    //função que verifica se há caracteres especiais na String
    public boolean validaCaracteres(String input){
        Pattern padrao = Pattern.compile("[^A-Za-z0-9 ]{1,1}");
        Matcher caracterespecial = padrao.matcher(input);
        if (caracterespecial.find()){
            return false;
        }else{
            return true;
        }
    }

    //função que acha espaços no começo e no final da string
    public boolean validaEspaco(String input){
        if (input.charAt(0) == ' ' || input.charAt(input.length()-1)== ' '){
            return false;
        }else{
            return true;
        }
    }

    //verifica se o campo ta vazio
    public boolean validaCampo(String input){
        if (input == "" || input == null){
            return false;
        }else{
            return true;
        }
    }

    //valida o login
    public boolean validaLogin(String login){
        Matcher caracterespecial = letras.matcher(login);
        Matcher comespaco = espaco.matcher(login);
        //vê se o tamanho ta certo
        if(login.length()>5 & login.length()<11){
            if(!comespaco.find()){
                if(!caracterespecial.find()){
                    return true;
                }
            }
        }
        return false;
    }


    public boolean validaSenha(String senha){
        Matcher caracterespecial = letras.matcher(senha);
        Matcher comespaco = espaco.matcher(senha);
        if(senha.length()>8 & senha.length()<16){
            if(!comespaco.find()){
                if(!caracterespecial.find()){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean validaEmail(String email){
        Pattern formemail = Pattern.compile("^[a-zA-Z0-9_\\.-]+@([a-zA-Z0-9]\\.)*([a-zA-Z0-9])*\\.([a-zA-Z])+");
        Matcher formatado = formemail.matcher(email);
        Matcher comespaco = espaco.matcher(email);
        if(formatado.find()){
            if(!comespaco.find()){
                return true;
            }
        }
        return false;
    }


    public boolean validaPlaca(String placa){
        Pattern formplaca = Pattern.compile("[A-Za-z]{3,3}-[0-9]{4,4}");
        Matcher formatado = formplaca.matcher(placa);
        if(placa.length()== 8){
            if(formatado.find()){
                return true;
            }
        }
        return false;
    }

    public boolean validaCep(String cep){
        Pattern formcep = Pattern.compile("[0-9]{5,5}-[0-9]{3,3}");
        Matcher formatado = formcep.matcher(cep);
        if(cep.length()==9){
            if (formatado.find()){
                return true;
            }
        }
        return false;

    }
}