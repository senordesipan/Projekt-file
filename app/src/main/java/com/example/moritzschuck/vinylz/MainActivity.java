package com.example.moritzschuck.vinylz;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageButton addButton;
       Button searchButton;
       EditText searchField;
       Platte newPlatte;
       TextView example;
       //Testfeld, kommt noch weg

    private List<Platte> myVinyl = new ArrayList<Platte>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUi();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exampleVinylList();
                populateListView();
            }
        });

    }

    private void exampleVinylList() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                Platte newPlatte;
                newPlatte = platteDatabase.daoAccess().findPlatteByTitle(searchField.getText().toString());
                //hier ist der Fehler schätze ich
                if(newPlatte!=null) {
                    myVinyl.add(newPlatte);
                    example.setText(newPlatte.getTitle());
                }else{
                    Toast.makeText(getApplicationContext(), "Couldn't find Vinyl with this Title", Toast.LENGTH_SHORT).show();
                }
            }
        }).start();


    }
    private void populateListView() {
        ArrayAdapter<Platte> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailView.class);
                //intent.putExtra("vID", position);
                //hier möchte ich die Id des ausgewähltem Objektes in die Detailview nehmen, how?
                startActivity(intent);
            }
        });
    }

    private void setupUi() {

        addButton = (ImageButton) (findViewById(R.id.addButton));
        searchButton = (Button)(findViewById(R.id.searchButton));
        searchField = (EditText)(findViewById(R.id.searchField));
        example = (TextView)(findViewById(R.id.exampleText));
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
        getMenuInflater().inflate(R.menu.main, menu);
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
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

            // Add data to the intent, the receiving app will decide
            // what to do with it.
            share.putExtra(Intent.EXTRA_SUBJECT, "Meine neue App: Vinylz");
            share.putExtra(Intent.EXTRA_TEXT, "Mit dieser App kannst du alle deine Platten Teilen! Ist das nicht super!");

            startActivity(Intent.createChooser(share, "Tell your Friend about your new App!"));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    PlatteDatabase platteDatabase;

    private class MyListAdapter extends ArrayAdapter<Platte> {
        public MyListAdapter() {
            super(MainActivity.this, R.layout.item_view, myVinyl);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }

            // Find the car to work with.
           Platte currentVinyl = myVinyl.get(position);
            //Hier versuche ich den ganzen shit aus dem Objekt zu extrahieren

            if(currentVinyl!=null) {

                // Fill the view
                ImageView imageView = (ImageView) itemView.findViewById(R.id.vPicture);
                /*String path = currentVinyl.getCoverSrc();
                Bitmap bmImg = BitmapFactory.decodeFile(path);
                imageView.setImageBitmap(bmImg);*/

                // title:
                TextView makeText = (TextView) itemView.findViewById(R.id.vName);
                String title = "" + currentVinyl.getTitle();
                makeText.setText(title);

                // price:
                TextView yearText = (TextView) itemView.findViewById(R.id.vPrice);
                yearText.setText("" + currentVinyl.getPrice());

                // year:
                TextView locationText = (TextView) itemView.findViewById(R.id.vLocation);
                locationText.setText(currentVinyl.getYear());
            }
            else {
                Toast.makeText(getApplicationContext(), "Nothing to show", Toast.LENGTH_SHORT).show();
            }

            return itemView;
        }
    }
}
