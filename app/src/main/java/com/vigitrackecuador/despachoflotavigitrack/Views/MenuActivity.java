package com.vigitrackecuador.despachoflotavigitrack.Views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.FragmentTransitionSupport;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.vigitrackecuador.despachoflotavigitrack.Fragments.LlegadaFragment;
import com.vigitrackecuador.despachoflotavigitrack.Fragments.SalidaFragment;
import com.vigitrackecuador.despachoflotavigitrack.R;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

public class MenuActivity extends AppCompatActivity
{
    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.title_toolbar_drawer, R.string.title_toolbar_drawer);
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
    }

    private void FragmentDefecto()
    {
        SalidaFragment oS = new SalidaFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contenedor,oS).setTransition(FragmentTransaction
                .TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
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
