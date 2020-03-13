package com.vigitrackecuador.despachoflotavigitrack.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.vigitrackecuador.despachoflotavigitrack.POO.cIdBuses;
import com.vigitrackecuador.despachoflotavigitrack.POO.cRuta;
import com.vigitrackecuador.despachoflotavigitrack.R;

import java.util.Calendar;
import java.util.TimeZone;

import static com.vigitrackecuador.despachoflotavigitrack.LoginActivity.oIdBuses;
import static com.vigitrackecuador.despachoflotavigitrack.LoginActivity.oRutas;

/**
 * A simple {@link Fragment} subclass.
 */
public class LlegadaFragment extends Fragment
{
    Spinner spinner_unidad_S;
    Spinner spinner_ruta_S;
    String stringFecha;
    Button button_llegada;
    TextView textViewF ;
    JsonArrayRequest jsonArrayRequest;
    RequestQueue requestQueue;
    public final Calendar c = Calendar.getInstance();
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);
    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    private static final String BARRA = "-";
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    public LlegadaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_llegada, container, false);
        spinner_unidad_S=view.findViewById(R.id.spinner_unidad_salida);
        spinner_ruta_S=view.findViewById(R.id.spinner_ruta_salida);
        textViewF =view.findViewById(R.id.txt_fecha_llegada);
        button_llegada=view.findViewById(R.id.btn_buscar_llegada);
        obtenerfechaActual();
        ArrayAdapter<cIdBuses>adapterIdB = new ArrayAdapter<cIdBuses>(getContext(),R.layout.support_simple_spinner_dropdown_item,oIdBuses);
        ArrayAdapter<cRuta>adapterIdR = new ArrayAdapter<cRuta>(getContext(),R.layout.support_simple_spinner_dropdown_item,oRutas);
        spinner_ruta_S.setAdapter(adapterIdR);
        spinner_unidad_S.setAdapter(adapterIdB);
        textViewF.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDatePicker();
            }
        });
        button_llegada.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
                leerDatosWebService();
            }
        });
        return view;
    }

    private void leerDatosWebService()
    {
    }

    private void obtenerfechaActual()
    {
        Calendar calendar= Calendar.getInstance(TimeZone.getDefault());
        int year=calendar.get(Calendar.YEAR);
        final int mesActual = calendar.get(Calendar.MONTH)+1;
        String diaFormateado = (calendar.get(Calendar.DAY_OF_MONTH) < 10)? CERO + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)):String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
        textViewF.setText(year + BARRA + mesFormateado + BARRA + diaFormateado);
        stringFecha=year + BARRA + mesFormateado + BARRA + diaFormateado;
    }
    private void showDatePicker()
    {
        DatePickerDialog recogerFecha = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                textViewF.setText(year + "-" + mesFormateado + "-" + diaFormateado);
                stringFecha=year + "-" + mesFormateado + BARRA + diaFormateado;
            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
    }
}
