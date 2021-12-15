package com.precloud.deliverystar.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Placeholder;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.precloud.deliverystar.Model.DirectionsJSONParser;
import com.precloud.deliverystar.R;
import com.precloud.deliverystar.Service.TrackerService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapActivity<latLng> extends AppCompatActivity implements LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final int PERMISSIONS_REQUEST = 1;

    int flag,flag1,i;
    static public String pickUpAdd,dropAdd;
    static public GoogleMap mGoogleMap;
    public static final int PLACE_AUTOCOMPLETE_DROP = 002;
    public static final int PLACE_PICKER_REQUEST_DROP =001;

    private HashMap<String, Marker> mMarkers = new HashMap<>();

    static public Activity context;

    Marker pickUpLocationMarker,dropLocationMarkar;
    ArrayList<Marker> allDriver;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    LatLng latLngPickUp,latLngDrop;
    Polyline polyline;
    String distance,duration;
    BitmapDescriptor pickup,drop,delivery_icon;
   LatLng latLng,latln1;
   Button bt_ride_now;
   LinearLayout ly_drop;
   ImageView iv_drop_choseloc;
   String sourcelatlng,destlatlng;
    Map<String, Marker> markers = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        initViews();

    }

    private void initViews(){

        bt_ride_now = findViewById(R.id.bt_ride_now);
        ly_drop = findViewById(R.id.ly_drop);
       // latLngPickUp = new LatLng(18.516726, 73.856255);
     //   latLngDrop = new LatLng(17.882444,75.020531);
        iv_drop_choseloc = findViewById(R.id.iv_drop_choseloc);

        if (getIntent().getExtras()!=null){
            sourcelatlng = getIntent().getExtras().getString("sourcelatlng");
            destlatlng = getIntent().getExtras().getString("destlatlng");
            String src = sourcelatlng.split("\\{")[1];
           // String in;
            try {
                String myjsonString = "{\"phonetype\":\"N95\",\"cat\":\"WP\"}";
                JSONObject jsonObject = new JSONObject(destlatlng);
                //getting specific key values
                JSONObject jsonObject_source = new JSONObject(sourcelatlng);
                latLngDrop = new LatLng(Double.parseDouble(jsonObject.getString("latitude")),Double.parseDouble(jsonObject.getString("longitude")));
                latLngPickUp = new LatLng(Double.parseDouble(jsonObject_source.getString("latitude")),Double.parseDouble(jsonObject_source.getString("longitude")));
            //   Log.e("latlongdrop", String.valueOf(latLngDrop));
              //  Log.e("latLngPickUp", String.valueOf(latLngPickUp));


                Log.e("latlongdrop1", sourcelatlng);
                Log.e("latLngPickUp1",destlatlng);
            } catch (JSONException e) {
                Log.e("exce",e.getLocalizedMessage());
                e.printStackTrace();
            }
            // JSONObject main  = reader.getJSONObject("source_lat_long");
            // String latitude = main.getString("latitude");
            // Log.e("src",)
            String src1 = src.split("\\}")[0];
            String[] split = src1.split( "(?<!\".{0,255}[^\"]),|,(?![^\"].*\")" );
            String src3 = split[1].replaceAll("^[\"']+|[\"']+$", "");
            String src4 = src3.replaceAll("\"", " ");
            String Src5 = src4.split("",2 )[1].replaceAll(":","").split("  ",2)[1].replaceAll(" ","");
            Log.e("Src5",Src5);


            String srclong = sourcelatlng.split("\\{")[0];;
            String srclong1 = srclong.split("\\}")[0];
            String[] srclong1split = srclong1.split( "(?<!\".{0,255}[^\"]),|,(?![^\"].*\")" );
            String srclong1src3 = srclong1split[0].replaceAll("^[\"']+|[\"']+$", "");
            String srclong1src4 = srclong1src3.replaceAll("\"", " ");
            String srclong1Src5 = srclong1src4.split("",2 )[0].replaceAll(":","").split("  ",2)[0];
            Log.e("Srclong5",srclong1src4);



        }


        pickup = BitmapDescriptorFactory.fromResource(R.drawable.pickup);
        drop = BitmapDescriptorFactory.fromResource(R.drawable.drop);
        delivery_icon = BitmapDescriptorFactory.fromResource(R.drawable.deliver_boyicon);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        showWay(1);
        bt_ride_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  String uri = String.format(Locale.ENGLISH, "geo:%f,%f", Double.parseDouble(sourcelatlng), Double.parseDouble(destlatlng));


                //   Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
              //  Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=18.516726, 73.856255&daddr=20.5666,45.345"));



                //  Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=18.516726,73.856255&daddr=20.5666,45.345"));

                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }

            }
        });
     iv_drop_choseloc.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
             try {
                 startActivityForResult(builder.build(MapActivity.this), PLACE_PICKER_REQUEST_DROP);
             } catch (GooglePlayServicesRepairableException e) {
                 e.printStackTrace();
             } catch (GooglePlayServicesNotAvailableException e) {
                 e.printStackTrace();
             }
         }
     });

        ly_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(MapActivity.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(17.882444, 75.020531, 1);
                  //  setDrop(latLngDrop,addresses.get(0).getAddressLine(0));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    private void setPickUp(LatLng latLng,String Address){
       // latLng = new LatLng(18.516726, 73.856255);

        if (pickUpLocationMarker != null) {
            pickUpLocationMarker.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);

        markerOptions.title(Address.replaceAll(",,",","));
        markerOptions.icon(pickup);
        pickUpLocationMarker = mGoogleMap.addMarker(markerOptions);
        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
        showWay(1);
    }

    private void setDrop(LatLng latLngDrop,String Address){
        // latln1 = new LatLng(17.882444,75.020531);
        if (dropLocationMarkar != null) {
            dropLocationMarkar.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latln1);
        markerOptions.title(Address.replaceAll(",,",","));
        markerOptions.icon(drop);
        dropLocationMarkar = mGoogleMap.addMarker(markerOptions);
        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latln1,16));

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
       mLocationRequest.setInterval(1000);
       mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,  this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if(flag==0) {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latLngPickUp.latitude,latLngPickUp.longitude, 1);
                pickUpAdd=addresses.get(0).getAddressLine(0);
               // tvPickup.setText(addresses.get(0).getAddressLine(0));
                latLngPickUp = new LatLng(latLngPickUp.latitude,latLngPickUp.longitude);
                setPickUp(latLngPickUp,addresses.get(0).getAddressLine(0));


                flag=1;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap=googleMap;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
                subscribeToUpdates();
            } else {
                //Request Location Permission
                //checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }

    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void showWay(int f) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
         //   latln1 = new LatLng(17.882444,75.020531);
            JSONObject jsonObject = new JSONObject(destlatlng);


            latLngDrop = new LatLng(Double.parseDouble(jsonObject.getString("latitude")),Double.parseDouble(jsonObject.getString("longitude")));

            addresses = geocoder.getFromLocation(latLngDrop.latitude, latLngDrop.longitude, 1);
            setDrop(latLngDrop,addresses.get(0).getAddressLine(0));
        }catch (Exception e){

        }



        if(latLngPickUp!=null && latLngDrop!=null){
            String url = getDirectionsUrl(latLngPickUp, latLngDrop);
            DownloadTask downloadTask = new DownloadTask();
            // Start downloading json data from Google Directions API
            downloadTask.execute(url,f);
        }else return;

    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        String key ="key=" +getResources().getString(R.string.google_maps_key);
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode+ "&"+key;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        Log.e("url",url);

        return url;
    }

    private class DownloadTask extends AsyncTask {

        @Override
        protected String doInBackground(Object[] url) {
            String data = "";
            try {
                data = downloadUrl(url[0].toString(),url[1].toString());
            } catch (Exception e) {

                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute((String) result);
        }
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = new PolylineOptions();

            if(result==null){

//                Toast.makeText(getBaseContext(), "No Points pleasew try Again", Toast.LENGTH_SHORT).show();
                return;
            }

            if (polyline != null) {
                polyline.remove();
            }
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                List<HashMap<String, String>> path = result.get(i);
                for(int j=0;j <path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    if(j==0){ // Get distance from the list
                        distance = (String)point.get("distance");
                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }


                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(getResources().getColor(R.color.blue));
                lineOptions.geodesic(true);

            }
            zoomRoute(points);
            if(lineOptions != null) {
                polyline=mGoogleMap.addPolyline(lineOptions);
            } else {
                //if no wey is found retry
                Toast.makeText(MapActivity.this,"Please check your Internet connection",Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void zoomRoute(List<LatLng> lstLatLngRoute) {
        if (mGoogleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);
        int routePadding = 100;
        LatLngBounds latLngBounds = boundsBuilder.build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
    }
    private String downloadUrl(String strUrl, String f) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case PLACE_AUTOCOMPLETE_DROP:
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    latLngDrop = place.getLatLng();
                    dropAdd=place.getAddress().toString();
                 //   tvDrop.setText(place.getAddress().toString());
                    //setDrop(latLngDrop,place.getAddress().toString());
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
                break;
            case PLACE_PICKER_REQUEST_DROP:
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);
                    latLngDrop = place.getLatLng();
                    dropAdd=place.getAddress().toString();
                    //tvDrop.setText(place.getAddress().toString());
                  //  setDrop(latLngDrop,place.getAddress().toString());
                }
                break;
        }
    }




    private void subscribeToUpdates() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(getString(R.string.firebase_path));
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                setMarker(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                setMarker(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("error", "Failed to read value.", error.toException());
            }
        });

    }

    private void setMarker(DataSnapshot dataSnapshot) {
        String key = dataSnapshot.getKey();

        HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
        double lat = Double.parseDouble(value.get("latitude").toString());
        double lng = Double.parseDouble(value.get("longitude").toString());
        LatLng location = new LatLng(lat, lng);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(delivery_icon);

        if (!mMarkers.containsKey(key)) {
            mMarkers.put(key, mGoogleMap.addMarker(markerOptions.title(key).position(location)));
         //   mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom( mGoogleMap.addMarker(new MarkerOptions().title(key).position(location)).getPosition(),14));

        } else {
            mMarkers.get(key).setPosition(location);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,14));

        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : mMarkers.values()) {
            Log.e("Size", String.valueOf(mMarkers.size()));
           // builder.include(marker.getPosition());

            double bearing = bearingBetweenLocations(marker.getPosition(), marker.getPosition());
            rotateMarker(marker, (float) bearing);
        }

      //  mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));
    }

    private double bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }


    private void rotateMarker(final Marker marker, final float toRotation) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final float startRotation = marker.getRotation();
        final long duration = 1000;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {

                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);

                float rot = t * toRotation + (1 - t) * startRotation;

                marker.setRotation(-rot > 180 ? rot / 2 : rot);
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });

    }
}

