package hackclash.help;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;

public class Help extends AppCompatActivity implements View.OnClickListener {
    LocationListener locationListener;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPrefEditor;
    Spinner spinner;
    Button HelpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help);

        HelpButton = (Button) findViewById(R.id.buttonHelp);

        String[] spinnerArray = getResources().getStringArray(R.array.Emergencies);
        ArrayAdapter<CharSequence> arrayAdapter = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,spinnerArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = (Spinner) findViewById(R.id.spinnerEmergency);
        spinner.setAdapter(arrayAdapter);


        sharedPreferences = getSharedPreferences("spref", MODE_PRIVATE);

        Log.d("objectID", sharedPreferences.getString("ObjectID", "x"));

        if(sharedPreferences.getBoolean("HelpDisabled", false)== true){
            disableHelpButton();
        }
        else{
            enableHelpButton();

        };

        sharedPrefEditor = sharedPreferences.edit();


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = MyLocationListener.getInstance(this);

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 100, locationListener);
        }catch(SecurityException e){
            e.printStackTrace();
        }
    }

    private void enableHelpButton() {
        HelpButton.setOnClickListener(this);
        HelpButton.setBackgroundColor(Color.RED);
    }

    private void disableHelpButton() {
        HelpButton.setOnClickListener(null);
        HelpButton.setBackgroundColor(Color.GRAY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.MenuMap :
                Intent intent = new Intent(this,MapActivity.class);
                startActivity(intent);
                break;
            case R.id.MenuHelpReceived:
                if(sharedPreferences.getBoolean("HelpDisabled", false)== false){
                    Toast.makeText(this,"No help request made yet!", Toast.LENGTH_SHORT).show();
                }
                else{
                    enableHelpButton();
                    sharedPrefEditor.putBoolean("HelpDisabled", false);
                    sharedPrefEditor.putString("ObjectID", "x");
                    sharedPrefEditor.commit();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonHelp:
                createInputDialog().show();
                break;

        }
    }
    private Dialog createInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Confirm?");
        builder.setTitle("Confirm Dialog");
        builder.setPositiveButton("OK", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                double latitude = Double.parseDouble(sharedPreferences.getString("Latitude", "0"));
                double longitude = Double.parseDouble(sharedPreferences.getString("Longitude", "0"));

                String caption = (String) spinner.getAdapter().getItem(spinner.getSelectedItemPosition());
                Log.d("Caption", caption);

                disableHelpButton();

                ParseGeoPoint parseGeoPoint = new ParseGeoPoint(latitude, longitude);
                insertIntoParse(parseGeoPoint, caption);
            }
        });
        builder.setNegativeButton("Cancel", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
        }
    public void insertIntoParse(ParseGeoPoint latlong, String caption){
        ParseObject parseObject = new ParseObject("GeoPoints");
        parseObject.put("LatLong", latlong);
        parseObject.put("Caption", caption);
        sharedPrefEditor.putString("ObjectID", parseObject.getObjectId());
        sharedPrefEditor.putBoolean("HelpDisabled", true);
        sharedPrefEditor.commit();
        parseObject.saveInBackground();
    }
    }



