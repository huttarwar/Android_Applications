package com.example.heramb.know_your_govt;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import static android.R.drawable.ic_dialog_alert;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private ArrayList<Official_Person> officialPersonList = new ArrayList<Official_Person>();
    private RecyclerView recyclerView;
    private OfficialAdapter officialAdapter;
    private Official_Person officialPerson;
    String zipCodeintent = "";
    private static final String TAG = "MainActivity";
    private static final int NEW = 1;
    private Locator locator;
    private String location = null;
    Official_Person officialPerson1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recylerView);
        officialAdapter = new OfficialAdapter(officialPersonList, this);
        recyclerView.setAdapter(officialAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        int result = 0;
        result = networkDialog(this);

        if (result != 1) {
            locator = new Locator(this);
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_person, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuHelp: {
                Intent intent = new Intent(MainActivity.this, About.class);
                startActivityForResult(intent, NEW);
                return true;
            }

            case R.id.menuSearch: {
                loadCivicInfoDownloader(this);
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public int networkDialog(Context context) {

        int result = 0;
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            Intent data = getIntent();
            builder.setTitle("No Network Connection");
            builder.setIcon(ic_dialog_alert);
            builder.setMessage("Data cannot be accessed/loaded without an internet connection");
            AlertDialog dialog = builder.create();
            dialog.show();
            result = 1;
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        officialPerson1 = officialPersonList.get(pos);
        Intent intent = new Intent(MainActivity.this, PersonActivity.class);
        intent.putExtra("officialPerson", officialPerson1);
        intent.putExtra("officialPosition", pos);
        intent.putExtra("location", location);
        startActivityForResult(intent, NEW);
    }

    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(this, " Long View has been clicked", Toast.LENGTH_LONG).show();
        return false;
    }


    public void setData(double lat, double lon) {

        Log.d(TAG, "setData: Lat: " + lat + ", Lon: " + lon);
        String address = doAddress(lat, lon);
        StringTokenizer stringTokenizer = new StringTokenizer(address, " ");
        String zipCodeintent = null;
        while (stringTokenizer.hasMoreTokens()) {
            zipCodeintent = stringTokenizer.nextToken();
        }
        ((TextView) findViewById(R.id.locationmain)).setText(address);
        officialPerson = new Official_Person();
        officialPerson.setZipcode(zipCodeintent);
        new CivicInfoDownloader(MainActivity.this).execute(officialPerson);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: CALL: " + permissions.length);
        Log.d(TAG, "onRequestPermissionsResult: PERM RESULT RECEIVED");
        if (requestCode == 5) {
            Log.d(TAG, "onRequestPermissionsResult: permissions.length: " + permissions.length);
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onRequestPermissionsResult: HAS PERM");
                        locator.setUpLocationManager();
                        locator.determineLocation();
                    } else {
                        Toast.makeText(this, "Location permission was denied - cannot determine address", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onRequestPermissionsResult: NO PERM");
                    }
                }
            }
        }
        Log.d(TAG, "onRequestPermissionsResult: Exiting onRequestPermissionsResult");
    }


    private String doAddress(double latitude, double longitude) {
        Log.d(TAG, "doAddress: Lat: " + latitude + ", Lon: " + longitude);
        List<Address> addresses = null;
        for (int times = 0; times < 3; times++) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                Log.d(TAG, "doAddress: Getting address now");
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                StringBuilder sb = new StringBuilder();
                for (Address ad : addresses) {
                    Log.d(TAG, "doLocation: " + ad);

                    for (int i = 0; i < 1; i++)

                        sb.append(ad.getAddressLine(1));
                }
                return sb.toString();
            } catch (IOException e) {
                Log.d(TAG, "doAddress: " + e.getMessage());
                return null;
            }
        }
        return null;
    }

    public void locationUnavailabledialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Intent data = getIntent();
        builder.setTitle("Enable Location");
        builder.setMessage("Enable location services for a search?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    protected void onDestroy() {
        locator.shutdown();
        super.onDestroy();
    }

    public void loadCivicInfoDownloader(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Intent data = getIntent();
        builder.setMessage("Enter a City, State or a Zip Code:");
        final EditText zipCode = new EditText(this);
        zipCode.setInputType(InputType.TYPE_CLASS_TEXT);
        zipCode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        zipCode.setGravity(Gravity.CENTER);
        builder.setView(zipCode);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                officialPerson = new Official_Person();
                zipCodeintent = zipCode.getText().toString();
                officialPerson.setZipcode(zipCodeintent);
                new CivicInfoDownloader(MainActivity.this).execute(officialPerson);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void setOfficialPersonList(HashMap<String, ArrayList<Official_Person>> officialPersonList) {

        HashMap<String, ArrayList<Official_Person>> objectList = new HashMap<String, ArrayList<Official_Person>>();

        objectList = officialPersonList;
        this.officialPersonList.clear();
        for (Map.Entry<String, ArrayList<Official_Person>> map : objectList.entrySet()) {
            location = map.getKey();
            ((TextView) findViewById(R.id.locationmain)).setText(map.getKey());
            this.officialPersonList.addAll(map.getValue());
        }
        officialAdapter.notifyDataSetChanged();
    }
}
