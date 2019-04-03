package com.example.heramb.know_your_govt;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class CivicInfoDownloader extends AsyncTask<Official_Person, Void, String> {

    MainActivity mainActivity;

    Official_Person officialPerson;
    private int count;


    private String dataURL = "https://www.googleapis.com/civicinfo/v2/representatives?key=";
    private String addressURL = "&address=";
    private String apiKey = "AIzaSyCtkfabKX_QMfqw7DQZ0J5xFXI6OaoqpLw";
    private String dataURLMain = dataURL +apiKey+ addressURL;

    private static final String TAG="CivicInfoDownloader";

    String addressLine1 = null, addressLine2 = null;
    String city = null, state = null, zipcode = null, type = null;
    String id = null, photo = null, channeltype = null, channelid = null;
    String urls_of_official = null;
    String address1 = null;
    String post = null;
    String name_of_official = null, address = null, phone = null, email = null, party = null;
    String location;
    ArrayList<Official_Person> offialDataList =  new ArrayList<Official_Person>();


    HashMap<String, ArrayList<Official_Person>> objectList = new HashMap<String, ArrayList<Official_Person>>();

    public CivicInfoDownloader(MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected String doInBackground(Official_Person... params) {

        String URLtosearch = dataURLMain +params[0].getZipcode();

        Uri dataUri = Uri.parse(URLtosearch);
        String final_url = dataUri.toString();

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(final_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            InputStream is = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            //sb.append("[{\"company_name\": \"NULL\", \"company_symbol\": \"123\", \"listing_exchange\": \"NULL\"}]");
            //return sb.toString();
        }

        Log.d(TAG, "doInBackground: " + sb.toString());


        return sb.toString();


    }

    @Override
    protected void onPostExecute(String s) {

        parseJSON(s);
        mainActivity.setOfficialPersonList(objectList);

    }


    private HashMap<String, ArrayList<Official_Person>> parseJSON(String s) {
        ArrayList<Official_Person> officialPersonList = new ArrayList<>();
        try {

            JSONObject jsonObject = new JSONObject(s);
            JSONObject response = jsonObject.getJSONObject("normalizedInput");
            count = response.length();
            String city = jsonObject.getJSONObject("normalizedInput").getString("city");
            String state = jsonObject.getJSONObject("normalizedInput").getString("state");
            String zipCode = jsonObject.getJSONObject("normalizedInput").getString("zip");

//For displaying the location in main activity

            if(city!=null || state!=null || zipCode!=null)
            {
                location = city +", "+ state +" "+ zipCode;
            }
            JSONObject json1 = new JSONObject(s);
            JSONObject json2 = new JSONObject(s);

            JSONArray jsonArray_Offices = json1.getJSONArray("offices");
            Log.d(TAG, "Length of offices: " +jsonArray_Offices.length());

            for(int i =0; i < jsonArray_Offices.length(); i++)
            {
                post = jsonArray_Offices.getJSONObject(i).getString("name");
                JSONArray offices = (jsonArray_Offices.getJSONObject(i).getJSONArray("officialIndices"));

                int k = jsonArray_Offices.getJSONObject(i).getJSONArray("officialIndices").length();

                for (int j = 0; j < k; j++)
                {
                    Official_Person official_person = new Official_Person();
                    int id = offices.getInt(j);

                    official_person.setPostofofficial(post);

                    name_of_official = json1.getJSONArray("officials").getJSONObject(id).getString("name");
                    official_person.setNameofofficial(name_of_official);
                    try
                    {
                        if(json1.getJSONArray("officials").getJSONObject(id).has("party"))
                        {
                            party = json1.getJSONArray("officials").getJSONObject(id).getString("party");
                        }
                        else {
                            party = "Unknown";
                        }
                        official_person.setParty(party);
                    }

                    catch (JSONException e)
                    {

                    }
                    if(json1.getJSONArray("officials").getJSONObject(id).has("address"))
                    {
                        try
                        {
                            {
                                JSONObject line1 = json2.getJSONArray("officials").getJSONObject(id).getJSONArray("address").getJSONObject(0);
                                if (line1.has("line1")) {
                                    addressLine1 = line1.getString("line1");
                                }
                                if (line1.has("line2")) {
                                    addressLine2 = line1.getString("line2");
                                }
                                if (line1.has("city")) {
                                    this.city = line1.getString("city");
                                }
                                if (line1.has("state")) {
                                    this.state = line1.getString("state");
                                }
                                if (line1.has("line2")) {
                                    zipcode = line1.getString("zip");
                                }
                                official_person.setLine1(addressLine1);
                                official_person.setLine2(addressLine2);
                                official_person.setCity(this.city);
                                official_person.setState(this.state);
                                official_person.setZipcode(zipcode);

                                Log.i("Address", "[" + j + "]" + ":" + addressLine1 + " " + addressLine2 + " " + this.city + " " + this.state + " " + zipcode);

                            }
                        }
                        catch (JSONException e)
                        {
                            Log.e(TAG, "JSON: ", e);
                        }
                    }
                    if(json1.getJSONArray("officials").getJSONObject(id).has("phones"))
                    {

                        phone = (json1.getJSONArray("officials").getJSONObject(id).getJSONArray("phones").get(0).toString());
                        official_person.setPhones(phone);

                    }
                    if(json1.getJSONArray("officials").getJSONObject(id).has("urls"))
                    {

                        urls_of_official =  json1.getJSONArray("officials").getJSONObject(id).getJSONArray("urls").get(0).toString();

                        official_person.setUrls(urls_of_official);
                        official_person.setWebsites(urls_of_official);


                    }

                    if(json1.getJSONArray("officials").getJSONObject(id).has("photoUrl"))
                    {

                        photo = json1.getJSONArray("officials").getJSONObject(id).getString("photoUrl");
                        official_person.setPhotoUrls(photo);

                    }
                    else
                    {
                        photo = "NoPhoto";
                        official_person.setPhotoUrls(photo);
                    }

                    if(json1.getJSONArray("officials").getJSONObject(id).has("emails"))
                    {

                        email = json1.getJSONArray("officials").getJSONObject(id).getJSONArray("emails").get(0).toString();
                        official_person.setEmails(email);

                    }

                    if(json1.getJSONArray("officials").getJSONObject(id).has("channels"))
                    {
                        HashMap<String, String> hashMap = new HashMap<String, String>();

                        hashMap.put("GooglePlus","12345");
                        hashMap.put("Facebook","12345");
                        hashMap.put("Twitter","12345");
                        hashMap.put("YouTube","12345");

                        JSONArray jsonChannels = json1.getJSONArray("officials").getJSONObject(id).getJSONArray("channels");

                        for ( int pntr = 0; pntr < jsonChannels.length(); pntr++)
                        {

                            if (jsonChannels.getJSONObject(pntr).getString("type").equals("GooglePlus")) {
                                hashMap.put("GooglePlus", jsonChannels.getJSONObject(pntr).getString("id"));
                            }

                            if (jsonChannels.getJSONObject(pntr).getString("type").equals("Facebook")) {
                                hashMap.put("Facebook", jsonChannels.getJSONObject(pntr).getString("id"));
                            }
                            if (jsonChannels.getJSONObject(pntr).getString("type").equals("Twitter")) {
                                hashMap.put("Twitter", jsonChannels.getJSONObject(pntr).getString("id"));
                            }

                            if (jsonChannels.getJSONObject(pntr).getString("type").equals("YouTube")) {
                                hashMap.put("YouTube", jsonChannels.getJSONObject(pntr).getString("id"));
                            }


                        }

                        official_person.setChannels(hashMap);


                    }





                    officialPersonList.add(official_person);

                    objectList.put(location, officialPersonList);
                }
            }

            return objectList;

        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }



}
