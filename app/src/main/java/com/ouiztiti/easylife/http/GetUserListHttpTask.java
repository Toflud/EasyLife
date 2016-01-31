package com.ouiztiti.easylife.http;

import android.os.AsyncTask;
import android.util.Log;

import com.ouiztiti.easylife.PlaceholderFragment;
import com.ouiztiti.easylife.UserList;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Read List (GET)
 */
public class GetUserListHttpTask extends AsyncTask<Void, Void, UserList> {

    private PlaceholderFragment placeholderFragment;

    public GetUserListHttpTask(PlaceholderFragment placeholderFragment) {
        this.placeholderFragment = placeholderFragment;
    }

    @Override
    protected UserList doInBackground(Void... params) {
        try {
            final String url = PlaceholderFragment.HTTP_HOST_USER_LIST + placeholderFragment.getListeName();
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            UserList userList = restTemplate.getForObject(url, UserList.class);
            return userList;
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(UserList list) {
        placeholderFragment.setUserList(list);
    }

}
