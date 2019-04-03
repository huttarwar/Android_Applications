package com.example.heramb.know_your_govt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static android.R.drawable.ic_dialog_alert;

public class PhotoActivity extends AppCompatActivity {

    Official_Person officialPerson = new Official_Person();
    String addresDisplay = null;
    private TextView locationinphoto;
    private TextView postinphoto;
    private TextView nameinphoto;
    private ImageView imageView;
    private String TAG = "PhotoActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity);
        ActionBar actionBar=getSupportActionBar();
        Intent intent = getIntent();

        officialPerson = (Official_Person)intent.getSerializableExtra("officialPerson");
        addresDisplay = (String)intent.getSerializableExtra("location");
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.photoConst);

        if(officialPerson.getParty().equals("Republican")) {
            constraintLayout.setBackgroundColor(Color.RED);
        }
        else if(officialPerson.getParty().equals("Democratic"))
        {
            constraintLayout.setBackgroundColor(Color.BLUE);
        }
        else
        {
            constraintLayout.setBackgroundColor(Color.BLACK);
        }


        if(officialPerson != null) {

            locationinphoto = (TextView)findViewById(R.id.photoLocation);
            locationinphoto.setBackgroundColor(getResources().getColor(R.color.back_purple));
            locationinphoto.setText(addresDisplay);


            postinphoto = (TextView)findViewById(R.id.photoDesignation);
            postinphoto.setText(officialPerson.getPostofofficial());

            nameinphoto = (TextView) findViewById(R.id.photoName);
            nameinphoto.setText(officialPerson.getNameofofficial());

        }


        int result = 0;
        result = networkDialog(this);

        if(result == 1)
        {
            imageView = (ImageView)findViewById(R.id.imageViewBig);
            imageView.setImageResource(R.drawable.placeholder);

        }
        else {

            if (!officialPerson.getPhotoUrls().equals("NoPhoto")) {
                imageView = (ImageView) findViewById(R.id.imageViewBig);
                loadImage(officialPerson.getPhotoUrls());
            } else {
                imageView = (ImageView) findViewById(R.id.imageViewBig);
                imageView.setImageResource(R.drawable.missingimage);
            }
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
    }


    @Override
    protected  void onPause(){
        super.onPause();
    }




    public int networkDialog(Context context) {

        int result = 0;
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            //Toast.makeText(this, "You ARE Connected to the Internet!", Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            Intent data = getIntent();
            builder.setTitle("No Network Connection");
            builder.setIcon(ic_dialog_alert);
            builder.setMessage("Data cannot be accessed/loaded without an internet connection");
            AlertDialog dialog = builder.create();
            dialog.show();
            result = 1;

        }
        return result;
    }



    private void loadImage(final String imageURL) {

        Log.d(TAG, "loadImage: " + imageURL);

        Picasso picasso = new Picasso.Builder(this)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Log.d(TAG, "onImageLoadFailed: ");
                        picasso.load(R.drawable.brokenimage)
                                .into(imageView);
                    }
                })
                .build();

        picasso.load(imageURL)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(imageView);
    }
}
