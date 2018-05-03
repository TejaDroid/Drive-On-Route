package com.tejadroid.driveonroute.PolylineBased;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.tejadroid.driveonroute.R;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LatLng sLatLng;

    private LatLng dLatLng;

    private List<LatLng> myDriveRoute;
    int position = 0;

    private GoogleMap mGoogleMap;
    private Marker mMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        createRoute();

    }

    private void createRoute() {

        String points = "kpmkC_gqyL_F~BuDxAqDvAeFfBO?oA\\yA^u@ZcAz@MJDHDH@REpOCt@wFsAWnAcSeEgW_Gu_@gI_Ew@yEiAg@M{G_CwHsCyIsDeCaA{HyCcDyAi_@{NyHuC{G{BqGoByFuAaFaAqNqCwEu@qHcB}A[sIwAwXuFw\\qGsp@gMal@cLqYyFyY{FqDu@s[kGi[cG{L{BuFoAgRoDg`@wHs]_HwaAgRc^eHW?]HIFSDc@CWQGMEIgAi@uB_@oK{BwGaB}Bu@wBs@aGaCqHsDyFaDcDiBkXyOs_@wTgKcGqMqH_AeAw@mA{MqYs^_w@iPs]O_@oTGiBBqAbCcCxEkGlLwKpSaO`RyNbRiGrIcBpCy@tA}D|FgN`TuEfH_BtBuBdB_FpCiGhDcC`B_BnBk@jAi@jCI`BAnFK~DYpDMlAeAfG}@fEaAtDk@rAkA`Bw@v@aBdAgBx@cLvDiIxCcFtBkGrDcBfAyArAoA|AcA`BaHhOiIxQoJjT{E`KeK~TiC|E{@zAq@`BsAnF}@pDa@rAa@h@_BjAwCfBcD|BkDdDeLbM_E|EoEvF{AbByBzA{B~@_M|DiK|DsCdAkB~@yB`Bm@t@k@~@m@jAe@vA{AtF[bAmAfEsCrJ}@nDsAzEmAbDoDzGs@nAmTl_@g@r@g@h@kBxAoBbAoCt@{BVeMtAwUbCqIfA]XWDmA^yAv@{BbBe@h@y@lA[JoCdEoI`MmF|HiGdJaEbJoElLa@~@e@t@iAnAwCnC{A`BmAvBq@nBc@jB{@vGgArI{AjKwApIe@zA_AnBiBdCsChDw@fAu@~A{ElKoHfLeBbCeAbA}EtDeEtCsBbBeAlAeAlB{@hCWdBK|Ai@tKK|AUbBYpAi@rAoA~BqCrEwBfDu@z@gAt@eBt@kC^}RvBqRxBmk@xGePfB{DZkJ`AqCP_XxCa`@|D}Cv@iDfAgSbH_U|HiEnB}CfAgPpGwIpCyK~CsD`AqJpDiD`ByBv@iIpCmYzJsUjImKrD_X`JiU`IsKjDaGbBkNlE_]zK{m@|PmIxBw_@`K{g@vMwGzA_a@hJyNhD{YbHyO|DcOpD{FzA}DbAeCz@mLxD}GbBmB\\_CRmAD}u@pEqQ`AaBHNRt@~@fCbCbBtBbAtB\\tA|AhDrAnDzD~GdE~J`DbIfErHdDdHfBfHtArKpBrL~@lHh@hAz@xAtAvD`DzJNrAR~DI`CBRL^z@|@R`A";

        if (myDriveRoute == null) {
            myDriveRoute = new ArrayList<>();
        } else {
            myDriveRoute.clear();
        }

        myDriveRoute.addAll(decodePolyLines(points));

        sLatLng = myDriveRoute.get(0);

        dLatLng = myDriveRoute.get(myDriveRoute.size() - 1);
    }


    public static List<LatLng> decodePolyLines(String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d,
                    lng / 100000d
            ));
        }
        return decoded;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a mMarker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;

        mMarker = map.addMarker(
                new MarkerOptions()
                        .title("")
                        .position(myDriveRoute.get(position))
                        .anchor(0.5f, 0.5f)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car1))
                        .flat(true));

        double andgle = bearingBetweenLocations(myDriveRoute.get(position), myDriveRoute.get(position + 1));

        rotateMarker(mMarker, (float) andgle);

        map.addMarker(
                new MarkerOptions()
                        .title("")
                        .position(myDriveRoute.get(0))
                        .anchor(0.5f, 0.8f)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.start))
                        .flat(true));

        map.addMarker(
                new MarkerOptions()
                        .title("")
                        .position(myDriveRoute.get(myDriveRoute.size() - 1))
                        .anchor(0.5f, 0.7f)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination))
                        .flat(true));

        mGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                setMapBound(sLatLng, dLatLng);
                startAnim();
            }
        });
    }

    private void setMapBound(LatLng first, LatLng last) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(first);
        builder.include(last);
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 295);
        mGoogleMap.moveCamera(cu);
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(first, 16)), 1295, null);

    }

    private void startAnim() {
        if (mGoogleMap != null) {
            MapAnimator.getInstance().animateRoute(mGoogleMap, myDriveRoute);
        } else {
            Toast.makeText(getApplicationContext(), "Still, Google Map not ready.", Toast.LENGTH_LONG).show();
        }

        position = 0;

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (position < myDriveRoute.size() - 2) {
                    double andgle = bearingBetweenLocations(myDriveRoute.get(position), myDriveRoute.get(position + 1));
                    rotateMarker(mMarker, (float) andgle);
                    position = position + 1;
                    handler.postDelayed(this, 1295);
                }
            }
        }, 2950);

    }

    public void resetAnimation(View view) {
        startAnim();
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

    boolean isMarkerRotating = false;

    private void rotateMarker(final Marker marker, final float toRotation) {
        if (!isMarkerRotating) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
//            final float startRotation = marker.getRotation();
            final long duration = 1000;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

//                    float rot = t * toRotation + (1 - t) * startRotation;

//                    marker.setRotation(-rot > 180 ? rot / 2 : rot);
                    LatLng startPosition = myDriveRoute.get(position);
                    LatLng endPosition = myDriveRoute.get(position + 1);
                    LatLng currentPosition = new LatLng(
                            startPosition.latitude * (1 - t) + endPosition.latitude * t,
                            startPosition.longitude * (1 - t) + endPosition.longitude * t);

                    Location startLocation = new Location("Start");
                    startLocation.setLatitude(startPosition.latitude);
                    startLocation.setLongitude(startPosition.longitude);

                    Location endLocation = new Location("End");
                    endLocation.setLatitude(endPosition.latitude);
                    endLocation.setLongitude(endPosition.longitude);

                    float targetBearing = endLocation.bearingTo(startLocation);

                    marker.setRotation(targetBearing + 530);
                    if (isVisibleOnMap(currentPosition)) {
                        marker.setPosition(currentPosition);
                    } else {
//                        setMapBound(currentPosition, POINT_B);
                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(currentPosition));
                    }

                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 29);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }

    public boolean isVisibleOnMap(LatLng latLng) {
        VisibleRegion vr = mGoogleMap.getProjection().getVisibleRegion();
        return vr.latLngBounds.contains(latLng);
    }

}
