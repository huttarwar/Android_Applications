package com.example.heramb.know_your_govt;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static android.R.drawable.ic_dialog_alert;

public class PersonActivity extends AppCompatActivity {


    Official_Person officialPerson = new Official_Person();
    private TextView location;
    private TextView postofofficial;
    private TextView nameOfficial;
    private TextView party;
    private TextView address1;
    private TextView phone;
    private TextView email;
    private TextView website;

    private ImageButton imageButtongoogle;
    private ImageButton imageButtonfacebook;
    private ImageButton imageButtonyoutube;
    private ImageButton imageButtontwitter;
    private ImageView imageView;
    private String address = null;

    private static final int NEW = 1;

    HashMap<String, String> hashMapChannels = new HashMap<String, String>();

    private static final String TAG = "PersonActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_activity);

        ActionBar actionBar=getSupportActionBar();
        Intent intent = getIntent();

        officialPerson = (Official_Person) intent.getSerializableExtra("officialPerson");
        address = (String) intent.getSerializableExtra("location");
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.constraintId);


        if (officialPerson.getParty().equals("Republican")) {
            constraintLayout.setBackgroundColor(Color.RED);
        } else if (officialPerson.getParty().equals("Democratic")) {
            constraintLayout.setBackgroundColor(Color.BLUE);
        } else {
            constraintLayout.setBackgroundColor(Color.BLACK);
        }

        if (officialPerson != null) {

            location = (TextView) findViewById(R.id.location);

            location.setBackgroundColor(getResources().getColor(R.color.back_purple));
            location.setText(address);

            postofofficial = (TextView) findViewById(R.id.designation);
            postofofficial.setText(officialPerson.getPostofofficial());

            nameOfficial = (TextView) findViewById(R.id.nameOfficial);
            nameOfficial.setText(officialPerson.getNameofofficial());

            party = (TextView) findViewById(R.id.party);
            party.setText("(" + officialPerson.getParty() + ")");

            address1 = (TextView) findViewById(R.id.address1);
            address1.setText(officialPerson.getLine1() + "\n" + officialPerson.getLine2() + " " + officialPerson.getCity() +
                    ", " + officialPerson.getState() + " " + officialPerson.getZipcode());
            address1.setLinkTextColor(Color.WHITE);

            Linkify.addLinks(address1, Linkify.MAP_ADDRESSES);

            phone = (TextView) findViewById(R.id.phones);
            phone.setText(officialPerson.getPhones());
            phone.setLinkTextColor(Color.WHITE);
            Linkify.addLinks(((TextView) findViewById(R.id.phones)), Linkify.PHONE_NUMBERS);


            email = (TextView) findViewById(R.id.emails);
            if (officialPerson.getEmails() == null) {
                email.setText("No data provided");
            } else {
                email.setText(officialPerson.getEmails());
                email.setLinkTextColor(Color.WHITE);
                Linkify.addLinks(((TextView) findViewById(R.id.emails)), Linkify.EMAIL_ADDRESSES);
            }
            website = (TextView) findViewById(R.id.websites);
            if (officialPerson.getWebsites() == null) {
                website.setText("No data provided");
            } else {
                website.setText(officialPerson.getWebsites());
                website.setLinkTextColor(Color.WHITE);
                Linkify.addLinks(((TextView) findViewById(R.id.websites)), Linkify.WEB_URLS);
            }

            imageButtongoogle = (ImageButton) findViewById(R.id.googleplus);
            imageButtonfacebook = (ImageButton) findViewById(R.id.facebook);
            imageButtonyoutube = (ImageButton) findViewById(R.id.youtube);
            imageButtontwitter = (ImageButton) findViewById(R.id.twitter);

            hashMapChannels = officialPerson.getChannels();

            try {
                if (hashMapChannels.get("GooglePlus").equals("12345")) {
                    imageButtongoogle.setVisibility(View.INVISIBLE);
                }

                if (hashMapChannels.get("Facebook").equals("12345")) {
                    imageButtonfacebook.setVisibility(View.INVISIBLE);
                }

                if (hashMapChannels.get("Twitter").equals("12345")) {
                    imageButtontwitter.setVisibility(View.INVISIBLE);
                }

                if (hashMapChannels.get("YouTube").equals("12345")) {
                    imageButtonyoutube.setVisibility(View.INVISIBLE);
                }
            }catch (Exception e){

            }

                int result = 0;
                result = networkDialog(this);
                if (result == 1) {
                    imageView = (ImageView) findViewById(R.id.imageView);
                    imageView.setImageResource(R.drawable.placeholder);
                } else {
                    if (!officialPerson.getPhotoUrls().equals("NoPhoto")) {
                        imageView = (ImageView) findViewById(R.id.imageView);
                        loadImage(officialPerson.getPhotoUrls());
                    } else {
                        imageView = (ImageView) findViewById(R.id.imageView);
                        imageView.setImageResource(R.drawable.missingimage);
                    }
                }
                imageView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        openPhotoActivity(view);

                    }

                });
            }
        }


        @Override
        protected void onResume () {
            super.onResume();
        }


        @Override
        protected void onPause () {
            super.onPause();
        }

    public void openPhotoActivity(View view) {
        Intent intent = new Intent(PersonActivity.this, PhotoActivity.class);
        intent.putExtra("officialPerson", officialPerson);
        intent.putExtra("location", address);
        startActivityForResult(intent, NEW);
    }


    public int networkDialog(Context context) {

        int result = 0;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
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


    public void facebookClicked(View v) {
        String FACEBOOK_URL = "https://www.facebook.com/" + officialPerson.getChannels().get("Facebook");
        String urlToUse;

        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;

            } else {
                urlToUse = "fb://page/" + officialPerson.getChannels().get("Facebook");
            }
        } catch (PackageManager.NameNotFoundException e) {

            urlToUse = FACEBOOK_URL;
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);

    }

    public void twitterClicked(View v) {
        Intent intent = null;
        String name = officialPerson.getChannels().get("Twitter");
        try {
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }

    public void googlePlusClicked(View v) {
        String name = officialPerson.getChannels().get("GooglePlus");
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus", "com.google.android.apps.plus.phones.UrlGatewayActivity");
            intent.putExtra("customAppUri", name);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + name)));
        }
    }

    public void youTubeClicked(View v) {
        String name = officialPerson.getChannels().get("YouTube");
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + name)));
        }
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