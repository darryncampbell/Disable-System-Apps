# Disable-System-Apps
Proof of concept to disable and enable various system apps on Zebra GMS devices

** List of GMS apps per OS **
==============
As far as I know the list of GMS applications on each desert flavour is NDA so I can't include those in this application.  If you just clone and build the master branch a sample list of applications is presented in the UI that you can enable or disable but there is no guarantee those make sense for your device.

If you get access to the GMS packages list you can uncomment the import statement in MainActivity.java (https://github.com/darryncampbell/Disable-System-Apps/blob/master/app/src/main/java/com/darryncampbell/disablesystemapps/MainActivity.java#L14) and place the java file at the appropriate location (com/darryncampbell/disablesystemapps/disablesystemapps/UNDER_NDA/).