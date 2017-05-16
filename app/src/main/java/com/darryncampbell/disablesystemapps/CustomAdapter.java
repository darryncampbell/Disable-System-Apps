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
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.color.black;
import static com.darryncampbell.disablesystemapps.R.*;

/**
 * Created by darry on 03/05/2017.
 */

public class CustomAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    ListRow tempValues = null;
    private Context context;

    public CustomAdapter(Activity localActivity, ArrayList localArrayList, Resources resLocal)
    {
        activity = localActivity;
        context = localActivity;
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
        public TextView rowTextPackage;
        public TextView rowTextApplication;
        public CheckBox rowCheck;
    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View inflatedView = convertView;
        final ViewHolder heldView;
        if(convertView==null)
        {
            inflatedView = inflater.inflate(layout.custom_list_row, null);
            heldView = new ViewHolder();
            heldView.rowTextPackage = (TextView) inflatedView.findViewById(id.rowtextPackageName);
            heldView.rowTextApplication = (TextView) inflatedView.findViewById(id.rowtextApplicationName);
            heldView.rowCheck = (CheckBox)inflatedView.findViewById(id.rowcheck);
            inflatedView.setTag( heldView );
        }
        else
        {
            heldView = (ViewHolder)inflatedView.getTag();
        }

        if(data.size()<=0)
        {
            heldView.rowTextPackage.setText("No Package");
            heldView.rowTextApplication.setText("No Application");
            heldView.rowCheck.setChecked(false);
        }
        else {
            tempValues = null;
            tempValues = (ListRow) data.get(position);
            heldView.rowTextPackage.setText(tempValues.getPackageName());
            heldView.rowCheck.setChecked(tempValues.getChecked());
            if (!tempValues.getInstalled()) {
                heldView.rowCheck.setEnabled(false);
                heldView.rowTextPackage.setEnabled(false);
                heldView.rowTextApplication.setEnabled(false);
                heldView.rowTextApplication.setText("(Not Installed) " + tempValues.getApplicationName());
                heldView.rowTextApplication.setTextColor(context.getResources().getColor(color.zebraGray));
            } else
            {
                heldView.rowCheck.setEnabled(true);
                heldView.rowTextPackage.setEnabled(true);
                heldView.rowTextApplication.setEnabled(true);
                heldView.rowTextApplication.setText(tempValues.getApplicationName());
                heldView.rowTextApplication.setTextColor(context.getResources().getColor(black));
            }
        }

/*        heldView.rowCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                     ListRow row = (ListRow) data.get(position);
                     row.setChecked(isChecked);
                 }
             }
        );
*/
        heldView.rowCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListRow row = (ListRow) data.get(position);
                row.setChecked(heldView.rowCheck.isChecked());
            }
        });

        heldView.rowTextPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heldView.rowCheck.performClick();
            }
        });
        heldView.rowTextApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heldView.rowCheck.performClick();
            }
        });
        return inflatedView;
    }
}
