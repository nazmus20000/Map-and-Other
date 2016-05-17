package com.project.sakib.task;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class profileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String img="";
    Spinner staticSpinner;
    DBhelper D;
    EditText fname,lname,phone;
    ImageView UpIm=null;
    private static final int RESULT_LOAD_IMAGE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        staticSpinner= (Spinner) findViewById(R.id.static_spinner);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.brew_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
                staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner.setAdapter(staticAdapter);

        fname=(EditText)findViewById(R.id.fname);
        phone=(EditText)findViewById(R.id.phnno);
        lname=(EditText)findViewById(R.id.lname);
        UpIm=(ImageView)findViewById(R.id.imaggg);
    }

    public static String random() {
        Random r = new Random();
        int i1 = r.nextInt(1000080 - 1) + 1;
        return Integer.toString(i1);
    }

    private String saveToInternalStorage(Bitmap bitmapImage) throws IOException {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());

        String random=random();
        random+=".jpg";
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,random);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        return random;
    }

    public void regif(View V) throws IOException {

        String C=staticSpinner.getSelectedItem().toString();
        String FN=fname.getText().toString();
        String LN=lname.getText().toString();
        String Ph=phone.getText().toString();
        if(!Ph.equals("")){
            long val=0;
            D=new DBhelper(getApplicationContext());
            ArrayList<person> P;
            P=D.getSelectedPerson(Ph);
            if(P!=null && P.size()>0) {
                Toast.makeText(getApplicationContext(), "Phone No Already Exists", Toast.LENGTH_LONG).show();
            }
            else
            {
                if(!UpIm.equals(null))
                {
                    Bitmap imm=((BitmapDrawable)UpIm.getDrawable()).getBitmap();
                    img=saveToInternalStorage(imm);
                }
                person a=new person(FN,LN,Ph,C,img);
                val = D.InsertPerson(a);
            }
            if(val>0){
                Toast.makeText(getApplicationContext(), "Your Information is Successfully Saved", Toast.LENGTH_LONG).show();
                Intent goToNextActivity = new Intent(getApplicationContext(), showActivity.class);
                goToNextActivity.putExtra("phn", Ph);
                startActivity(goToNextActivity);
                finish();
            }
        }
        else Toast.makeText(getApplicationContext(), "Fill up the required fields.", Toast.LENGTH_LONG).show();
    }

    public void up(View V){

        Intent gallaryInsert=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallaryInsert,RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_LOAD_IMAGE&&resultCode==RESULT_OK&&data!=null)
        {
            Uri select=data.getData();
            UpIm.setImageURI(select);
        }
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
        getMenuInflater().inflate(R.menu.profile, menu);
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
            Toast.makeText(getApplicationContext(), "You Are Already in Registration Page", Toast.LENGTH_LONG).show();
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
            startActivity(goToNextActivity);
            finish();
        }
        else if (id == R.id.history) {
            // Handle the camera action
            Toast.makeText(getApplicationContext(), "You can only access this section after login", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
