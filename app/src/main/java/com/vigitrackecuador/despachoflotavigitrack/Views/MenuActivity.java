package com.vigitrackecuador.despachoflotavigitrack.Views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.FragmentTransitionSupport;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.vigitrackecuador.despachoflotavigitrack.Fragments.LlegadaFragment;
import com.vigitrackecuador.despachoflotavigitrack.Fragments.SalidaFragment;
import com.vigitrackecuador.despachoflotavigitrack.POO.cFrecuencia;
import com.vigitrackecuador.despachoflotavigitrack.POO.cIdBuses;
import com.vigitrackecuador.despachoflotavigitrack.POO.cRuta;
import com.vigitrackecuador.despachoflotavigitrack.R;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static com.vigitrackecuador.despachoflotavigitrack.LoginActivity.oFrecuencia;
import static com.vigitrackecuador.despachoflotavigitrack.LoginActivity.oIdBuses;
import static com.vigitrackecuador.despachoflotavigitrack.LoginActivity.oRutas;

public class MenuActivity extends AppCompatActivity
{
    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    AlertDialog alertDialog = null;
    TextView textViewFecha;
    TextView textViewHora;
    Spinner spinnerFrecuencia;
    public final Calendar c = Calendar.getInstance();
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);
    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    private static final String BARRA = "-";
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    String stringFecha;
    String stringHora;
    ArrayList<cFrecuencia>oF ;
    /** String de datos **/
    String RutaString=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        toolbar = findViewById(R.id.toolbar_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.developer));
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView=findViewById(R.id.bottom_nav_menu);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MenuActivity.this, drawerLayout, toolbar, R.string.title_toolbar_drawer, R.string.title_toolbar_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        FragmentDefecto();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.item_salida:
                        Toast.makeText(MenuActivity.this, "Salida", Toast.LENGTH_SHORT).show();
                        SalidaFragment oS = new SalidaFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contenedor,oS).setTransition(FragmentTransaction
                                .TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
                        break;
                    case R.id.item_llegada:
                        Toast.makeText(MenuActivity.this, "Llegada", Toast.LENGTH_SHORT).show();
                        LlegadaFragment oLl = new LlegadaFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contenedor,oLl).setTransition(FragmentTransaction
                                .TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
                        break;
                    case R.id.item_flota:
                        Toast.makeText(MenuActivity.this, "Flota", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.tab_asig_ruta:
                        AlertDialogSalida();
                        Toast.makeText(MenuActivity.this, "Ruta", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tab_nueva_unidad:
                        Toast.makeText(MenuActivity.this, "Unidad", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.tab_rastreo_unidad:
                        Toast.makeText(MenuActivity.this, "Rastreo", Toast.LENGTH_SHORT).show();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });

    }

    private void AlertDialogSalida()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
        final LayoutInflater layoutInflater = getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.alert_dialog_salida,null);
        textViewHora = view.findViewById(R.id.textview_alert_hora);
        textViewFecha= view.findViewById(R.id.textview_alert_fecha);

        Spinner spinnerRuta = view.findViewById(R.id.spinner_alert_ruta);
        Spinner spinnerUnidad = view.findViewById(R.id.spinner_alert_unidad);
        spinnerFrecuencia = view.findViewById(R.id.spinner_alert_frecuencia);

        Button button_generar_A = view.findViewById(R.id.btn_alert_generar);
        Button button_cancelar_A = view.findViewById(R.id.btn_alert_cancelar);
        ArrayAdapter<cIdBuses>oAdaBuses = new ArrayAdapter<cIdBuses>(MenuActivity.this,R.layout.support_simple_spinner_dropdown_item,oIdBuses);
        spinnerUnidad.setAdapter(oAdaBuses);
        ArrayAdapter<cRuta>oAdaRuta = new ArrayAdapter<cRuta>(MenuActivity.this,R.layout.support_simple_spinner_dropdown_item,oRutas);
        spinnerRuta.setAdapter(oAdaRuta);

        spinnerRuta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                RutaString = parent.getItemAtPosition(position).toString();
                if (RutaString.equals(null)||RutaString.equals(""))
                {
                    Toast.makeText(MenuActivity.this, "Error de Spinner Frecuencias", Toast.LENGTH_SHORT).show();
                }else
                    {
                        RefrescarFrecuencias(RutaString);
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        builder.setView(view);
        builder.setIcon(R.drawable.icono_vigitrack);
        builder.show();
        alertDialog = builder.create();
        textViewFecha.setText(obtenerfechaActual());
        textViewHora.setText(obtenerhoraActual());
        textViewFecha.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDatePicker();
            }
        });
        textViewHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showTimePicker();
            }
        });
        button_cancelar_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(MenuActivity.this, "Operacion Cancelada", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
        button_generar_A.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
    }

    private void RefrescarFrecuencias(String aux)
    {
        oF= ConsultaFrecuenciaDatos(aux);
        ArrayAdapter<cFrecuencia>adaFreS = new ArrayAdapter<cFrecuencia>(MenuActivity.this,R.layout.support_simple_spinner_dropdown_item,oF);
        spinnerFrecuencia.setAdapter(adaFreS);
    }

    private ArrayList<cFrecuencia> ConsultaFrecuenciaDatos(String aux)
    {
        ArrayList<cFrecuencia>oAux = new ArrayList<>();
        if (aux.equals("*"))
        {
            for(int j=0;j<oFrecuencia.size();j++)
            {
                if(j>0)
                {
                    cFrecuencia oO1 = oFrecuencia.get(j);
                    oAux.add(oO1);
                }
            }
        }else
            {
                for(int i=0;i<oFrecuencia.size();i++)
                {
                    cFrecuencia oO = oFrecuencia.get(i);
                    if(oO.getLetrFrec().equals(aux)){oAux.add(oO);}
                }
            }
        return oAux;
    }
    private void FragmentDefecto()
    {
        SalidaFragment oS = new SalidaFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contenedor,oS).setTransition(FragmentTransaction
                .TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
    }
    public String obtenerfechaActual()
    {
        String FechaAlert="";
        Calendar calendar= Calendar.getInstance(TimeZone.getDefault());
        int year=calendar.get(Calendar.YEAR);
        final int mesActual = calendar.get(Calendar.MONTH)+1;
        String diaFormateado = (calendar.get(Calendar.DAY_OF_MONTH) < 10)? CERO + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)):String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
        FechaAlert=year + BARRA + mesFormateado + BARRA + diaFormateado;
        stringFecha=FechaAlert;
        return FechaAlert;
    }
    public void showDatePicker()
    {
        DatePickerDialog recogerFecha = new DatePickerDialog(MenuActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                textViewFecha.setText(year + "-" + mesFormateado + "-" + diaFormateado);
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
    public void showTimePicker()
    {
        TimePickerDialog recogerHora = new TimePickerDialog(MenuActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                //Muestro la hora con el formato deseado
                textViewHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado+":00");
            }
        }, hora, minuto, true);

        recogerHora.show();
    }
    public String obtenerhoraActual()
    {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto = calendar.get(Calendar.MINUTE);
        int segundo = calendar.get(Calendar.SECOND);
        String horaCon="",minutoCon="",segundoCon="";
        if(hora<10){horaCon="0"+hora;}else{horaCon=String.valueOf(hora);}
        if(minuto<10){minutoCon="0"+minuto;}else {minutoCon=String.valueOf(minuto);}
        if(segundo<10){segundoCon="0"+segundo;}else {segundoCon=String.valueOf(segundo);}
        stringHora=horaCon+":"+minutoCon+":"+segundoCon;
        return stringHora;
    }
    @Override
        public boolean onCreateOptionsMenu(Menu menu)
        {
            getMenuInflater().inflate(R.menu.toolbar_menu, menu);
            return true;
        }
        public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (  Integer.valueOf(android.os.Build.VERSION.SDK) < 7 //Instead use android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            // Take care of calling this method on earlier versions of
            // the platform where it doesn't exist.
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }
        @Override
        public void onBackPressed() {
        return;
        }
}
