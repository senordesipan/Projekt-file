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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageButton addButton, nameButton, locationButton, favoriteButton;
    Button searchButton;
    EditText searchField;
    TextView example;
    int searchRequest = 0;
    //Testfeld, kommt noch weg

    private List<Platte> myVinyl = new ArrayList<Platte>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUi();
        exampleVinylList();
        populateListView();
        initListeners();
    }

    private void setupUi() {
        addButton = (ImageButton) (findViewById(R.id.addButton));
        searchButton = (Button) (findViewById(R.id.searchButton));
        searchField = (EditText) (findViewById(R.id.searchField));

        favoriteButton = (findViewById(R.id.favoriteButton));
        locationButton = (findViewById(R.id.locationButton));
        nameButton = findViewById(R.id.nameButton);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initListeners() {
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
                String searchText = searchField.getText().toString();
                executeSearch(searchText);
                populateListView();
            }
        });
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchField.setHint("Search by Location");
                searchRequest = 1;
            }
        });
        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchField.setHint("Search by Title");
                searchRequest = 2;
            }
        });
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchField.setHint("Search favorite Title");
                searchRequest = 3;
                showFavs();
               // populateListView();

            }
        });
    }

    private void executeSearch(final String searchText) {
        if (searchRequest==1){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    myVinyl.clear();
                    myVinyl.add(PlatteDatabase.getInstance(getApplicationContext()).daoAccess().findPlatteByLocation(searchText));
                }
            }).start();


        }else if (searchRequest == 2){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    myVinyl.clear();
                    myVinyl.add(PlatteDatabase.getInstance(getApplicationContext()).daoAccess().findPlatteByTitle(searchText));

                }
            }).start();

        }else if(searchRequest ==3){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    myVinyl.clear();
                    myVinyl.add(PlatteDatabase.getInstance(getApplicationContext()).daoAccess().findFavTitle(true, searchText));

                }
            }).start();

        }
    }

    private void showFavs() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                myVinyl = PlatteDatabase.getInstance(getApplicationContext()).daoAccess().findFavs(true);
                Log.d("******DEBUG", myVinyl.get(0).getTitle());
            }
        }).start();

    }

    private void exampleVinylList() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                myVinyl = PlatteDatabase.getInstance(getApplicationContext()).daoAccess().selectAll();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

            }
        }).start();

        final String searchtext = searchField.getText().toString();

        Log.d("***DEBUG***", searchtext);
    }


    private void populateListView() {

        final ArrayAdapter<Platte> adapter = new MyListAdapter();
        final ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Intent intent = new Intent(MainActivity.this, DetailView.class);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("*****DEBUGG", String.valueOf(myVinyl.get(position).getPlattenID()));
                        intent.putExtra("vKey", myVinyl.get(position).getPlattenID());
                        startActivity(intent);
                    }
                }).start();
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                      PlatteDatabase.getInstance(getApplicationContext()).daoAccess().deletePlatte(myVinyl.get(position));
                      runOnUiThread(new Runnable() {
                          @Override
                          public void run() {

                          }
                      });


                   }
               }).start();
                return false;
            }
        });
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

            // Find the vinyl to work with.
            final Platte currentVinyl = myVinyl.get(position);
            //Hier versuche ich den ganzen shit aus dem Objekt zu extrahieren

            if (currentVinyl != null) {

                // Fill the view
                if (currentVinyl.getCoverSrc() != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            File imgFile = new File(currentVinyl.getCoverSrc());

                            if (imgFile.exists()) {

                                final Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ImageView myImage = (ImageView) findViewById(R.id.vPicture);
                                        myImage.setImageBitmap(myBitmap);
                                    }
                                });


                            }
                        }

                    }).start();
                }


                    // title:
                    TextView makeText = (TextView) itemView.findViewById(R.id.vName);
                    String title = "" + currentVinyl.getTitle() + " - " + currentVinyl.getBand();
                    makeText.setText(title);

                    // price:
                    TextView yearText = (TextView) itemView.findViewById(R.id.vPrice);
                    yearText.setText(currentVinyl.getPrice() + " €");

                    // year:
                    TextView locationText = (TextView) itemView.findViewById(R.id.vLocation);
                    locationText.setText(currentVinyl.getYear());
                } else {
                    Toast.makeText(getApplicationContext(), "Nothing to show", Toast.LENGTH_SHORT).show();
                }

                return itemView;
            }
        }
    }

