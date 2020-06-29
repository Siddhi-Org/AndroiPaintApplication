package com.example.paintapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.UUID;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MyCanvas canvasObj ;
    DrawerLayout drawer;
    NavigationView navigationView;
    View contentView;
    ImageView pencil,eraser,undo,redo;
    private ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        canvasObj = new MyCanvas(this,null);
        setContentView(R.layout.activity_main);
        initObjects();
        initDrawer();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.undo:
                Toast undo = Toast.makeText(getApplicationContext(),
                        "siddhi",
                        Toast.LENGTH_SHORT);
                undo.show();
                break;
            case R.id.redo:
                Toast redo = Toast.makeText(getApplicationContext(),
                        "hey",
                        Toast.LENGTH_SHORT);
                redo.show();
                break;
        }

    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void UserMenuSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            case R.id.Pencil:
                Toast toast = Toast.makeText(getApplicationContext(),
                        "pencil selected",
                        Toast.LENGTH_SHORT);
                toast.show();
               setContentView(canvasObj);
                break;
            case R.id.Eraser:
                Toast toast1 = Toast.makeText(getApplicationContext(),
                        "Eraser selected",
                        Toast.LENGTH_SHORT);
                toast1.show();
                break;
        }
    }

    private void initDrawer()
    {
        drawer.setScrimColor(Color.TRANSPARENT);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,drawer,R.string.drawer_open,R.string.drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelected(item);
                return false;
            }
        });

    }

    private void initObjects()
    {
        pencil = findViewById(R.id.Pencil);
        eraser = findViewById(R.id.Eraser);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        contentView = findViewById(R.id.content);
        undo = findViewById(R.id.undo);
        undo.setOnClickListener(this);
        redo = findViewById(R.id.redo);
        redo.setOnClickListener(this);
    }

    // TODO:  This method will come under save option
    private void showSavePaintingConfirmationDialog(){
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        saveDialog.setTitle("Save drawing");
        saveDialog.setMessage("Save drawing to device Gallery?");
        saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                //save drawing
                canvasObj.setDrawingCacheEnabled(true);
                String imgSaved = MediaStore.Images.Media.insertImage(
                        getContentResolver(), canvasObj.getDrawingCache(),
                        UUID.randomUUID().toString()+".png", "drawing");
                if(imgSaved!=null){
                    Toast savedToast = Toast.makeText(getApplicationContext(),
                            "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                    savedToast.show();
                }
                else{
                    Toast unsavedToast = Toast.makeText(getApplicationContext(),
                            "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                    unsavedToast.show();
                }
                // Destroy the current cache.
                canvasObj.destroyDrawingCache();
            }
        });
        saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        saveDialog.show();
    }


}