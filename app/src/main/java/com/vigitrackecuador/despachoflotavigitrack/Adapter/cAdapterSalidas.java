package com.vigitrackecuador.despachoflotavigitrack.Adapter;

import android.app.Activity;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vigitrackecuador.despachoflotavigitrack.Interface.longClickSalidas;
import com.vigitrackecuador.despachoflotavigitrack.POO.cVueltas;
import com.vigitrackecuador.despachoflotavigitrack.R;

import java.util.ArrayList;

public class cAdapterSalidas extends RecyclerView.Adapter<cAdapterSalidas.cViewHolderSalidas>
{
    private Activity activity;
    private int resource;
    private ArrayList<cVueltas>oVueltas;
    public cAdapterSalidas(Activity activity, int resource, ArrayList<cVueltas> oVueltas)
    {
        this.activity = activity;
        this.resource = resource;
        this.oVueltas = oVueltas;
    }

    @NonNull
    @Override
    public cViewHolderSalidas onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return new cViewHolderSalidas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cViewHolderSalidas holder, int position)
    {
        cVueltas oO= oVueltas.get(position);
        if (oO.getEstaSali_m()==0)
        {
            //holder.cardView.setBackgroundColor(activity.getResources().getColor(R.color.vuelta_pendiente));
            holder.imageViewSalida.setImageDrawable(activity.getResources().getDrawable(R.drawable.blanco_bus));
        }else
            {
                //holder.cardView.setBackgroundColor(activity.getResources().getColor(R.color.vuelta_en_proceso));
                holder.imageViewSalida.setImageDrawable(activity.getResources().getDrawable(R.drawable.bus_estado_uno));
            }
        String unidad =" [ "+oO.getId_bus()+" ] ";
        String id_ruta =" # "+oO.getId_ruta();
        final long auxIdRuta = oO.getId_ruta();
        holder.textViewUnidadSalida.setText(unidad);
        holder.textViewCodeSalida.setText(id_ruta);
        String Salida="Sali : "+oO.getDate_salida().toString();
        String Llegada="Lleg : "+oO.getDate_llegada().toString();
        String LetraRuta ="Ruta : "+oO.getLetra_ruta();
        holder.textViewRutaSalida.setText(LetraRuta);
        holder.textViewHoraPSalida.setText(Salida);
        holder.textViewHoraLSalida.setText(Llegada);
        String num_vuelta =" Vuelta # "+oO.getNum_vuelta();
        holder.textViewVueltaSalida.setText(num_vuelta);
        holder.setItemLongClickListener(new longClickSalidas() {
            @Override
            public void onItemLongClick(View v, int pos)
            {
                Toast.makeText(activity, "idRuta : "+auxIdRuta, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return oVueltas.size();
    }
    class cViewHolderSalidas extends RecyclerView.ViewHolder implements View.OnLongClickListener
    {
        CardView cardView;
        LinearLayout linearLayout_card;
        TextView textViewUnidadSalida;
        TextView textViewCodeSalida;
        TextView textViewRutaSalida;
        TextView textViewHoraPSalida;
        TextView textViewHoraLSalida;
        TextView textViewVueltaSalida;
        ImageView imageViewSalida;
        longClickSalidas itemLongClickListener;
        public cViewHolderSalidas(@NonNull View itemView)
        {
            super(itemView);
            textViewUnidadSalida=itemView.findViewById(R.id.txt_unidad_salida);
            textViewCodeSalida=itemView.findViewById(R.id.txt_code_salida);
            textViewRutaSalida=itemView.findViewById(R.id.txt_ruta_salida);
            textViewHoraPSalida=itemView.findViewById(R.id.txt_horaP_salida_);
            textViewHoraLSalida=itemView.findViewById(R.id.txt_horaL_salida);
            textViewVueltaSalida=itemView.findViewById(R.id.txt_vuelta_salida);
            imageViewSalida=itemView.findViewById(R.id.imagen_salida);
            cardView=itemView.findViewById(R.id.cardview_salida);
            linearLayout_card=itemView.findViewById(R.id.linear_card);
            itemView.setOnLongClickListener(this);
        }
        public void setItemLongClickListener(longClickSalidas ic)
        {
            this.itemLongClickListener=ic;
        }
        @Override
        public boolean onLongClick(View v) {
            this.itemLongClickListener.onItemLongClick(v,getLayoutPosition());
            return false;
        }
    }
}
