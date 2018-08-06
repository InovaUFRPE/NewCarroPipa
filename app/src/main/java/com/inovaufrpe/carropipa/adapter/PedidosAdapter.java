package com.inovaufrpe.carropipa.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.inovaufrpe.carropipa.R;
import com.inovaufrpe.carropipa.model.Pedido;

import java.util.ArrayList;
import java.util.List;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosListHolder> {
    private final List<Pedido> pedidos;

    public PedidosAdapter(ArrayList p) {
        pedidos = p;
    }


    @Override
    public PedidosListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PedidosListHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pedido_row, parent, false));
    }

    @Override
    public void onBindViewHolder(PedidosListHolder holder, int position) {
        holder.id = pedidos.get(position).idPedido;
        holder.pedido.setText(String.valueOf((int)pedidos.get(position).valorAgua*1000/25) + " litros");

        //alterar para endereco real
        holder.endereco.setText(pedidos.get(position).checkin);
        holder.idPedido.setText(String.valueOf(pedidos.get(position).idPedido));


    }

    @Override
    public int getItemCount() {
        return pedidos != null ? pedidos.size() : 0;
    }
}