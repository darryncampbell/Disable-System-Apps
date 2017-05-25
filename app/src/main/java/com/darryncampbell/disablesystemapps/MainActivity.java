package com.darryncampbell.disablesystemapps;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//  Uncomment this import statement if you have the authoritative list of GMS applications.
//  (autoDetectApplications will also need to be set to false)
//import com.darryncampbell.disablesystemapps.UNDER_NDA.PackagesList;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    //  Set this to true to use the PackageManager to generate the list of system apps to present to the user.
    boolean autoDetectApplications = true;

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

        final Button btnSelectToggle = (Button) findViewById(R.id.btnSelectToggle);
        btnSelectToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnSelectToggle.getText().equals("Select\nAll"))
                {
                    //  Select
                    for (int i = 0; i < packageEnabledList.size(); i++)
                    {
                        if (packageEnabledList.get(i).getInstalled())
                            packageEnabledList.get(i).setChecked(true);
                    }

                    btnSelectToggle.setText("Select\nNone");
                }
                else
                {
                    //  Deselect
                    for (int i = 0; i < packageEnabledList.size(); i++)
                    {
                        if (packageEnabledList.get(i).getInstalled())
                            packageEnabledList.get(i).setChecked(false);
                    }

                    btnSelectToggle.setText("Select\nAll");
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
        if (autoDetectApplications)
        {
            final PackageManager pm = getPackageManager();
            List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.MATCH_SYSTEM_ONLY);
            String[][] dynamicPackages = new String [packages.size() - 1][2];

            int count = 0;
            for (int i = 0; i < packages.size(); i++) {
                if (packages.get(i).packageName.equals("com.darryncampbell.disablesystemapps"))
                    continue;   //  Would be a bad idea to disable ourselves
                String packageDescription = packages.get(i).sourceDir;
                if (packageDescription.endsWith("/"))
                    packageDescription = packageDescription.substring(0, packageDescription.length() - 1);
                if (packageDescription.contains("/"))
                    packageDescription = packageDescription.substring(packageDescription.lastIndexOf('/') + 1, packageDescription.length());
                dynamicPackages[count][0] = packageDescription;
                dynamicPackages[count][1] = packages.get(i).packageName;
                count++;
            }
            selectedPackages = dynamicPackages;
            operating_system = "N/A - Applications are dynamically determined";
        }
        else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                operating_system = "KitKat";
                selectedPackages = PackagesList.kitkat_packages;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                operating_system = "Lollipop 5.0";
                selectedPackages = PackagesList.lollipop_50_packages;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 &&
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                operating_system = "Lollipop 5.1";
                selectedPackages = PackagesList.lollipop_51_packages;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                operating_system = "Marshmallow";
                selectedPackages = PackagesList.marshmallow_packages;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N &&
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
                operating_system = "Nougat 7.0";
                selectedPackages = PackagesList.nougat_70_packages;
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                operating_system = "Nougat 7.1";
                selectedPackages = PackagesList.nougat_71_packages;
            } else {
                operating_system = "UNRECOGNIZED";
            }
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
            Log.d(LOG_TAG, "Debug: " + i + packageList[i][1]);
            temp.setApplicationName(packageList[i][0]);
            temp.setPackageName(packageList[i][1]);
            temp.setChecked(false);
            temp.setInstalled(isAppInstalled(packageList[i][1]));
            packageEnabledList.add(temp);
        }
    }

    public void EnableSelectedPackages()
    {
        int overallResult = -1;
        Boolean processResult = false;
        for (int i = 0; i < packageEnabledList.size(); i++)
        {
            if (packageEnabledList.get(i).getChecked()) {
                overallResult = 0;
                String packageBeingEnabled = packageEnabledList.get(i).getPackageName();
                Log.i(LOG_TAG, "Enabling Package: " + packageBeingEnabled);
                if (emdkProxy != null) {
                    processResult = emdkProxy.processProfile(packageBeingEnabled, false);
                }
            }
        }
        if (overallResult != -1)
        {
            if (processResult)
                Toast.makeText(getApplicationContext(), "All selected packages successfully enabled", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "Problem disabling all selected packages", Toast.LENGTH_SHORT).show();
        }

    }


    public void DisableSelectedPackages()
    {
        int overallResult = -1;
        Boolean processResult = false;
        for (int i = 0; i < packageEnabledList.size(); i++)
        {
            if (packageEnabledList.get(i).getChecked()) {
                overallResult = 0;
                String packageBeingDisabled = packageEnabledList.get(i).getPackageName();
                Log.i(LOG_TAG, "Disabling Package: " + packageEnabledList.get(i).getPackageName());
                if (emdkProxy != null) {
                    processResult = emdkProxy.processProfile(packageBeingDisabled, true);
                }
            }
        }
        if (overallResult != -1)
        {
            if (processResult)
                Toast.makeText(getApplicationContext(), "All selected packages successfully disabled", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "Problem disabling all selected packages", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isAppInstalled(String packageName) {
        PackageManager pm = getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }


}
