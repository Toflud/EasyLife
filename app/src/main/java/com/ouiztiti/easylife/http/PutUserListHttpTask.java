package com.ouiztiti.easylife.http;

import android.os.AsyncTask;
import android.util.Log;

import com.ouiztiti.easylife.PlaceholderFragment;
import com.ouiztiti.easylife.UserList;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Save List (PUT)
 */
public class PutUserListHttpTask extends AsyncTask<UserList, Void, Void> {

    @Override
    protected Void doInBackground(UserList... params) {
        try {
            UserList userList = params[0];

            final String url = PlaceholderFragment.HTTP_HOST_USER_LIST + userList.getListeName();

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


            restTemplate.put(url, userList) ;
            Log.i("MainActivity", "put done") ;

        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }
        return null ;
    }
}
