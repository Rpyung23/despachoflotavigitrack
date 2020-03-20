package com.vigitrackecuador.despachoflotavigitrack.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vigitrackecuador.despachoflotavigitrack.POO.cFlota;
import com.vigitrackecuador.despachoflotavigitrack.R;

import java.util.ArrayList;

public class cAdapterFlota extends RecyclerView.Adapter<cAdapterFlota.cFlotasViewHolder>
{
    private ArrayList<cFlota>oFlotas;
    private int resource;
    private Activity activity;
    public cAdapterFlota(ArrayList<cFlota> oFlotas, int resource,Activity activity1) {
        this.oFlotas = oFlotas;
        this.resource = resource;
        activity=activity1;
    }

    @NonNull
    @Override
    public cFlotasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(activity.getApplicationContext()).inflate(resource,parent,false);
        return new cFlotasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cFlotasViewHolder holder, int position)
    {
        cFlota oF = oFlotas.get(position);
        if(oF.getEstadoVehi().equals("VD"))
        {
            holder.imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.bus_estado_uno));
;
        }
        if(oF.getEstadoVehi().equals("PC"))
        {
            holder.imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.bus_estado_dos));
        }
        if(oF.getEstadoVehi().equals("PO"))
        {
            holder.imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.bus_estado_tres));
        }
        holder.textViewCel.setText("["+oF.getNumeSimVehi()+"]");
        holder.textViewIdBus.setText("["+oF.getCodiVehiMoni()+"]");
        holder.textViewFecha.setText("["+oF.getUltiFechMoni()+"]");
        holder.textViewVrsDispo.setText("[V "+oF.getVersDispMoni()+"]");
        holder.textViewReloj.setText("[R "+oF.getCtrlCounMoni()+"]");
    }

    @Override
    public int getItemCount() {
        return oFlotas.size();
    }

    class  cFlotasViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView textViewIdBus;
        TextView textViewFecha;
        TextView textViewVrsDispo;
        TextView textViewCel;
        TextView textViewReloj;
        public cFlotasViewHolder(@NonNull View itemView)
        {
            super(itemView);
            imageView=itemView.findViewById(R.id.card_imagen);
            textViewIdBus=itemView.findViewById(R.id.card_flotas);
            textViewFecha=itemView.findViewById(R.id.card_fecha);
            textViewVrsDispo=itemView.findViewById(R.id.card_dispo);
            textViewCel=itemView.findViewById(R.id.card_cel);
            textViewReloj=itemView.findViewById(R.id.card_reloj);
        }
    }
}
