///*
// * Copyright 2017 Giant MFG CO., Inc.
// * Author: C.Y YEN
// */
//
//package com.giant.jamie.weather;
//
//import android.app.Service;
//import android.content.ComponentName;
//import android.content.Intent;
//import android.content.ServiceConnection;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.os.Binder;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.support.v4.content.ContextCompat;
//import android.util.Log;
//
//import com.giant.jamie.cyclingpower.Jamie;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.model.LatLng;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by G96937 on 2017/7/11.
// */
//
//public class SpeedService extends Service implements GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener,
//        LocationListener {
//
//    private LocalBinder m_Binder = new LocalBinder();
//    private Jamie jamie;
//    private String TAG = SpeedService.class.getSimpleName();
//    public GoogleApiClient mGoogleApiClient;
//    private final int gpsInterval = 1100;
//    private List<Location> listOfLocation;
//    public static List<LatLng> listOfLatlng;
//    private static float sum = 0;
//    private ServiceConnect serviceConnect;
//
//    class LocalBinder extends Binder {
//
//        SpeedService getService(){
//
//            return SpeedService.this;
//
//        }
//
//    }
//
//    @Override
//    public boolean onUnbind(Intent intent) {
//        return super.onUnbind(intent);
//    }
//
//    public IBinder onBind(Intent intent) {
//        return m_Binder;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        Log.i(TAG, "SpeedService");
//        jamie = (Jamie) getApplication();
//        buildGoogleApiClient();
//        serviceConnect = new ServiceConnect();
//        bindService(new Intent(this, SpeedService.class), serviceConnect, BIND_AUTO_CREATE);
//        listOfLocation = new ArrayList<>();
//        listOfLatlng = new ArrayList<>();
//
//    }
//
//    public void buildGoogleApiClient(){
//
//        mGoogleApiClient = new GoogleApiClient.Builder(this).
//                addConnectionCallbacks(this).
//                addOnConnectionFailedListener(this).
//                addApi(LocationServices.API).
//                build();
//        mGoogleApiClient.connect();
//
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.i("DistanceLocalService", "Received start id " + startId + ": " + intent);
//        // We want this service to continue running until it is explicitly
//        // stopped, so return sticky.
//        return START_STICKY;
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
//
//        LocationRequest mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(gpsInterval);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//
//        }
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//
////        Log.i(TAG, location+"");
//        //initial
//        double lastLat, lastLon;
//        Location lastLocation = new Location("");
//        lastLocation.reset();
//
//        Location nowLocation = location;
//        jamie.calculator.lat = nowLocation.getLatitude();
//        jamie.calculator.lon = nowLocation.getLongitude();
//        Log.i(TAG, nowLocation+"_nowLocation");
//
//        addCoordinate(nowLocation);
//
//        if (listOfLatlng.size() > 1) {
//
//            //get coordinator which before now and set in last location
//            lastLat = listOfLatlng.get(listOfLatlng.size() - 2).latitude;
//            lastLon = listOfLatlng.get(listOfLatlng.size() - 2).longitude;
////          Log.i(TAG, lastLat + " : " + lastLon + "_lastLat:lastLon");
//
//            lastLocation.setLatitude(lastLat);
//            lastLocation.setLongitude(lastLon);
//            Log.i(TAG, lastLocation + "_LastLocation");
//
//            calculateDistance(nowLocation, lastLocation);
//
//        }
//
//    }
//
//    private void addCoordinate(Location location){
//
//        jamie.calculator.lat = location.getLatitude();
//        jamie.calculator.lon = location.getLongitude();
//        listOfLocation.add(location);
//        listOfLatlng.add(new LatLng(jamie.calculator.lat, jamie.calculator.lon));
////        Log.i(TAG, listOfLatlng.get(listOfLatlng.size()-1)+"_listoflatlon");
////        Log.i(TAG, listOfLocation.get(listOfLocation.size()-1)+"_listoflocation");
//        Log.i(TAG, listOfLatlng + "_listoflatlon");
//
//    }
//
//    private void calculateDistance(Location now, Location last) {
//
//        float distance = now.distanceTo(last);
////        Log.i(TAG, distance + "_Distance");
//
//////todo should we leave getAccuracy here
////     if (location.getAccuracy() < distance) {
//        sum += distance;
////        Log.i(TAG, sum + "_Sum");
////    }
//
//    }
//
//    public static Float getSum(){
//
//        return sum;
//
//    }
//
//    class ServiceConnect implements ServiceConnection {
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//
//            jamie.speedService = ((LocalBinder)service).getService();
//            Log.i(TAG, "Service connected.");
//
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//
//            jamie.speedService = null;
//            Log.i(TAG, "Service disconnected.");
//
//        }
//    }
//
//    public float speed(){
//
//        float speed;
//
//        if(listOfLatlng == null || listOfLatlng.size() <= 2) {
//
//            speed = 0;
//
//        }else {
//
//            float deltaDistance = listOfLocation.get(listOfLocation.size() - 1).distanceTo(listOfLocation.get(listOfLocation.size() - 2));
//            float deltaTime = listOfLocation.get(listOfLocation.size() - 1).getTime() - listOfLocation.get(listOfLocation.size() - 2).getTime();
//            speed = deltaDistance / (deltaTime / 60 * 60);
////            Log.i(TAG, speed + "_speed");
//
//        }
//
//        //Tolerance, 4 is adjusted value
//        return speed*1000*4;
//
//    }
//
//}
//
