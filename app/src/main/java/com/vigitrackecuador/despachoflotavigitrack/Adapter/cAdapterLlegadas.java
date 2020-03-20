package com.vigitrackecuador.despachoflotavigitrack.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.vigitrackecuador.despachoflotavigitrack.POO.cVueltas;
import com.vigitrackecuador.despachoflotavigitrack.R;
import com.vigitrackecuador.despachoflotavigitrack.Views.MenuActivity;
import com.vigitrackecuador.despachoflotavigitrack.Views.TarjetasActivity;

import java.util.ArrayList;

public class cAdapterLlegadas extends RecyclerView.Adapter<cAdapterLlegadas.cViewHolderSalidas>
{
    private Activity activity;
    private int resource;
    private ArrayList<cVueltas>oVueltas;
    private Context context;
    public cAdapterLlegadas(Activity activity, int resource, ArrayList<cVueltas> oVueltas, Context context1)
    {
        this.activity = activity;
        this.resource = resource;
        this.oVueltas = oVueltas;
        context=context1;
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
        cVueltas oO = new cVueltas();
         oO= oVueltas.get(position);
         final long auxId = oO.getId_ruta();
         final String ruta=oO.getLetra_ruta();
        final String salida=oO.getDate_salida();
        final String unidad=oO.getId_bus();
        if (oO.getEstaSali_m()==0)
        {
            //holder.cardView.setBackgroundColor(activity.getResources().getColor(R.color.vuelta_pendiente));
            holder.imageViewSalida.setImageDrawable(activity.getResources().getDrawable(R.drawable.blanco_bus));
        }else
            {
                //holder.cardView.setBackgroundColor(activity.getResources().getColor(R.color.vuelta_en_proceso));
                holder.imageViewSalida.setImageDrawable(activity.getResources().getDrawable(R.drawable.llegada));
            }
        final String unidad2 =" [ "+oO.getId_bus()+" ] ";
        String id_ruta =" # "+oO.getId_ruta();

        holder.textViewUnidadSalida.setText(unidad2);
        holder.textViewCodeSalida.setText(id_ruta);
        String Salida="Sali : "+oO.getDate_salida().toString();
        String Llegada="Lleg : "+oO.getDate_llegada().toString();
        String LetraRuta ="Ruta : "+oO.getLetra_ruta();
        holder.textViewRutaSalida.setText(LetraRuta);
        holder.textViewHoraPSalida.setText(Salida);
        holder.textViewHoraLSalida.setText(Llegada);
        String num_vuelta =" Vuelta # "+oO.getNum_vuelta();
        holder.textViewVueltaSalida.setText(num_vuelta);
        holder.buttontarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, TarjetasActivity.class);
                intent.putExtra("idruta",auxId );
                intent.putExtra("unidad",unidad);
                intent.putExtra("salida",salida);
                intent.putExtra("ruta",ruta);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return oVueltas.size();
    }

    class cViewHolderSalidas extends RecyclerView.ViewHolder
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
        Button buttontarjeta;
        public cViewHolderSalidas(@NonNull View itemView)
        {
            super(itemView);
            textViewUnidadSalida=itemView.findViewById(R.id.txt_unidad_llegadas);
            textViewCodeSalida=itemView.findViewById(R.id.txt_code_llegadas);
            textViewRutaSalida=itemView.findViewById(R.id.txt_ruta_llegadas);
            textViewHoraPSalida=itemView.findViewById(R.id.txt_horaP_llegadas_);
            textViewHoraLSalida=itemView.findViewById(R.id.txt_horaL_llegadas);
            textViewVueltaSalida=itemView.findViewById(R.id.txt_vuelta_llegadas);
            imageViewSalida=itemView.findViewById(R.id.imagen_llegadas);
            cardView=itemView.findViewById(R.id.cardview_llegadas);
            linearLayout_card=itemView.findViewById(R.id.linear_llegadas);
            buttontarjeta=itemView.findViewById(R.id.btn_tarjeta_llegadas);
        }
    }
}
