package com.inovaufrpe.carropipa.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.inovaufrpe.carropipa.MapsActivity;
import com.inovaufrpe.carropipa.MapsActivity2;
import com.inovaufrpe.carropipa.R;
import com.inovaufrpe.carropipa.model.Pedido;
import com.inovaufrpe.carropipa.utils.Sessao;

import java.util.ArrayList;
import java.util.List;

public class PedidosListHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemClickListener {

    public TextView pedido;
    public TextView endereco;
    public TextView idPedido;
    private View vw;

    public PedidosListHolder(final View itemView) {
        super(itemView);
        pedido = itemView.findViewById(R.id.tvPreco);
        endereco = itemView.findViewById(R.id.tvEndereco);
        idPedido = itemView.findViewById(R.id.tvIDPedido);


        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                vw = v;
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Deseja realizar essa entrega?");
                builder.setPositiveButton("Aceitar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Sessao.idPedido = Integer.parseInt(idPedido.getText().toString());
                        Intent it = new Intent(itemView.getContext(), MapsActivity2.class);
                        vw.getContext().startActivity(it);
                    }
                });
                builder.create();
                builder.show();

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
}
