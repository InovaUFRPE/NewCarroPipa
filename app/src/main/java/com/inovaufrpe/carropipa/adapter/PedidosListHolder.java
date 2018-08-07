package com.inovaufrpe.carropipa.adapter;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class PedidosListHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemClickListener {
    public int id;
    public TextView pedido;
    public TextView endereco;
    public TextView idPedido;

    public PedidosListHolder(final View itemView) {
        super(itemView);
        pedido = itemView.findViewById(R.id.tvPreco);
        endereco = itemView.findViewById(R.id.tvEndereco);
        idPedido = itemView.findViewById(R.id.tvIDPedido);

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("idpedido", idPedido.getText().toString());
                Toast.makeText(v.getContext(), idPedido.getText(), Toast.LENGTH_SHORT).show();
                Intent it = new Intent(itemView.getContext(), MapsActivity2.class);
                it.putExtras(bundle);
                v.getContext().startActivity(it);

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
}
