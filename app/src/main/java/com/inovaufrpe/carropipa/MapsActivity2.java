package com.inovaufrpe.carropipa;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.util.Log;
import android.widget.Toast;

import com.inovaufrpe.carropipa.utils.Conexao;
import com.inovaufrpe.carropipa.utils.Sessao;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.List;
import java.util.Map;

public class MapsActivity2 extends AppCompatActivity {
    LocationManager locationManager;
    String partida = "-8.125577,-35.0289911";
    String destino = "-8.115209,-35.022845";



    String url1 = "https://graphhopper.com/api/1/route?point="+partida;
    String url2 = "&point="+destino;
    String url3 = "&vehicle=car&debug=true&key=6591140d-f1e9-4609-a565-4e056b868f66&type=json&points_encoded=false";
    String url = url1+url2+url3;

    MapView mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setTitle("Pedido");

        Sessao.verificarPedidos.interrupt();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mMap = (MapView) findViewById(R.id.mapaId);



        mMap.setTileSource(TileSourceFactory.MAPNIK);
        /*mMap.getController().setCenter(new GeoPoint(-7.082433, -41.468516));
        mMap.getController().setZoom(15);
        Marker marcador = new Marker(mMap);
        marcador.setPosition(new GeoPoint(-7.082433, -41.468516));

        mMap.getOverlays().add(marcador);*/
        getCurrentLocation();
        //Log.i("pedido", String.valueOf(Sessao.idPedido));
        recuperainfo();
        //desenhaRota();
    }

    public void desenhaRota(){
        new MapsActivity2.RequestRota().execute();
    }

    public void recuperainfo(){
        new MapsActivity2.RequestRecupera(String.valueOf(Sessao.idPedido)).execute();
    }

    public void getCurrentLocation(){
        boolean fineLocation = ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean coarseLocation = ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean internet = ActivityCompat.checkSelfPermission(this,Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
        boolean exStorage = ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if(fineLocation && coarseLocation && internet && exStorage) {
            List<String> providers = locationManager.getProviders(new Criteria(),true);
            if (providers!= null){
                Location location = null;
                for (String provider : providers){
                    Location l = locationManager.getLastKnownLocation(provider);
                    if(l != null){
                        if (location == null || l.getTime() > location.getTime()){
                            location = l;
                        }
                    }

                }
                partida = String.valueOf(location.getLatitude())+","+String.valueOf(location.getLongitude());
                Log.i("ROTA",partida);
            }
            GpsMyLocationProvider gps = new GpsMyLocationProvider(this);
            gps.addLocationSource(LocationManager.NETWORK_PROVIDER);

            MyLocationNewOverlay myLocation = new MyLocationNewOverlay(gps,mMap);

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
            return Conexao.recuperainfo(url);
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
                    Log.i("PEDIDO:",json.toString());
                    new MapsActivity2.EditaInfo(json).execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
    }
}

private class RequestRota extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... voids) {
        String url1 = "https://graphhopper.com/api/1/route?point="+partida;
        String url2 = "&point="+destino;
        String url3 = "&vehicle=car&debug=true&key=6591140d-f1e9-4609-a565-4e056b868f66&type=json&points_encoded=false";
        String url = url1+url2+url3;
        Log.i("ROTA",url);
        return Conexao.recuperaRota(url);
    }
    protected void onPostExecute(String result){
        if (result.equals("NOT FOUND")){
            Log.i("Erro: ","erro");
        }
            else{

                Log.i("ROTA",result);
                try {

                    JSONObject rota = new JSONObject(result);
                    String points = rota.getString("paths");
                    rota = new JSONObject(points);
                    Log.i("ROTAS",points.toString());
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
            Sessao.pedido = json;
            try {
                destino = json.getString("checkIn");
                new MapsActivity2.RequestRota().execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }




}
