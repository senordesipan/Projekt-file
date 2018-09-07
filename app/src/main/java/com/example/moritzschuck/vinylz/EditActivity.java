package com.example.moritzschuck.vinylz;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    ImageButton coverLoad, coverLoadCamera, locationButton;
    TextView locationText;
    Button addButton;
    EditText editTitle, editBand, editYear, editPrice, editEdition, editGenre;
    ImageView mImageView;
    Switch switchFav;

    Platte newVinyl;
    boolean isFavorite;
    String location, imagePath;
    List<Address> adresses;

    Geocoder geocoder;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GALLERY = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_main);
        initToolbar();
        setupUI();

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        //        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.addDrawerListener(toggle);
        //toggle.syncState();

        locationText = (EditText)(findViewById(R.id.locationText));
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
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        switchFav = (Switch)findViewById(R.id.switchFav);
        switchFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isFavorite = true;

                 Toast.makeText(getApplicationContext(), "Marked as a Favorite", Toast.LENGTH_SHORT).show();
                }
                else{
                    isFavorite = false;
                }
            }
        });


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

                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }

            private File createImageFile() throws IOException {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File image = File.createTempFile(
                        imageFileName,   /* prefix */
                        ".jpg",   /* suffix */
                        storageDir      /* directory */
                );

                // Save a file: path for use with ACTION_VIEW intents
                Uri uri = Uri.parse(image.getAbsolutePath());
                imagePath = uri.toString();
                return image;
            }


        });


        addButton = (Button) (findViewById(R.id.vinylAdd));
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String title = editTitle.getText().toString();
                final String bandname = editBand.getText().toString();
                final String price = editPrice.getText().toString();
                final String year = editYear.getText().toString();
                final String edition = editEdition.getText().toString();
                final String genre = editGenre.getText().toString();
                final String location = locationText.getText().toString();

                if((!bandname.equals("") && !title.equals(""))) /*Wird noch weitergeführt, gerade zu faul ;)*/ {
                    addVinyl(title, bandname, year, price, edition, genre, imagePath,location, isFavorite);

                    Toast.makeText(getApplicationContext(), "Successfully added new Vinyl in Database", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Fill in the missing fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void initToolbar() {
        toolbar = (Toolbar) (findViewById(R.id.toolbar));
        setSupportActionBar(toolbar);
        toolbar.showOverflowMenu();
    }

    private void setupUI() {
        editBand = (EditText)(findViewById(R.id.bandEdit));
        editTitle = (EditText)(findViewById(R.id.titleEdit));
        editPrice = (EditText)(findViewById(R.id.priceEdit));
        editYear = (EditText)(findViewById(R.id.yearEdit));
        editEdition = (EditText)(findViewById(R.id.editionEdit));
        editGenre = (EditText)(findViewById(R.id.genreEdit));
    }


    private void addVinyl(final String title, final String bandname, final String year, final String price, final String edition, final String genre, final String imagePath, final String location, final boolean isFavorite) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                newVinyl = new Platte();
                newVinyl.setYear(year);
                newVinyl.setPrice(price);
                newVinyl.setEdition(edition);
                newVinyl.setGenre(genre);
                newVinyl.setCoverSrc(imagePath);
                newVinyl.setTitle(title);
                newVinyl.setBand(bandname);
                newVinyl.setLocation(location);
                newVinyl.setFav(isFavorite);
                PlatteDatabase.getInstance(getApplicationContext()).daoAccess().insertPlatte(newVinyl);
            }
        }).start();
    }

    private String populateAddress() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        double latitude;
        double longitude;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else {

            if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)&& locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!=null) {
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
                Toast.makeText(getApplicationContext(), "The location could not be located", Toast.LENGTH_SHORT).show();
                return null;
            }

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
           Bitmap imageBitmap = BitmapFactory.decodeFile(imagePath, null);
            mImageView.setImageBitmap(imageBitmap);

            Log.d("****DEBUG",imagePath);

        }
        else if(requestCode==REQUEST_GALLERY && resultCode == RESULT_OK){
           Uri selectedImageUri = data.getData();
           //Pfad abgreifen?
            imagePath = selectedImageUri.toString();
            mImageView.setImageURI(selectedImageUri);

        }
            }

        }




