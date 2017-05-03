package com.darryncampbell.disablesystemapps;

/**
 * Created by darry on 03/05/2017.
 */

import android.content.Context;
import android.util.Log;

import com.symbol.emdk.*;

public class EMDKProxy implements EMDKManager.EMDKListener {

    //  Zebra EMDK
    private ProfileManager profileManager = null;
    private EMDKManager emdkManager;
    private static final String LOG_TAG = "Disable System Apps";

    public EMDKProxy(Context context)
    {
        //The EMDKManager object will be created and returned in the callback.
        EMDKResults results = EMDKManager.getEMDKManager(context, this);
        //Check the return status of getEMDKManager
        if (results.statusCode == EMDKResults.STATUS_CODE.SUCCESS) {
            // EMDKManager object creation success
        } else {
            // EMDKManager object creation failed
            Log.e(LOG_TAG, "Could not create EMDK Manager");
        }
    }


    public void processProfile(String packageBeingProcessed, boolean disableApplication)
    {
        String action = "enabled";
        if (disableApplication)
            action = "disabled";
        if (profileManager != null)
        {
            String[] modifyData = new String[1];
            modifyData[0] = DisableApplicationProfile(packageBeingProcessed, disableApplication);

            EMDKResults results = profileManager.processProfile(packageBeingProcessed,
                    ProfileManager.PROFILE_FLAG.SET, modifyData);

            if (results.statusCode == EMDKResults.STATUS_CODE.CHECK_XML)
                Log.i(LOG_TAG, packageBeingProcessed + " successfully " + action);
            else
                Log.w(LOG_TAG, packageBeingProcessed + " was NOT " + action);
        }

    }

    public String DisableApplicationProfile(String packageName, boolean disableApplication)
    {
        String disableOrEnable = "EnableApplication";
        if (disableApplication)
            disableOrEnable = "DisableApplication";
        String profile = "  <characteristic type=\"Profile\">\n" +
                "    <parm name=\"ProfileName\" value=\"ApplicationManager\"/>\n" +
                "    <parm name=\"ModifiedDate\" value=\"2017-01-25 08:07:34\"/>\n" +
                "    <parm name=\"TargetSystemVersion\" value=\"6.0\"/>\n" +
                "    <characteristic type=\"AppMgr\" version=\"5.1\">\n" +
                "      <parm name=\"emdk_name\" value=\"AppManager\"/>\n" +
                "      <parm name=\"Action\" value=\""+ disableOrEnable + "\"/>\n" +
                "      <parm name=\"Package\" value=\"" + packageName + "\"/>\n" +
                "    </characteristic>\n" +
                "  </characteristic>";
        return profile;
    }


    @Override
    public void onOpened(EMDKManager emdkManager) {
        this.emdkManager = emdkManager;
        // Get the ProfileManager object to process the profiles
        profileManager = (ProfileManager) emdkManager
                .getInstance(EMDKManager.FEATURE_TYPE.PROFILE);
    }

    @Override
    public void onClosed() {
        profileManager = null;
    }

}
