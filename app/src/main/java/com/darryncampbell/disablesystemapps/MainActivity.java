package com.darryncampbell.disablesystemapps;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    private static final String LOG_TAG = "Disable System Apps";
    private ArrayList<ListRow> packageEnabledList = new ArrayList<>();
    CustomAdapter packageListAdapter;
    private String operating_system = "";
    private String[][] selectedPackages = null;
    private EMDKProxy emdkProxy = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView packageEnabledListView = ( ListView )findViewById( R.id.list );
        packageListAdapter = new CustomAdapter( this, this.packageEnabledList, getResources());
        packageEnabledListView.setAdapter(packageListAdapter);
        configureApplication();
        populateInformationWindow();
        populateListView(selectedPackages);

        //  Test Only
        //createTestData();

        Button btnEnableSelected = (Button) findViewById(R.id.btnEnableSelected);
        btnEnableSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnableSelectedPackages();
            }
        });

        Button btnDisableSelected = (Button) findViewById(R.id.btnDisableSelected);
        btnDisableSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisableSelectedPackages();
            }
        });

        Button btnSelectAll = (Button) findViewById(R.id.btnSelectAll);
        btnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < packageEnabledList.size(); i++)
                {
                    packageEnabledList.get(i).setChecked(true);
                }
                packageListAdapter.notifyDataSetChanged();
            }
        });

        Button btnSelectNone = (Button) findViewById(R.id.btnSelectNone);
        btnSelectNone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < packageEnabledList.size(); i++)
                {
                    packageEnabledList.get(i).setChecked(false);
                }
                packageListAdapter.notifyDataSetChanged();
            }
        });

        try
        {
            emdkProxy = new EMDKProxy(getApplicationContext());
        }
        catch (NoClassDefFoundError e)
        {
            Log.e(LOG_TAG, "Application will only run on Zebra devices");
            Toast.makeText(getApplicationContext(), "Application will only run on Zebra devices", Toast.LENGTH_LONG);
            TextView informationWindow = (TextView) findViewById(R.id.txtInformation);
            String information = "ERROR: Requires a Zebra device with EMDK support";
            informationWindow.setText(informationWindow.getText() + "\n" + information);
        }
    }

    public void configureApplication()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            operating_system = "KitKat";
            selectedPackages = PackagesList.kitkat_packages;
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            operating_system = "Lollipop 5.0";
            selectedPackages = PackagesList.lollipop_50_packages;
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            operating_system = "Lollipop 5.1";
            selectedPackages = PackagesList.lollipop_51_packages;
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
        {
            operating_system = "Marshmallow";
            selectedPackages = PackagesList.marshmallow_packages;
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1)
        {
            operating_system = "Nougat 7.0";
            selectedPackages = PackagesList.nougat_70_packages;
        }
        else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1)
        {
            operating_system = "Nougat 7.1";
            selectedPackages = PackagesList.nougat_71_packages;
        }
        else
        {
            operating_system = "UNRECOGNIZED";
        }
    }

    public void populateInformationWindow()
    {
        TextView informationWindow = (TextView) findViewById(R.id.txtInformation);
        String information = "Operating System: " + operating_system;
        informationWindow.setText(information);
    }

    public void populateListView(String[][] packageList)
    {
        for (int i = 0; i < packageList.length; i++) {
            final ListRow temp = new ListRow();
            temp.setApplicationName(packageList[i][0]);
            temp.setPackageName(packageList[i][1]);
            temp.setChecked(true);
            packageEnabledList.add(temp);
        }
    }

    public void EnableSelectedPackages()
    {
        for (int i = 0; i < packageEnabledList.size(); i++)
        {
            String packageBeingEnabled = packageEnabledList.get(i).getPackageName();
            Log.i(LOG_TAG, "Enabling Package: " + packageBeingEnabled + " " + packageEnabledList.get(i).getChecked());
            if (emdkProxy != null)
                emdkProxy.processProfile(packageBeingEnabled, true);
        }
    }


    public void DisableSelectedPackages()
    {
        for (int i = 0; i < packageEnabledList.size(); i++)
        {
            String packageBeingDisabled = packageEnabledList.get(i).getPackageName();
            Log.i(LOG_TAG, "Disabling Package: " + packageEnabledList.get(i).getPackageName() + " " + packageEnabledList.get(i).getChecked());
            if (emdkProxy != null)
                emdkProxy.processProfile(packageBeingDisabled, false);
        }

    }

    public void createTestData()
    {
        for (int i = 0; i < 101; i++) {
            final ListRow temp = new ListRow();
            temp.setPackageName("package " + i);
            temp.setChecked(i % 2 == 0);
            packageEnabledList.add(temp);
        }
    }


}
