package com.inovaufrpe.carropipa.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

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
                Toast.makeText(v.getContext(), idPedido.getText(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
}
