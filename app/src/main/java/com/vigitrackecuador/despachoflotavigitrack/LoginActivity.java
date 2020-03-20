package com.vigitrackecuador.despachoflotavigitrack;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import com.vigitrackecuador.despachoflotavigitrack.Views.MenuActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity
{
    JsonObjectRequest jsonObjectRequest;
    RequestQueue requestQueue;
    List<cDatosEmpresas> oE1;
    ProgressDialog progressDialogE;
    Button ingresar,admin,btnConfig;
    ProgressDialog progressDialog;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextInputEditText textInputEditTextuser;
    TextInputEditText textInputEditTextpass;
    Spinner spinnerEmpresas;
    TextView textViewEmpre;
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
    String UserLeer ;
    String PassLeer;
    String BDLeer;
    String IpLeer;
    String empresaSP;
    Boolean banSharedP=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();//nodo rpincipal
        ingresar = findViewById(R.id.btn_ingresar);
        textInputEditTextpass = findViewById(R.id.txt_pass);
        textInputEditTextuser = findViewById(R.id.txt_user);
        textViewEmpre =findViewById(R.id.TextempresaSP);
        btnConfig=findViewById(R.id.btn_cofig);
        spinnerEmpresas = findViewById(R.id.spinner_empresas);
        admin = findViewById(R.id.btn_admin);
        NombresEmpresas.add("Seleccione una empresa");
        leerEmpresasFireBase();
        ArrayAdapter<String> oEmpresas = new ArrayAdapter<String>(LoginActivity.this, R.layout.support_simple_spinner_dropdown_item,NombresEmpresas);
        oEmpresas.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerEmpresas.setAdapter(oEmpresas);
        cargarsharedPreferendEMpre();
        spinnerEmpresas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //Toast.makeText(LoginActivity.this, "pos : "+position, Toast.LENGTH_SHORT).show();
                if (position!=0)
                {
                    buscadorBD=parent.getItemAtPosition(position).toString();
                    textViewEmpre.setText(buscadorBD);
                    buscarDatosIngresoEmpresa();
                }else
                    {
                        //Toast.makeText(LoginActivity.this, "Seleccione una Empresa", Toast.LENGTH_SHORT).show();
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
                        if(banSharedP==true)
                        {
                            CargarWebService();
                        }else
                            {
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

            }
        });
        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("Desea Habilitar el cambio de Empresa");
                builder.setPositiveButton("Activar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        spinnerEmpresas.setEnabled(true);
                        Toast.makeText(LoginActivity.this, "Operacion Exitosa", Toast.LENGTH_SHORT).show();
                        textInputEditTextpass.setText("");
                        textInputEditTextuser.setText("");
                        ip="";
                        name_base="";
                        user_u="";
                        pass_u="";

                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(LoginActivity.this, "Operacion Cancelada", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }
    private void CargarWebService()
    {
        guardarPreferencia();
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
    private void leerEmpresasFireBase()
    {
        progressDialogE=new ProgressDialog(LoginActivity.this);
        progressDialogE.setTitle(R.string.developer);
        progressDialogE.setIcon(getResources().getDrawable(R.drawable.icono_vigitrack));
        progressDialogE.setMessage("Cargando Empresas .... ");
        progressDialogE.setCancelable(false);
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
    private void guardarPreferencia() {
        SharedPreferences sharedPreferences = getSharedPreferences("EmpresaSeleccionada", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //Toast.makeText(this, "GSP"+empresa+"-"+user_name_xml+"-"+pass_name_xml, Toast.LENGTH_LONG).show();
        editor.putString("empresa",buscadorBD);
        editor.putString("usuario", user_u);
        editor.putString("pass", pass_u);
        editor.putString("bd", name_base);
        editor.putString("ip", ip);
        editor.apply();
    }
    private void cargarsharedPreferendEMpre() {
        SharedPreferences sharedPreferences = getSharedPreferences("EmpresaSeleccionada", Context.MODE_PRIVATE);
        empresaSP= sharedPreferences.getString("empresa", "ErrorE");
        UserLeer = sharedPreferences.getString("usuario", "ErrorU");
        PassLeer = sharedPreferences.getString("pass", "ErrorP");
        BDLeer=sharedPreferences.getString("bd", "ErrorB");
        IpLeer=sharedPreferences.getString("ip", "ErrorI");
        Toast.makeText(this, "GSP"+empresaSP+"-"+ UserLeer+"-"+PassLeer, Toast.LENGTH_LONG).show();
        if (UserLeer.equals("ErrorU") || PassLeer.equals("ErrorP")|| BDLeer.equals("ErrorB")|| IpLeer.equals("ErrorI")||empresaSP.equals("ErrorE"))
        {
            //Toast.makeText(LoginActivity.this,"Error SP",Toast.LENGTH_SHORT);
        } else
            {
                spinnerEmpresas.setEnabled(false);
                textViewEmpre.setText(empresaSP);
                textInputEditTextuser.setText(UserLeer);
                buscadorBD=empresaSP;
                textInputEditTextpass.setText(PassLeer);
                ip=IpLeer;
                user_u=UserLeer;
                pass_u=PassLeer;
                name_base=BDLeer;
                banSharedP=true;
        }
    }

}
