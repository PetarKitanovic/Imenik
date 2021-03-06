package petarkitanovic.androidkurs.imenik;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import petarkitanovic.androidkurs.imenik.db.Broj;

public class AdapterBrojeva extends RecyclerView.Adapter<AdapterBrojeva.MyViewHolder> {

    private List<Broj> rv_lista;

    public AdapterBrojeva(List<Broj> rv_lista) {
        this.rv_lista = rv_lista;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView category;
        TextView number;
        ImageView send;
        View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            category = itemView.findViewById( R.id.rv_broj_phone_category);
            number = itemView.findViewById( R.id.rv_broj_phone);
            send = itemView.findViewById(R.id.send_btn);
        }

        public void bind(final Broj broj) {
            category.setText(broj.getmKategorija());
            this.number.setText(broj.getmBroj());
            this.number.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tools.dialPhoneNumber( MyViewHolder.this.number.getText().toString(),itemView.getContext());
                }
            });
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tools.sendMessege(MyViewHolder.this.number.getText().toString(),itemView.getContext());

                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.rv_single_item_broj,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.bind(rv_lista.get(i));

    }

    @Override
    public int getItemCount() {
        return rv_lista.size();
    }

    public void clear(){
        rv_lista.clear();
    }

    public void addAll(List<Broj> slikeList){
        rv_lista.addAll(slikeList);
    }


}
