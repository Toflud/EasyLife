package com.ouiztiti.easylife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * A simple array adapter that creates a list of UserIntent.
 */
class PopupAdapter extends ArrayAdapter<UserIntent> {

    private PlaceholderFragment placeholderFragment;
    private UserList userList ;


    PopupAdapter(PlaceholderFragment placeholderFragment, UserList userList) {
        super(placeholderFragment.getActivity(), R.layout.list_item, android.R.id.text1, userList.getListe());
        this.placeholderFragment = placeholderFragment;
        this.userList = userList ;
    }

    public UserList getUserList() {
        return userList;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup container) {
        // Let ArrayAdapter inflate the layout and set the text
        //View view = super.getView(position, convertView, container);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, container, false);
        }
        TextView tv = (TextView)convertView.findViewById(android.R.id.text1) ;

        UserIntent item = getItem(position) ;
        tv.setText(item.getContent());
        // Set the item as the text(s tag so it can be retrieved later to build the intent editor
        tv.setTag(item);
        // Set the fragment instance as the OnClickListener
        tv.setOnClickListener(placeholderFragment);

        // BEGIN_INCLUDE(button_popup)
        // Retrieve the popup button from the inflated view
        View popupButton = convertView.findViewById(R.id.button_popup);

        // Set the item as the button's tag so it can be retrieved later
        popupButton.setTag(item);

        // Set the fragment instance as the OnClickListener
        popupButton.setOnClickListener(placeholderFragment);
        // END_INCLUDE(button_popup)

        // Finally return the view to be displayed
        return convertView;
    }
}
