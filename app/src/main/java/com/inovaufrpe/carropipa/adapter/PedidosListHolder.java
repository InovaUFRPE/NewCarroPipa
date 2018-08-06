package com.inovaufrpe.carropipa.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inovaufrpe.carropipa.R;
import com.inovaufrpe.carropipa.model.Pedido;

import java.util.ArrayList;
import java.util.List;

public class PedidosListHolder extends RecyclerView.ViewHolder {
    public int id;
    public TextView pedido;
    public TextView endereco;

    public PedidosListHolder(final View itemView) {
        super(itemView);
        pedido = itemView.findViewById(R.id.tvPreco);
        endereco = itemView.findViewById(R.id.tvEndereco);
        id = 0;
    }
}
