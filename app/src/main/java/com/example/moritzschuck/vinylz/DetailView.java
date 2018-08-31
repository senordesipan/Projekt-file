package com.example.moritzschuck.vinylz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView titleView, locationView, priceView, editionView, yearView, genreView;
    ImageView coverImage;
    CheckBox favCheck;
    Platte currentVinyl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        setupUI();
        checkIntent();
        findVinyl();
        //populateViews();

    }

    private void populateViews() {
        String title = currentVinyl.getTitle() +" - "+ currentVinyl.getBand();
        String price = currentVinyl.getPrice() + " €";
        String edition = currentVinyl.getEdition();
        String year = currentVinyl.getYear();
        String genre = currentVinyl.getGenre();
        String location = currentVinyl.getLocation();
        String coverSrc = currentVinyl.getCoverSrc();
        if(coverSrc!=""){
            final Bitmap bmp = BitmapFactory.decodeFile(coverSrc);
         new Thread(new Runnable() {
             @Override
             public void run() {
                // coverImage.setImageBitmap(bmp);
             }
         }).start();

        }
        //Favorite heart


        titleView.setText(title);
        priceView.setText(price);
        editionView.setText(edition);
        yearView.setText(year);
        genreView.setText(genre);
        locationView.setText(location);

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

    return b.getLong("vKey");

    }

    private void setupUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        titleView = (TextView)(findViewById(R.id.titleView));
        yearView = (TextView)(findViewById(R.id.yearView));
        priceView = (TextView)(findViewById(R.id.priceView));
        locationView = (TextView)(findViewById(R.id.locationView));
        genreView = (TextView)(findViewById(R.id.genreView));
        editionView = (TextView)(findViewById(R.id.editionView));
        favCheck = (CheckBox) (findViewById(R.id.switchFav));
        coverImage = (ImageView) (findViewById(R.id.detailCoverView));


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } //else if (id == R.id.nav_send) {

        //}

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
