package com.inovaufrpe.carropipa;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.util.Log;
import android.widget.Toast;

import com.inovaufrpe.carropipa.utils.Sessao;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MapsActivity extends AppCompatActivity {

    MapView mMap;
    Marker marcador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setTitle("Pedido");


        //getCurrentLocation();
        mMap = (MapView) findViewById(R.id.mapaId);



        Log.i("pedido", Sessao.pedido.toString());
        getCurrentLocation();
    }

    public void getCurrentLocation(){
        boolean fineLocation = ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean coarseLocation = ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean internet = ActivityCompat.checkSelfPermission(this,Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
        boolean exStorage = ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if(fineLocation && coarseLocation && internet && exStorage) {
            MyLocationNewOverlay myLocation = new MyLocationNewOverlay(mMap);
            myLocation.enableFollowLocation();
            myLocation.enableMyLocation();

            mMap.getOverlays().add(myLocation);
            mMap.getController().setZoom(20);
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Você não pode retornar até que o pedido seja concluido", Toast.LENGTH_LONG).show();
//        super.onBackPressed();
    }




}
