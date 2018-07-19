package com.inovaufrpe.carropipa;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;




public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.ViewHolder>  {
    private List<String> pedidos;
    private Context context;
    private ItemClickListener listener;

    public PedidosAdapter(Context context, List<String> listaPedidos, ItemClickListener listener){
        this.pedidos = listaPedidos;
        this.context = context;
        this.listener = new ItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView pedido;
        private TextView status;


        public ViewHolder(View view) {
            super(view);
            pedido = view.findViewById(R.id.pedido);
            status = view.findViewById(R.id.status);
        }

        @Override
        public void onClick(View view) {
        }
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pedido_row, parent, false);
        final ViewHolder viewHolder = new ViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v , viewHolder.getPosition());
            }
        });
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder myHolder, int position) {
        String pedido = pedidos.get(position);
        Toast.makeText(context, pedido, Toast.LENGTH_SHORT).show();

    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public void insertItem(String pedido) {
        pedidos.add(pedido);
        notifyItemInserted(getItemCount());
    }
}

interface ItemClickListener {

    void onItemClick(View v, int position);

}