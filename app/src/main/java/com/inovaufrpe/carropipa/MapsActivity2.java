package com.inovaufrpe.carropipa;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
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

import com.inovaufrpe.carropipa.utils.Conexao;
import com.inovaufrpe.carropipa.utils.Sessao;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.Map;

public class MapsActivity2 extends AppCompatActivity {

    MapView mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setTitle("Pedido");


        //getCurrentLocation();
        mMap = (MapView) findViewById(R.id.mapaId);



        /*mMap.setTileSource(TileSourceFactory.MAPNIK);
        mMap.getController().setCenter(new GeoPoint(-7.082433, -41.468516));
        mMap.getController().setZoom(15);
        Marker marcador = new Marker(mMap);
        marcador.setPosition(new GeoPoint(-7.082433, -41.468516));
        mMap.getOverlays().add(marcador);*/
        getCurrentLocation();

        recuperainfo();
    }

    public void recuperainfo(){
        int id = Sessao.idPedido;
        new MapsActivity2.RequestRecupera(String.valueOf(id)).execute();
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

    private class RequestRecupera extends AsyncTask<Void, Void, String> {
        String idpedido;
        public RequestRecupera(String id){
            this.idpedido = id;
        }
        @Override
        protected String doInBackground(Void... voids) {
            String url = "http://api-carro-pipa.herokuapp.com/pedidos/"+this.idpedido;
            return Conexao.recuperaEnd(url);
        }
        protected void onPostExecute(String result){
            if (result.equals("NOT FOUND")){
                Log.i("Erro: ","erro");
            }
            else{

                try {
                    JSONObject json = new JSONObject(result);
                    json.put("id_pessoa_mot", Sessao.usuario.getInt("id_pessoa"));
                    json.put("id_pedido",Sessao.idPedido);
                    Log.i("editado",json.toString());
                    new MapsActivity2.EditaInfo(json).execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class EditaInfo extends AsyncTask<Void, Void, String> {
        JSONObject json;

        public EditaInfo(JSONObject json){
            this.json = json;
        }
        @Override
        protected String doInBackground(Void... voids) {
            return Conexao.aceitaPedido("http://api-carro-pipa.herokuapp.com/pedidos",json);
        }

        protected void onPostExecute(String result){
            if (result.equals("NOT FOUND")){
                Log.i("erro editado","not found");
            }
            else{
                Log.i("editado",result);
            }
        }
    }




}
