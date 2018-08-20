package com.example.moritzschuck.vinylz;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Provider;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    ImageButton coverLoad, coverLoadCamera, locationButton;
    List<Address> adresses;
    Geocoder geocoder;
    TextView locationText;
    String location, imagePath;
    Vinyl newVinyl;
    Button addButton;
    EditText editBand, editYear, editPrice;
    ImageView mImageView;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_main);
        toolbar = (Toolbar) (findViewById(R.id.toolbar));
        setSupportActionBar(toolbar);
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        //        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.addDrawerListener(toggle);
        //toggle.syncState();

        locationText = (TextView)(findViewById(R.id.locationText));
        locationButton = (ImageButton) (findViewById(R.id.locationFinder));
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               location =  populateAddress();
            }
        });

        coverLoad = (ImageButton) (findViewById(R.id.uploadButton));
        coverLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageFromGallery(v);

            }
        });

        coverLoadCamera = (ImageButton) (findViewById(R.id.coverLoadCamera));
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "Your device doesn't have a camera!", Toast.LENGTH_LONG).show();
            coverLoadCamera.setVisibility(View.GONE);
        }

        mImageView = (ImageView)findViewById(R.id.thumbnail);

        coverLoadCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        //galleryAddPic();

                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                              "com.example.moritzschuck.vinylz.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                }

            }

            private File createImageFile() throws IOException {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File image = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );

                // Save a file: path for use with ACTION_VIEW intents
                imagePath = image.getAbsolutePath();
                return image;
            }


        });


        addButton = (Button) (findViewById(R.id.vinylAdd));
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editBand = (EditText)(findViewById(R.id.bandEdit));
                editPrice = (EditText)(findViewById(R.id.priceEdit));
                editYear = (EditText)(findViewById(R.id.yearEdit));

                String bandname = editBand.getText().toString();
                String price = editPrice.getText().toString();
                String year = editYear.getText().toString();

                //newVinyl = new Vinyl();
                //addToDatabase
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
            }
        });

    }


    private String populateAddress() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        double latitude;
        double longitude;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else {
            if(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!=null){
                //Fehler abfangen, falls Netzwerk nicht verfügbar ist.
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    adresses = geocoder.getFromLocation(latitude,longitude,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String city = adresses.get(0).getLocality();

                locationText.setText(city);
                return city;

            } else {
                locationText.setText("Dein Standort konnte nicht lokalisiert werden");

            }

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        return null;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public void chooseImageFromGallery(View view) {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, REQUEST_GALLERY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Thumbnail bekommen und anzeigen?
        }
        else if(requestCode==REQUEST_GALLERY && resultCode == RESULT_OK){
           Uri selectedImageUri = data.getData();
           //Pfad abgreifen?
            mImageView.setImageURI(selectedImageUri);

        }
            }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
        }




