package com.darryncampbell.disablesystemapps;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by darry on 03/05/2017.
 */

public class CustomAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    ListRow tempValues = null;

    public CustomAdapter(Activity localActivity, ArrayList localArrayList, Resources resLocal)
    {
        activity = localActivity;
        data = localArrayList;
        res = resLocal;
        inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount()
    {
        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder
    {
        public TextView rowText;
        public CheckBox rowCheck;
    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View inflatedView = convertView;
        final ViewHolder heldView;
        if(convertView==null)
        {
            inflatedView = inflater.inflate(R.layout.custom_list_row, null);
            heldView = new ViewHolder();
            heldView.rowText = (TextView) inflatedView.findViewById(R.id.rowtext);
            heldView.rowCheck = (CheckBox)inflatedView.findViewById(R.id.rowcheck);
            inflatedView.setTag( heldView );
        }
        else
        {
            heldView = (ViewHolder)inflatedView.getTag();
        }

        if(data.size()<=0)
        {
            heldView.rowText.setText("No Package");
            heldView.rowCheck.setChecked(false);
        }
        else
        {
            tempValues=null;
            tempValues = ( ListRow ) data.get( position );
            heldView.rowText.setText( tempValues.getPackageName() );
            heldView.rowCheck.setChecked( tempValues.getChecked() );
        }

        heldView.rowCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                   ListRow row = (ListRow) data.get(position);
                   row.setChecked(isChecked);
               }
           }
        );
        heldView.rowText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heldView.rowCheck.performClick();
            }
        });
        return inflatedView;
    }
}
