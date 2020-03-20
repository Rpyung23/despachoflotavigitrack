package com.vigitrackecuador.despachoflotavigitrack.Fragments;

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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.vigitrackecuador.despachoflotavigitrack.Adapter.cAdapterFlota;
import com.vigitrackecuador.despachoflotavigitrack.POO.cFlota;
import com.vigitrackecuador.despachoflotavigitrack.POO.cIdBuses;
import com.vigitrackecuador.despachoflotavigitrack.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.vigitrackecuador.despachoflotavigitrack.LoginActivity.ip;
import static com.vigitrackecuador.despachoflotavigitrack.LoginActivity.name_base;
import static com.vigitrackecuador.despachoflotavigitrack.LoginActivity.oIdBuses;

/**
 * A simple {@link Fragment} subclass.
 */
public class Unidadfragment extends Fragment
{
    JsonArrayRequest jsonArrayRequest;
    RequestQueue requestQueue;
    Spinner spinner;
    RecyclerView recyclerView;
    String stringSpiner="*";
    ArrayList<cFlota>ArrayFlotas;
    ProgressDialog progressDialog;
    TextView textViewRegistrados;
    TextView textViewEnlinea;
    TextView textViewsinsenal;
    TextView textViewsinreloj;
    int linea=0;
    int senal=0;
    int reloj=0;
    public Unidadfragment()
    {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_unidad, container, false);
        spinner=view.findViewById(R.id.spinner_unidad_unidad);
        recyclerView=view.findViewById(R.id.recycleview_unidad_unidad);
        textViewRegistrados=view.findViewById(R.id.registrados);
        textViewEnlinea=view.findViewById(R.id.enlinea);
        textViewsinsenal=view.findViewById(R.id.sinsenal);
        textViewsinreloj=view.findViewById(R.id.sinreloj);
        ArrayAdapter<cIdBuses>oAda = new ArrayAdapter<cIdBuses>(getActivity(),R.layout.support_simple_spinner_dropdown_item,oIdBuses);
        spinner.setAdapter(oAda);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                stringSpiner = parent.getItemAtPosition(position).toString();
                if (stringSpiner.equals("*"))
                {
                    boolean ban=true;
                    /**Llaamar el JsonREquest**/
                    llemarTarjetas(stringSpiner,ban);
                }else
                {
                    llemarTarjetas(stringSpiner,false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private void llemarTarjetas(String stringSpiner, boolean ban)
    {
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setIcon(R.drawable.icono_vigitrack);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Generando ....");
        progressDialog.setTitle("Vigitrack Cia.Ltda");
        progressDialog.show();
        String url="";
        String vectE="";
        if (ban)
        {
            for(int i=0;i<oIdBuses.size();i++)
            {
                cIdBuses oB = new cIdBuses();
                oB=oIdBuses.get(i);
                if(i==1)
                {
                    vectE=oB.getIdbues();
                }
                else
                {
                    String a = vectE;
                    vectE=a+","+oB.getIdbues();
                }
            }
            url ="http://www.vigitrackecuador.com/ServiceDespacho/tarjetas_flotas.php?namebd="+name_base+"&ipserver="+ip+
                    "&idbusObse="+vectE;
        }else
        {
            url ="http://www.vigitrackecuador.com/ServiceDespacho/tarjetas_flotas.php?namebd="+name_base+"&ipserver="+ip+
                    "&idbusObse="+stringSpiner;
        }
        jsonArrayRequest= new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                ArrayFlotas = new ArrayList<cFlota>();
                for(int i=0;i<response.length();i++)
                {
                    try
                    {
                        JSONObject jsonObject = response.getJSONObject(i);
                        cFlota oF = new cFlota();
                        oF.setCodiVehiMoni(jsonObject.getString("CodiVehiMoni"));
                        oF.setCtrlCounMoni(jsonObject.getInt("CtrlCounMoni"));
                        oF.setEstadoVehi(jsonObject.getString("EstadoVehi"));
                        oF.setNumeSimVehi(jsonObject.getString("NumeSimVehi"));
                        oF.setUltiFechMoni(jsonObject.getString("UltiFechMoni"));
                        oF.setVersDispMoni(jsonObject.getInt("VersDispMoni"));
                        if(jsonObject.getString("EstadoVehi").equals("VD")){linea++;}else{senal++;}
                        if(jsonObject.getInt("CtrlCounMoni")<=0){reloj++;}
                        ArrayFlotas.add(oF);
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "JSONTryCatch : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                LinearLayoutManager oL = new LinearLayoutManager(getContext());
                oL.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(oL);
                cAdapterFlota oAda = new cAdapterFlota(ArrayFlotas,R.layout.card_flotas,getActivity());
                recyclerView.setAdapter(oAda);
                progressDialog.dismiss();
                textViewRegistrados.setText(oIdBuses.size()+" registrados");
                textViewEnlinea.setText(linea+" En linea");
                textViewsinsenal.setText(senal+" Sin transmitir");
                textViewsinreloj.setText(reloj+" Sin relojes");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getContext(), "VolleyError : "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }
}
