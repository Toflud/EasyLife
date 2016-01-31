package com.ouiztiti.easylife.http;

import android.os.AsyncTask;
import android.util.Log;

import com.ouiztiti.easylife.PlaceholderFragment;
import com.ouiztiti.easylife.UserListListener;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by cpe on 31/01/16.
 */
public class MoveItemHttpTask extends AsyncTask<Void, Void, Void> {

    private PlaceholderFragment placeholderFragment;
    private int itemPosition;
    private String listNameDestination;

    public MoveItemHttpTask(PlaceholderFragment placeholderFragment, final int itemPosition, final String listNameDestination) {
        this.placeholderFragment = placeholderFragment;
        this.itemPosition = itemPosition;
        this.listNameDestination = listNameDestination;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {

            final String url = PlaceholderFragment.HTTP_HOST_USER_LIST + placeholderFragment.getListeName() + "/" + listNameDestination;
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            URI uri = restTemplate.postForLocation(url, new Integer(itemPosition));
            Log.i("MainActivity", "postForLocation result = " + uri);
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void list) {
        // Test TODO HTTP RESULT !!
        // reload me
        placeholderFragment.loadContentList();
        // broadcast reload event !
        String[] targetListNames = {listNameDestination};
        ((UserListListener) placeholderFragment.getActivity()).onBroadCast(targetListNames);
    }

}
