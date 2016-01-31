package com.ouiztiti.easylife.http;

import android.os.AsyncTask;
import android.util.Log;

import com.ouiztiti.easylife.PlaceholderFragment;
import com.ouiztiti.easylife.UserIntent;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cpe on 31/01/16.
 */
public class AddItemHttpTask extends AsyncTask<Void, Void, Void> {

    private PlaceholderFragment placeholderFragment;

    public AddItemHttpTask(PlaceholderFragment placeholderFragment) {
        this.placeholderFragment = placeholderFragment;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {

            //String listeName = PlaceholderFragment.MAPPED_LISTED_NAME[curItem] ;

            final String url = PlaceholderFragment.HTTP_HOST_USER_LIST + placeholderFragment.getListeName();
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            UserIntent userIntent = new UserIntent();
            userIntent.setContent("[TEST] " + placeholderFragment.getListeName() + "(" + SimpleDateFormat.getDateInstance().format(new Date()) + ")");
            URI uri = restTemplate.postForLocation(url, userIntent);
            Log.i("MainActivity", "postForLocation result = " + uri);
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void list) {
        // reload
        placeholderFragment.loadContentList();
    }

}
