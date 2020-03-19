package com.vigitrackecuador.despachoflotavigitrack;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vigitrackecuador.despachoflotavigitrack.POO.cDatosEmpresas;
import com.vigitrackecuador.despachoflotavigitrack.POO.cFrecuencia;
import com.vigitrackecuador.despachoflotavigitrack.POO.cGeoCercas;
import com.vigitrackecuador.despachoflotavigitrack.POO.cIdBuses;
import com.vigitrackecuador.despachoflotavigitrack.POO.cRuta;
import com.vigitrackecuador.despachoflotavigitrack.Views.LoginAdminActivity;
import com.vigitrackecuador.despachoflotavigitrack.Views.MenuActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity
{
    JsonObjectRequest jsonObjectRequest;
    RequestQueue requestQueue;
    List<cDatosEmpresas> oE1;
    Button ingresar,admin;
    ProgressDialog progressDialog;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextInputEditText textInputEditTextuser;
    TextInputEditText textInputEditTextpass;
    Spinner spinnerEmpresas;
    /*Array String con datos para spinner*/
    ArrayList<String>NombresEmpresas= new ArrayList<>();
    public  static  ArrayList<cIdBuses>oIdBuses;
    public  static ArrayList<cRuta>oRutas;
    public  static  ArrayList<cFrecuencia>oFrecuencia;
    public  static ArrayList<cGeoCercas>oG;
    /* DAtos de la empresa */
    public  static String ip;
    public  static String name_base;
    public  static String user_u;
    public  static String pass_u;
    /************/
    String buscadorBD="S/N";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();//nodo rpincipal
        ingresar = findViewById(R.id.btn_ingresar);
        textInputEditTextpass = findViewById(R.id.txt_pass);
        textInputEditTextuser = findViewById(R.id.txt_user);
        spinnerEmpresas = findViewById(R.id.spinner_empresas);
        admin = findViewById(R.id.btn_admin);
        NombresEmpresas.add("Seleccione una empresa");
        leerEmpresasFireBase();
        ArrayAdapter<String> oEmpresas = new ArrayAdapter<String>(LoginActivity.this, R.layout.support_simple_spinner_dropdown_item,NombresEmpresas);
        oEmpresas.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerEmpresas.setAdapter(oEmpresas);
        spinnerEmpresas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(LoginActivity.this, "pos : "+position, Toast.LENGTH_SHORT).show();
                if (position!=0)
                {
                    buscadorBD=parent.getItemAtPosition(position).toString();
                    buscarDatosIngresoEmpresa();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (textInputEditTextpass.getText().toString().equals("")||textInputEditTextuser.getText().toString().equals(""))
                {
                    Toast.makeText(LoginActivity.this, "Datos Vacios", Toast.LENGTH_SHORT).show();
                }else
                    {
                        /*Toast.makeText(LoginActivity.this, ""+user_u+"="+textInputEditTextuser.getText()+" "+pass_u+"="+textInputEditTextpass.getText(),
                                Toast.LENGTH_LONG).show();*/
                        if (user_u.equals(textInputEditTextuser.getText().toString()) & pass_u.equals(textInputEditTextpass.getText().toString()))
                        {
                            /**Llamo al WEbservice*/
                            //Toast.makeText(LoginActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                            CargarWebService();
                        }else
                            {
                                Toast.makeText(LoginActivity.this, "Datos Erroneos", Toast.LENGTH_SHORT).show();
                            }
                    }

            }
        });
    }
    private void CargarWebService()
    {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Vigitrack Cia.Ltd");
        progressDialog.setIcon(R.drawable.icono_vigitrack);
        progressDialog.setMessage("Cargando Informacion");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
        String url_datos = "http://www.vigitrackecuador.com/ServiceDespacho/WebServiceDatos.php"+"?namebd="+name_base+"&ipserver="+ip;
        //Toast.makeText(this, "utl : "+url_datos, Toast.LENGTH_SHORT).show();
        jsonObjectRequest=new JsonObjectRequest(url_datos, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    oIdBuses= new ArrayList<>();
                    JSONArray jsonArrayUnidad = response.getJSONArray("Unidad");
                    cIdBuses oIAux = new cIdBuses();
                    oIAux.setIdbues("*");
                    oIdBuses.add(oIAux);
                    for(int i=0;i<jsonArrayUnidad.length();i++)
                    {
                        JSONObject jsonObjectU = jsonArrayUnidad.getJSONObject(i);
                        cIdBuses oI = new cIdBuses();
                        oI.setIdbues(jsonObjectU.getString("CodiVehiObseVehi"));
                        oIdBuses.add(oI);
                        Toast.makeText(LoginActivity.this, "Id : "+oI.getIdbues(), Toast.LENGTH_SHORT).show();

                    }
                    oRutas= new ArrayList<>();
                    JSONArray jsonArrayRuta= response.getJSONArray("Ruta");
                    cRuta oRA = new cRuta();
                    oRA.setCodiClieRuta("*");
                    oRA.setDescRuta("*");
                    oRA.setLetrRuta("*");
                    oRutas.add(oRA);
                    for (int j=0;j<jsonArrayRuta.length();j++)
                    {
                        JSONObject jsonObjectR = jsonArrayRuta.getJSONObject(j);
                        cRuta oR = new cRuta();
                        oR.setCodiClieRuta(jsonObjectR.getString("CodiClieRuta"));
                        oR.setDescRuta(jsonObjectR.getString("DescRuta"));
                        oR.setLetrRuta(jsonObjectR.getString("LetrRuta"));
                        oR.setIdRuta(jsonObjectR.getInt("idRuta"));
                        oRutas.add(oR);
                    }
                    oFrecuencia = new ArrayList<>();
                    JSONArray jsonArrayFrecuencias= response.getJSONArray("Frecuencia");
                    cFrecuencia oFFA = new cFrecuencia();
                    oFFA.setDescFrec("*");
                    //oFFA.setIdRutaFrec(jsonObjectF.getString());
                    oFFA.setLetrFrec("*");
                    oFrecuencia.add(oFFA);
                    for (int k=0;k<jsonArrayFrecuencias.length();k++)
                    {
                        JSONObject jsonObjectF = jsonArrayFrecuencias.getJSONObject(k);
                        cFrecuencia oFF = new cFrecuencia();
                        oFF.setDescFrec(jsonObjectF.getString("DescFrec"));
                        //oFF.setIdRutaFrec(jsonObjectF.getString());
                        oFF.setLetrFrec(jsonObjectF.getString("LetrFrec"));
                        oFrecuencia.add(oFF);
                    }
                    oG = new ArrayList<cGeoCercas>();
                    JSONArray jsonArray = response.getJSONArray("Control");
                    for(int l=0;l<jsonArray.length();l++)
                    {
                        JSONObject jsonObjectCn = jsonArray.getJSONObject(l);
                        cGeoCercas oGe = new cGeoCercas();
                        oGe.setDescCtrl(jsonObjectCn.getString("DescCtrl"));
                        oGe.setLati1Ctrl(jsonObjectCn.getDouble("Lati1Ctrl"));
                        oGe.setLong1Ctrl(jsonObjectCn.getDouble("Long1Ctrl"));
                        oGe.setLati2Ctrl(jsonObjectCn.getDouble("Lati2Ctrl"));
                        oGe.setLong2Ctrl(jsonObjectCn.getDouble("Long2Ctrl"));
                        oGe.setRadiCtrl(jsonObjectCn.getInt("RadiCtrl"));
                        oG.add(oGe);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "jsonEx : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Intent intent= new Intent(LoginActivity.this,MenuActivity.class);
                startActivity(intent);
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(LoginActivity.this, "errorVolley : "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
    private void buscarDatosIngresoEmpresa()
    {
        if(buscadorBD.equals("S/N"))
        {
            Toast.makeText(this, "Selecione una empresa ", Toast.LENGTH_SHORT).show();
        }else
            {
                boolean ban=false;
                int i=0;
                do {
                    cDatosEmpresas  oA=oE1.get(i);
                    if(oA.getEmrpesa_name().equals(buscadorBD))
                    {
                        user_u=oA.getUser();
                        pass_u=oA.getPass();
                        ip=oA.getIp_server();
                        name_base=oA.getName_db();
                        Toast.makeText(this, "user : "+user_u+" pass : "+pass_u, Toast.LENGTH_SHORT).show();
                        ban=true;
                    }else
                    {
                        i++;
                    }
                }while (ban==false);
            }
    }
    private void leerEmpresasFireBase() {
        oE1 = new ArrayList<cDatosEmpresas>();
        databaseReference.child("Empresas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        databaseReference.child("Empresas").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    cDatosEmpresas oE = new cDatosEmpresas();
                                    oE.setName_db(dataSnapshot.child("bd_name").getValue().toString());
                                    oE.setEmrpesa_name(dataSnapshot.child("empresa_name").getValue().toString());
                                    oE.setIp_server(dataSnapshot.child("ip_server").getValue().toString());
                                    oE.setUser(dataSnapshot.child("usuario").getValue().toString());
                                    oE.setPass(dataSnapshot.child("pass").getValue().toString());
                                    //Toast.makeText(LoginActivity.this, "Empr : "  +oE.getEmrpesa_name(), Toast.LENGTH_SHORT).show();
                                    oE1.add(oE);
                                    NombresEmpresas.add(oE.getEmrpesa_name());
                                } else {
                                    Toast.makeText(LoginActivity.this, "No exite lectura", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(LoginActivity.this, "Error al leer empresas", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "dataSnapshot no existe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "canel : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
