package com.vigitrackecuador.despachoflotavigitrack.Fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.vigitrackecuador.despachoflotavigitrack.Adapter.cAdapterSalidas;
import com.vigitrackecuador.despachoflotavigitrack.POO.cIdBuses;
import com.vigitrackecuador.despachoflotavigitrack.POO.cRuta;
import com.vigitrackecuador.despachoflotavigitrack.POO.cVueltas;
import com.vigitrackecuador.despachoflotavigitrack.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import static com.vigitrackecuador.despachoflotavigitrack.LoginActivity.ip;
import static com.vigitrackecuador.despachoflotavigitrack.LoginActivity.name_base;
import static com.vigitrackecuador.despachoflotavigitrack.LoginActivity.oIdBuses;
import static com.vigitrackecuador.despachoflotavigitrack.LoginActivity.oRutas;

/**
 * A simple {@link Fragment} subclass.
 */
public class LlegadaFragment extends Fragment
{
    ProgressDialog progressDialog;
    ArrayList<cVueltas>oLle;
    Spinner spinner_unidad_L;
    Spinner spinner_ruta_L;
    String stringFecha;
    Button button_llegada;
    TextView textViewF ;
    JsonArrayRequest jsonArrayRequest;
    RequestQueue requestQueue;
    RecyclerView recyclerView;
    String unidad_spinner="*";
    String ruta_spinner="*";
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
        spinner_unidad_L=view.findViewById(R.id.spinner_unidad_salida);
        spinner_ruta_L=view.findViewById(R.id.spinner_ruta_salida);
        recyclerView = view.findViewById(R.id.recycleview_llegadas);
        textViewF =view.findViewById(R.id.txt_fecha_llegada);
        button_llegada=view.findViewById(R.id.btn_buscar_llegada);
        obtenerfechaActual();
        ArrayAdapter<cIdBuses>adapterIdB = new ArrayAdapter<cIdBuses>(getContext(),R.layout.support_simple_spinner_dropdown_item,oIdBuses);
        ArrayAdapter<cRuta>adapterIdR = new ArrayAdapter<cRuta>(getContext(),R.layout.support_simple_spinner_dropdown_item,oRutas);
        spinner_ruta_L.setAdapter(adapterIdR);
        spinner_unidad_L.setAdapter(adapterIdB);
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
        spinner_ruta_L.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                ruta_spinner= parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_unidad_L.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                unidad_spinner = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private void leerDatosWebService()
    {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Vigitrack Cia.Ltda");
        progressDialog.setIcon(R.drawable.icono_vigitrack);
        progressDialog.setMessage("Generando ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        oLle = new ArrayList<cVueltas>();
        String url_llegadas="http://www.vigitrackecuador.com/ServiceDespacho/llegadas.php?namebd="+name_base+"&ipserver="+ip+
                "&fechaDespacho="+stringFecha+"&idBusDespacho="+unidad_spinner+"&ruta="+ruta_spinner;
        //Toast.makeText(getContext(), "url : "+url_llegadas, Toast.LENGTH_LONG).show();
        jsonArrayRequest = new JsonArrayRequest(url_llegadas, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                for (int i=0;i<response.length();i++)
                {
                    try
                    {
                        JSONObject jsonObject = response.getJSONObject(i);
                        cVueltas oL = new cVueltas();
                        oL.setId_bus(jsonObject.getString("CodiVehiSali_m"));
                        oL.setId_ruta(jsonObject.getInt("idSali_m"));
                        oL.setLetra_ruta(jsonObject.getString("LetraRutaSali_m"));
                        oL.setDate_salida(jsonObject.getString("HoraSaliProgSali_m"));
                        oL.setDate_llegada(jsonObject.getString("HoraLLegProgSali_m"));
                        oL.setNum_vuelta(jsonObject.getInt("NumeVuelSali_m"));
                        //oV.setAdelanto(jsonObject.getInt("atrasoN"));
                        //oV.setAtraso(jsonObject.getInt("adelantoP"));
                        oL.setEstaSali_m(jsonObject.getInt("EstaSali_m"));
                        oLle.add(oL);
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                if (oLle.size()>0)
                {
                    LinearLayoutManager olinear = new LinearLayoutManager(getContext());
                    olinear.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(olinear);
                    cAdapterSalidas oAda = new cAdapterSalidas(getActivity(),R.layout.card_salidas,oLle);
                    recyclerView.setAdapter(oAda);
                }else
                    {
                        Toast.makeText(getContext(), "No existe informaciòn", Toast.LENGTH_SHORT).show();
                    }
            progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "VollerError : "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
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
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        leerDatosWebService();
    }
}
