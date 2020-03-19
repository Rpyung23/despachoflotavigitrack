package com.vigitrackecuador.despachoflotavigitrack.Views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.internal.maps.zzt;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.vigitrackecuador.despachoflotavigitrack.POO.cGeoCercas;
import com.vigitrackecuador.despachoflotavigitrack.POO.cIdBuses;
import com.vigitrackecuador.despachoflotavigitrack.POO.cRastreo;
import com.vigitrackecuador.despachoflotavigitrack.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.vigitrackecuador.despachoflotavigitrack.LoginActivity.ip;
import static com.vigitrackecuador.despachoflotavigitrack.LoginActivity.name_base;
import static com.vigitrackecuador.despachoflotavigitrack.LoginActivity.oG;
import static com.vigitrackecuador.despachoflotavigitrack.LoginActivity.oIdBuses;

public class RastreoActivity extends AppCompatActivity implements OnMapReadyCallback
{
    ArrayList<MarkerOptions>oArrayMarkerOptions;
    ArrayList<Marker>oArrayMarker;
    Spinner spinnerU;
    String ValSpinnerU="*";
    GoogleMap googleMapR;
    float v1 = (float) 0.5;
    float v2 = (float) 0.5;
    ArrayList<cRastreo> ArrasyRastreo;
    Toolbar toolbar;
    JsonArrayRequest jsonArrayRequest;
    RequestQueue requestQueue;
    MapView mapViewR;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rastreo);
        toolbar=findViewById(R.id.toolbar_rastreo);
        spinnerU=findViewById(R.id.spinner_rastreo_unidad);
        mapViewR=findViewById(R.id.mapview_rastreo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ArrayAdapter<cIdBuses>oAdap = new ArrayAdapter<cIdBuses>(RastreoActivity.this,R.layout.support_simple_spinner_dropdown_item,oIdBuses);
        spinnerU.setAdapter(oAdap);
        if (mapViewR!=null)
        {
            mapViewR.onCreate(null);
            mapViewR.onResume();
            mapViewR.getMapAsync(this);
        }
        spinnerU.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                ValSpinnerU=parent.getItemAtPosition(position).toString();
                cargarControles();
                Toast.makeText(RastreoActivity.this, "op : "+ValSpinnerU, Toast.LENGTH_SHORT).show();
                if (ValSpinnerU.equals("*"))
                {
                    boolean ban=true;
                    /**Llaamar el JsonREquest**/
                    LlenarPosiciones(ValSpinnerU,ban);
                }else
                    {
                        LlenarPosiciones(ValSpinnerU,false);
                    }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
    }

    private void LlenarPosiciones(String valSpinnerU, boolean ban)
    {
        String url="";
        String vectE="";
        if(oArrayMarker!=null)
        {
            /**Elimino los Marker anteriores**/
            for (int f=0;f<oArrayMarker.size();f++)
            {
                oArrayMarker.get(f).remove();
            }
        }
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
             url ="http://www.vigitrackecuador.com/ServiceDespacho/rastreo.php?namebd="+name_base+"&ipserver="+ip+
                     "&idbusObse="+vectE;
        }else
            {
                url ="http://www.vigitrackecuador.com/ServiceDespacho/rastreo.php?namebd="+name_base+"&ipserver="+ip+
                        "&idbusObse="+valSpinnerU;
            }
        Toast.makeText(this, "url : "+url, Toast.LENGTH_SHORT).show();
        jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                ArrasyRastreo = new ArrayList<>();
                for (int a=0;a<response.length();a++)
                {
                    try {
                        JSONObject jsonObject = response.getJSONObject(a);
                        cRastreo oR = new cRastreo();
                        oR.setCodiVehiMoni(jsonObject.getString("CodiVehiMoni"));
                        oR.setUltiFechMoni(jsonObject.getString("UltiFechMoni"));
                        oR.setUltiLatiMoni(jsonObject.getDouble("UltiLatiMoni"));
                        oR.setUltiLongMoni(jsonObject.getDouble("UltiLongMoni"));
                        oR.setUltiRumbMoni(jsonObject.getInt("UltiRumbMoni"));
                        oR.setUltiVeloMoni(jsonObject.getInt("UltiVeloMoni"));
                        ArrasyRastreo.add(oR);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(RastreoActivity.this, "TRyCatch : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                LlenarMArkersOptions();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                //Toast.makeText(RastreoActivity.this, "Voller Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(RastreoActivity.this);
        requestQueue.add(jsonArrayRequest);
    }

    public void LlenarMArkersOptions()
    {
        oArrayMarkerOptions=new ArrayList<MarkerOptions>();
        MarkerOptions oMO;
        for(int r=0;r<ArrasyRastreo.size();r++)
        {
            cRastreo oRR = ArrasyRastreo.get(r);
            if(oRR.getUltiRumbMoni()>0 && oRR.getUltiRumbMoni()<=180)
            {
                oMO = new MarkerOptions();
                oMO.position(new LatLng(oRR.getUltiLatiMoni(),oRR.getUltiLongMoni()));
                oMO.title("Unidad : " + oRR.getCodiVehiMoni()).snippet("HORA : " + oRR.getUltiFechMoni()
                        +" "+oRR.getUltiVeloMoni()+ " Km"+" Posición : "
                        + oRR.getUltiLatiMoni() + "  " + oRR.getUltiLongMoni());
                oMO.icon(BitmapDescriptorFactory.fromResource(R.drawable.b2))
                        .anchor(v1,v2).rotation(oRR.getUltiRumbMoni()).flat(true);
                oArrayMarkerOptions.add(oMO);
            }else
                {
                    oMO = new MarkerOptions();
                    oMO.position(new LatLng(oRR.getUltiLatiMoni(),oRR.getUltiLongMoni()));
                    oMO.title("Unidad : " + oRR.getCodiVehiMoni()).snippet("HORA : " + oRR.getUltiFechMoni()
                            +" "+oRR.getUltiVeloMoni()+ " Km"+" Posición : "
                            + oRR.getUltiLatiMoni() + "  " + oRR.getUltiLongMoni());
                    oMO.icon(BitmapDescriptorFactory.fromResource(R.drawable.b))
                            .anchor(v1,v2).rotation(oRR.getUltiRumbMoni()).flat(true);
                    oArrayMarkerOptions.add(oMO);
                }
        }
        LlenarArraysMarker_AddMapa();
    }
    public void LlenarArraysMarker_AddMapa()
    {
        oArrayMarker = new ArrayList<>();
        for(int i=0;i<oArrayMarkerOptions.size();i++)
        {
            MarkerOptions oMOP = oArrayMarkerOptions.get(i);
            Marker oM = googleMapR.addMarker(oMOP);
            oArrayMarker.add(oM);
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        MapsInitializer.initialize(RastreoActivity.this);
        googleMapR=googleMap;
        googleMapR.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMapR.setTrafficEnabled(true);
    }
    private void cargarControles()
    {
        if(oG.size()>0)
        {
            for(int i=0;i<oG.size();i++)
            {
                cGeoCercas oA = oG.get(i);
                float ancho = (float) oA.getRadiCtrl();
                LatLng oL = new LatLng(oA.getLati1Ctrl(),oA.getLong1Ctrl());
                LatLng oL2 = new LatLng(oA.getLati2Ctrl(),oA.getLong2Ctrl());
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.add(oL,oL2);
                polylineOptions.color(R.color.background_controles);
                polylineOptions.width(ancho);
                if (googleMapR!=null) { googleMapR.addPolyline(polylineOptions);}
            }
        }else
            {
                Toast.makeText(this, "Sin Controles", Toast.LENGTH_SHORT).show();
            }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}