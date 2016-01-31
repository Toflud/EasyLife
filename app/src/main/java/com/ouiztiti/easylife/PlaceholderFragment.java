package com.ouiztiti.easylife;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ouiztiti.easylife.http.AddItemHttpTask;
import com.ouiztiti.easylife.http.GetUserListHttpTask;
import com.ouiztiti.easylife.http.MoveItemHttpTask;
import com.ouiztiti.easylife.http.PutUserListHttpTask;

/**
 *  The PlaceholderFragment presents a UserIntent list
 *
 */
public class PlaceholderFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = PlaceholderFragment.class.getSimpleName() ;
    public static final String[] MAPPED_LISTED_NAME = new String[]{"maybe","todo","done"} ;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final String HOST = "ec2-52-30-29-111.eu-west-1.compute.amazonaws.com/" ;
    public static final String HTTP_HOST_USER_LIST = "http://" + HOST + "/user/list/";

    private String listeName ;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment() {
        setHasOptionsMenu(true);
    }

    public final String getListeName() {
        return listeName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate
                (R.layout.fragment_main, container, false);
        return rootView;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i(LOG_TAG, "onViewStateRestored " + listeName) ;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume " + listeName) ;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadContentList();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Òactivity.
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_userlist, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if( id == R.id.action_test_add) {
            // test add
            Log.i(LOG_TAG, "onOptionsItemSelected " + listeName) ;
            testAddItem();
            return true ;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(final View view) {
        // Click on text.
        switch(view.getId()) {
            case R.id.button_popup :
                // We need to post a Runnable to show the popup to make sure that the PopupMenu is
                // correctly positioned. The reason being that the view may change position before
                // the PopupMenu is shown.
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        showPopupMenu(view);
                    }
                });
                break ;
            case android.R.id.text1 :
                boolean mIsLargeLayout = true ;
                // Show Item Editor View Dialog
                // Retrieve the clicked item from view's tag
                final UserIntent item = (UserIntent) view.getTag();
                FragmentManager fragmentManager = getFragmentManager();
                UserIntentEditorFragment editorFragment = UserIntentEditorFragment.newInstance(item, "");
                if(mIsLargeLayout) {
                    editorFragment.show(fragmentManager, "dialog");
                } else {
                    // The device is smaller, so show the fragment fullscreen
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    // For a little polish, specify a transition animation
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    // To make it fullscreen, use the 'content' root view as the container
                    // for the fragment, which is always the root view for the activity
                    transaction.add(android.R.id.content, editorFragment)
                            .addToBackStack(null).commit();
                }

                break ;
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        listeName = MAPPED_LISTED_NAME[getUserListPosition()] ;
    }

    public void loadContentList() {
        Log.d(LOG_TAG, "loadContentList : " + listeName) ;
        GetUserListHttpTask request = new GetUserListHttpTask(this);
        request.execute();
    }

    /**
     * Push UserList to backend server.
     */
    private void saveContentList() {
        ListView listView = (ListView) PlaceholderFragment.this.getView().findViewById(R.id.listView) ;
        final PopupAdapter adapter = (PopupAdapter) listView.getAdapter();
        UserList userList = adapter.getUserList() ;
        PutUserListHttpTask put = new PutUserListHttpTask();
        put.execute(userList) ;
    }

    /**
     * Update the presented list
     * @param list
     */
    public void setUserList(UserList list) {
        if(list==null) {
            Log.e(PlaceholderFragment.class.getSimpleName(), "UserList[" + listeName + "] == null") ;
            return ;
        }

        View rootView = getView() ;
        if(rootView==null) {
            Log.e(PlaceholderFragment.class.getSimpleName(), "Root fragement is null") ;
            return ;
        }

        TextView listeNameText = (TextView) rootView.findViewById(R.id.liste_name);
        TextView listeSizeText = (TextView) rootView.findViewById(R.id.liste_size);
        listeNameText.setText(list.getListeName());
        listeSizeText.setText(String.valueOf(list.getListe().size()));

        ListView listView = (ListView) rootView.findViewById(R.id.listView) ;

        if(list==null) {
            Log.e(PlaceholderFragment.class.getSimpleName(), "List " + listeName + "is NULL") ;
            return ;
        }

        ArrayAdapter adapter = new PopupAdapter(this, list) ;


        listView.setAdapter(adapter);
    }

    /**
     * Add Item ? POST to Liste
     */
    private void testAddItem() {
        new AddItemHttpTask(this).execute() ;
    }

    /**
     *
     * @param itemPosition
     * @param listNameDestination
     */
    private void testMove(final int itemPosition, final String listNameDestination) {

        new MoveItemHttpTask(this, itemPosition, listNameDestination).execute() ;
    }


    // BEGIN_INCLUDE(show_popup)
    private void showPopupMenu(View view) {
        ListView listView = (ListView) getView().findViewById(R.id.listView) ;
        final PopupAdapter adapter = (PopupAdapter) listView.getAdapter();

        // Retrieve the clicked item from view's tag
        final UserIntent item = (UserIntent) view.getTag();

        // Create a PopupMenu, giving it the clicked view for an anchor
        PopupMenu popup = new PopupMenu(getActivity(), view);

        // Inflate our menu resource into the PopupMenu's Menu
        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());

        // Set a listener so we are notified if a menu item is clicked
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_remove:
                        // Remove the item from the adapter
                        adapter.remove(item);

                        /** **/
                        saveContentList();
                        /** **/

                        return true;
                    case R.id.menu_move_to_left: {
                        // Check if movement is possible
                        int position = getUserListPosition();
                        if (position == 0) return true;
                        int itemPosition = adapter.getPosition(item) ;
                        String listNameDestination = MAPPED_LISTED_NAME[position-1] ;
                        testMove(itemPosition, listNameDestination);
                        return true;
                    }
                    case R.id.menu_move_to_right: {
                        //
                        int position = getUserListPosition();
                        if (position == (MAPPED_LISTED_NAME.length-1)) return true;
                        int itemPosition = adapter.getPosition(item) ;
                        String listNameDestination = MAPPED_LISTED_NAME[position+1] ;
                        testMove(itemPosition, listNameDestination);
                        return true;
                    }
                }
                return false;
            }
        });

        // Finally show the PopupMenu
        popup.show();
    }



    /**
     * Get the user list position (section number)
     * @return
     */
    private int getUserListPosition() {
        return getArguments().getInt(ARG_SECTION_NUMBER);
    }
    // END_INCLUDE(show_popup)


}
