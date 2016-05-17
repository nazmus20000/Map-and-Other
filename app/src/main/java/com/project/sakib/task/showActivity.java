package com.project.sakib.task;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class showActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView t2,t3,t4;
    String S;
    DBhelper D;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        t2=(TextView)findViewById(R.id.name);
        t3=(TextView)findViewById(R.id.phn);
        t4=(TextView)findViewById(R.id.inter);
        S=getIntent().getStringExtra("phn");

        D=new DBhelper(getApplicationContext());
        ArrayList<person> P;
        P=D.getSelectedPerson(S);
        if(P!=null && P.size()>0){
            for(person a: P){
                t2.setText("Name: "+a.getFname()+" "+a.getLname());
                t3.setText("Phone No: "+a.getPhone());
                t4.setText("Interests: "+a.getInterests());

                if(!a.getImage().equals("")) {
                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

                    loadImageFromStorage(directory.getAbsolutePath(), a.getImage());
                }
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "User Doesn't Exists!!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadImageFromStorage(String path,String imagename)
    {

        try {
            File f=new File(path, imagename);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img=(ImageView)findViewById(R.id.im2);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    public void mapclick2(View view)
    {
        Intent goToNextActivity = new Intent(getApplicationContext(), MapsActivity.class);
        goToNextActivity.putExtra("phn", S);
        startActivity(goToNextActivity);
    }
    public void hist(View view)
    {
        Intent goToNextActivity = new Intent(getApplicationContext(), history.class);
        goToNextActivity.putExtra("phn", S);
        startActivity(goToNextActivity);
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
        getMenuInflater().inflate(R.menu.show, menu);
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

        if (id == R.id.nav_uppro) {
            // Handle the camera action
            Intent goToNextActivity = new Intent(getApplicationContext(), profileActivity.class);
            startActivity(goToNextActivity);
            finish();
        }
        else if (id == R.id.nav_log) {
            // Handle the camera action
            Intent goToNextActivity = new Intent(getApplicationContext(), login.class);
            startActivity(goToNextActivity);
            finish();
        }
        else if (id == R.id.nav_map) {
            // Handle the camera action
            Intent goToNextActivity = new Intent(getApplicationContext(), MapsActivity.class);
            goToNextActivity.putExtra("phn", S);
            startActivity(goToNextActivity);
        }
        else if (id == R.id.history) {
            // Handle the camera action
            Intent goToNextActivity = new Intent(getApplicationContext(), history.class);
            goToNextActivity.putExtra("phn", S);
            startActivity(goToNextActivity);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
