package com.hunglq.map1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hunglq.map1.catchpokemon.AppConstants;
import com.hunglq.map1.model.PokeStop;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.hunglq.map1.MainActivity.pokeStops;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, RewardedVideoAdListener {

    public static int pokemonID;
    public static LatLng pokemonLocation;
    public static boolean isGocha = false;
    boolean isFirstJoin=true;
    boolean isLoading = true;
    LinearLayout layout;
    //public static String oldCoins;
    //private int newCoins = Integer.parseInt(oldCoins);
    //private String coins= Integer.toString(newCoins);
    GoogleMap mMap;
    Marker trainer;
    Circle circle;
    Marker pokemon;
    LatLng latLngTrainer;
    Button btnPokedex, btnPokemon, btnItem, btnShop, btnWatchAds;
    //Ads
    private RewardedVideoAd mAd;
    private TextView mText;
    private int coins;
    private String newCoins;
    //
    GoogleMap.OnMyLocationChangeListener onMyLocationChangeListener =
            new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    if (trainer != null) {
                        trainer.remove();
                    }
                    if (circle != null) {
                        circle.remove();
                    }
                    if(isFirstJoin){
                        putPokemonOnMap(location.getLatitude(), location.getLongitude(), 100);
                        addPokeStop();
                        isFirstJoin=false;
                    }
                    latLngTrainer = new LatLng(location.getLatitude(), location.getLongitude());
                    trainer = mMap.addMarker(new MarkerOptions()
                            .position(latLngTrainer)
                            .title("Trainer: " + MainActivity.uid)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.trainer)));
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLngTrainer) // Sets the center of the map to
                            .zoom(20f)                   // Sets the zoom
                            .tilt(90)    // Sets the tilt of the camera to 30 degrees
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            cameraPosition));
                    circle = mMap.addCircle(new CircleOptions()
                            .center(latLngTrainer)
                            .radius(50.0)
                            .strokeWidth(3f)
                            .strokeColor(Color.BLUE)
                            .fillColor(Color.argb(30, 50, 50, 150)));
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        layout = findViewById(R.id.loading);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        addEvents();

        //Admob rewarded ID (use this): ca-app-pub-8325320225112014~1313306544
        //Admob ID for testing: ca-app-pub-3940256099942544~3347511713
        mText=(TextView)findViewById(R.id.txtViewCoins);
        getCoins(MainActivity.url+ "getCoinsOfAccount.php");
        MobileAds.initialize(getApplicationContext(),"ca-app-pub-8325320225112014~1313306544");
        mAd=MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();

        MobileAds.initialize(this, "ca-app-pub-4526244130357322~6254159537");
        //Admod Top Banner
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);
    }

    private void addPokeStop(){
        for(PokeStop stop:pokeStops){
            LatLng loc=new LatLng(stop.getLatitude(),stop.getLongitude());
            mMap.addMarker(new MarkerOptions()
                .position(loc)
                .title("Poke Stop")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pokestop)));
        }

    }

    private void addEvents() {
        btnPokedex = findViewById(R.id.btnPokedex);
        btnPokedex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, PokedexActivity.class));
            }
        });

        btnPokemon = findViewById(R.id.btnPokemon);
        btnPokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, PokemonAccountActivity.class));
            }
        });

        btnItem = findViewById(R.id.btnItems);
        btnItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, AccountItemActivity.class));
            }
        });

        btnShop = findViewById(R.id.btnShop);
        btnShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, ShopActivity.class));
            }
        });
        btnWatchAds=findViewById(R.id.btnWatchAds);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setOnMyLocationChangeListener(onMyLocationChangeListener);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (cameraPosition.zoom == 20f && isLoading){
                    layout.setVisibility(View.GONE);
                    isLoading = false;
                }
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        float[] distance = new float[2];

        Location.distanceBetween( marker.getPosition().latitude, marker.getPosition().longitude,
                circle.getCenter().latitude, circle.getCenter().longitude, distance);
        if( distance[0] > circle.getRadius()  ){
            Toast.makeText(getBaseContext(), "Walk closer to interact.", Toast.LENGTH_LONG).show();
        } else{
        if(marker.getTitle().equals("Poke Stop")){
            startActivity(new Intent(MapsActivity.this, PokeStopActivity.class));
        } else if (!marker.getTitle().equals("Trainer: " + MainActivity.uid)) {
            pokemon = marker;

                pokemonID = Integer.valueOf(marker.getTitle());
                // Initialization AppConstants to display game scene
                AppConstants.initialization(this.getApplicationContext());
                startActivity(new Intent(MapsActivity.this, GameActivity.class));
                pokemonLocation = marker.getPosition();
            }
            if (marker.getTitle().equals("Trainer: " + MainActivity.uid)) {
                Intent intent = new Intent(MapsActivity.this, UserActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }


        return false;
    }

    private void putPokemonOnMap(double lat, double lon, int amount) {
        // Add some markers to the map.
        LatLng southWest = new LatLng(lat - 0.003000, lon - 0.003000);
        LatLng northEast = new LatLng(lat + 0.003000, lon + 0.003000);
        double lngSpan = northEast.longitude - southWest.longitude;
        double latSpan = northEast.latitude - southWest.latitude;
        // Create some markers
        for (int i = 0; i < amount; i++) {
            Random random = new Random();
            int pokemonID = random.nextInt(91 - 1 + 1) + 1;
            LatLng newLocation = new LatLng(southWest.latitude + latSpan * Math.random(), southWest.longitude + lngSpan * Math.random());
            mMap.addMarker(new MarkerOptions()
                    .position(newLocation)
                    .title(pokemonID + "")
                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromAssets("images/pokemon/" + pokemonID + ".png"))));
        }
    }

    public Bitmap getBitmapFromAssets(String fileName) {
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap.createScaledBitmap(bitmap, 300, 300, false);
    }

    @Override
    protected void onResume() {
        mAd.resume(this);
        super.onResume();
        if(isGocha){
            pokemon.remove();
            isGocha = false;
        }
        if(MainActivity.uid==null){
            finish();
        }
    }

    //Rewarded Ad Video
    private  void getCoins(String url){
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        mText.setText("Coins: "+response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
            @Override
            protected Map<String, String> getParams(){

                Map<String, String> param = new HashMap<>();
                param.put("uid", MainActivity.uid);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private  void updateCoins(String url){
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams(){

                Map<String, String> param = new HashMap<>();
                param.put("uid", MainActivity.uid);

                param.put("newcoins", newCoins);

                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void loadRewardedVideoAd(){
        if(!mAd.isLoaded()){
            mAd.loadAd("ca-app-pub-3940256099942544/5224354917",new AdRequest.Builder().build());
        }
    }

    public void startAdVideo(View view)
    {
        loadRewardedVideoAd();
        if(mAd.isLoaded()){
            mAd.show();
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        //load new ad
        loadRewardedVideoAd();
        getCoins(MainActivity.url + "getCoinsOfAccount.php");
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
       coins=10;
       newCoins =Integer.toString(coins);
       updateCoins(MainActivity.url+"updateCoins.php");
       getCoins(MainActivity.url + "getCoinsOfAccount.php");
    }
    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    @Override
    protected void onPause() {
        mAd.pause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mAd.destroy(this);
        super.onDestroy();
    }

}
