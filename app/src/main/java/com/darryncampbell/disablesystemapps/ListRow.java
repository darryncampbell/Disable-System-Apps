package com.darryncampbell.disablesystemapps;

/**
 * Created by darry on 03/05/2017.
 */

public class ListRow {
    private String packageName = "";
    private boolean checked = false;

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getPackageName() {
        return packageName;
    }

    public boolean getChecked() {
        return checked;
    }
}
