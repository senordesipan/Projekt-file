package com.example.moritzschuck.vinylz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView titleView, locationView, priceView, editionView, yearView, genreView;
    ImageView coverImage, favHeart, favHeartBlack;

    Platte currentVinyl;
    //ImageButton shareButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        setupUI();
        checkIntent();
        findVinyl();
        populateViews();


    }

    private void populateViews() {
        if(currentVinyl!=null) {
            final String title = currentVinyl.getTitle() + " - " + currentVinyl.getBand();
            final String price = "Price: " + currentVinyl.getPrice();
            final String edition = "Edition: " + currentVinyl.getEdition();
            final String year = "Year: " + currentVinyl.getYear();
            final String genre = "Genre: " + currentVinyl.getGenre();
            final String location = "Location: " + currentVinyl.getLocation();
            final String coverSrc = currentVinyl.getCoverSrc();
            if (coverSrc != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Uri uri = Uri.parse(coverSrc);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                coverImage.setImageURI(uri);
                            }
                        });
                    }
                }).start();
            }


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!currentVinyl.getFav()){
                        favHeart.setVisibility(View.INVISIBLE);
                        favHeartBlack.setVisibility(View.VISIBLE);
                    }else {
                        favHeart.setVisibility(View.VISIBLE);
                        favHeartBlack.setVisibility(View.INVISIBLE);
                    }
                    titleView.setText(title);
                    priceView.setText(price);
                    editionView.setText(edition);
                    yearView.setText(year);
                    genreView.setText(genre);
                    locationView.setText(location);
                  //  shareButton.setOnClickListener(new View.OnClickListener() {
                   //     @Override
                     //   public void onClick(View v) {
                      //      shareVinyl();
                      //  }
                   // });
                }
            });

        }

    }

    private void findVinyl() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                currentVinyl = PlatteDatabase.getInstance(getApplicationContext()).daoAccess().findPlatteByID(checkIntent());
                Log.d("*****DEBUG", currentVinyl.getTitle());
                populateViews();
            }
        }).start();


    }

    private long checkIntent() {
    Intent intent = getIntent();
    Bundle b= intent.getExtras();

        assert b != null;
        return b.getLong("vKey");

    }

    private void setupUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        titleView = findViewById(R.id.titleView);
        yearView = findViewById(R.id.yearView);
        priceView = findViewById(R.id.priceView);
        locationView = findViewById(R.id.locationView);
        genreView = findViewById(R.id.genreView);
        editionView = findViewById(R.id.editionView);
        favHeart = findViewById(R.id.favHeart);
        favHeartBlack = findViewById(R.id.favHeartBlack);
        coverImage = findViewById(R.id.detailCoverView);
        //shareButton = (ImageButton)(findViewById(R.id.shareButton));



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.shareVinyl){
            shareVinyl();
        }
        if(id == R.id.deleteVinyl){
            Intent intent = new Intent(DetailView.this, MainActivity.class);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    PlatteDatabase.getInstance(getApplicationContext()).daoAccess().deletePlatte(currentVinyl);

                }
            }).start();
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareVinyl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        //share.setType("image/*");

        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Meine neue App: Vinylz");
        // share.putExtra(Intent.EXTRA_STREAM, imageUri);
        share.putExtra(Intent.EXTRA_TEXT, "Ich habe einen neuen Fund gemacht: " + currentVinyl.getTitle()+ " von " +currentVinyl.getBand() + "! Aus dem Jahr: " + currentVinyl.getYear()+".");

        startActivity(Intent.createChooser(share, "Tell your Friend about your new App!"));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action


        } else if (id == R.id.nav_slideshow) {


        } else if (id == R.id.nav_share) {

        } //else if (id == R.id.nav_send) {

        //}

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
