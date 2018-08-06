package com.inovaufrpe.carropipa.utils;

import android.os.AsyncTask;

import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class Rota extends AsyncTask<RoadManager, Void, Road> {

    ArrayList<GeoPoint> pontos;
    RoadManager roadManager;

    @Override
    protected Road doInBackground(RoadManager... roadManagers) {
        Road road = roadManager.getRoad(pontos);
        return road;
    }

    public Rota(ArrayList<GeoPoint> pontos, RoadManager roadManager){
        this.pontos = pontos;
        this.roadManager = roadManager;
    }
}
