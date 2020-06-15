package petarkitanovic.androidkurs.imenik;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import petarkitanovic.androidkurs.imenik.db.Kontakt;

public class AdapterKontakt extends
        RecyclerView.Adapter<AdapterKontakt.MyViewHolder> {

    public List<Kontakt> listKontakt;
    public OnRecyclerItemClickListener listener;

    public interface OnRecyclerItemClickListener {
        void onRVItemClick(Kontakt kontakt);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvIme;
        TextView tvPrezime;
        ImageView slika;
        View view;

        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            tvIme = itemView.findViewById( R.id.tv_recycler_ime);
            tvPrezime = itemView.findViewById( R.id.tv_recycler_prezime);
            slika = itemView.findViewById(R.id.rv_image_kontakta);

        }

        public void bind(final Kontakt kontakt, final OnRecyclerItemClickListener listener) {
            tvIme.setText(kontakt.getmIme());
            tvPrezime.setText(kontakt.getmPrezime());
            slika.setImageBitmap( BitmapFactory.decodeFile(kontakt.getmSlika()));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRVItemClick(kontakt);
                }
            });
        }
    }

    public AdapterKontakt(List<Kontakt> listKontakt, OnRecyclerItemClickListener listener) {
        this.listKontakt = listKontakt;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_single_item, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        myViewHolder.bind(listKontakt.get(i), listener);

        final Kontakt glumac = listKontakt.get(i);

    }

    @Override
    public int getItemCount() {
        return listKontakt.size();
    }


    public void addGlumac(Kontakt kontakt) {
        listKontakt.add(kontakt);
        notifyDataSetChanged();
    }
}
