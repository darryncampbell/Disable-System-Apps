package com.darryncampbell.disablesystemapps;

/**
 * Created by darry on 03/05/2017.
 */

public class ListRow {
    private String packageName = "";
    private String applicationName = "";
    private boolean checked = false;
    private boolean installed = false;

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setInstalled(boolean installed) {
        this.installed = installed;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public boolean getChecked() {
        return checked;
    }

    public boolean getInstalled() { return installed; }
}
