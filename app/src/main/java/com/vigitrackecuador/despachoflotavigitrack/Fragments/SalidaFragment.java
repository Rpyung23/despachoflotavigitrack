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
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
public class SalidaFragment extends Fragment {

    public SalidaFragment() {
        // Required empty public constructor
    }
    RecyclerView recyclerView;
    TextView textView_fecha;
    String stringFecha;
    cAdapterSalidas oAda ;
    JsonArrayRequest jsonArrayRequest;
    RequestQueue requestQueue;
    Button buscar;
    ProgressDialog progressDialog;
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
    Spinner spinner_unidad_S;
    Spinner spinner_ruta_S;
    ArrayList<cVueltas> oVV;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_salida, container, false);
        recyclerView=view.findViewById(R.id.recycleview_salidas);
        buscar=view.findViewById(R.id.btn_buscar);
        textView_fecha=view.findViewById(R.id.txt_fecha);
        recyclerView=view.findViewById(R.id.recycleview_salidas);
        spinner_unidad_S=view.findViewById(R.id.spinner_unidad_salida);
        spinner_ruta_S=view.findViewById(R.id.spinner_ruta_salida);
        ArrayAdapter<cIdBuses>oda = new ArrayAdapter<cIdBuses>(getContext(),R.layout.support_simple_spinner_dropdown_item,oIdBuses);
        spinner_unidad_S.setAdapter(oda);
        ArrayAdapter<cRuta>odaR=new ArrayAdapter<cRuta>(getContext(),R.layout.support_simple_spinner_dropdown_item,oRutas);
        spinner_ruta_S.setAdapter(odaR);
        obtenerfechaActual();

        textView_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showDatePicker();
            }
        });
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(getContext(), "unidad : "+unidad_spinner+" ruta : "+ruta_spinner, Toast.LENGTH_LONG).show();
                LeerWebService();
            }
        });
        spinner_ruta_S.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                ruta_spinner=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_unidad_S.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                unidad_spinner=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        return view;
    }

    private void LeerWebService()
    {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Vigitrack Cia.Ltda");
        progressDialog.setMessage("Generando .... ");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
        oVV=new ArrayList<cVueltas>();
        String url_vueltas ="http://www.vigitrackecuador.com/ServiceDespacho/salidas.php?namebd="+name_base+"&ipserver="+ip+
                "&fechaDespacho="+stringFecha+"&idBusDespacho="+unidad_spinner+"&ruta="+ruta_spinner;
        jsonArrayRequest = new JsonArrayRequest(url_vueltas, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                for (int i=0;i<response.length();i++)
                {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        cVueltas oV = new cVueltas();
                        oV.setId_bus(jsonObject.getString("CodiVehiSali_m"));
                        oV.setId_ruta(jsonObject.getInt("idSali_m"));
                        oV.setLetra_ruta(jsonObject.getString("LetraRutaSali_m"));
                        oV.setDate_salida(jsonObject.getString("HoraSaliProgSali_m"));
                        oV.setDate_llegada(jsonObject.getString("HoraLLegProgSali_m"));
                        oV.setNum_vuelta(jsonObject.getInt("NumeVuelSali_m"));
                        //oV.setAdelanto(jsonObject.getInt("atrasoN"));
                        //oV.setAtraso(jsonObject.getInt("adelantoP"));
                        oV.setEstaSali_m(jsonObject.getInt("EstaSali_m"));
                        oVV.add(oV);
                    } catch (JSONException e)
                    {
                        Toast.makeText(getContext(), "JsonException : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                if (oVV.size()>0)
                {
                    LinearLayoutManager oL = new LinearLayoutManager(getContext());
                    oL.setOrientation(RecyclerView.VERTICAL);
                    recyclerView.setLayoutManager(oL);
                    oAda = new cAdapterSalidas(getActivity(),R.layout.card_salidas,oVV);
                    recyclerView.setAdapter(oAda);
                }else
                    {
                        Toast.makeText(getContext(), "recycler vacio", Toast.LENGTH_SHORT).show();
                    }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                LinearLayoutManager oL = new LinearLayoutManager(getContext());
                oL.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(oL);
                oAda = new cAdapterSalidas(getActivity(),R.layout.card_salidas,oVV);
                recyclerView.setAdapter(oAda);
                Toast.makeText(getContext(), "Sin Rutas", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
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
        textView_fecha.setText(year + BARRA + mesFormateado + BARRA + diaFormateado);
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
                textView_fecha.setText(year + "-" + mesFormateado + "-" + diaFormateado);
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
        LeerWebService();
    }
}
