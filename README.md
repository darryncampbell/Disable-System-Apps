# Disable-System-Apps
Proof of concept to disable and enable various system apps on Zebra GMS devices

*This application is provided without guarantee or warranty.  Disabling the wrong application could require a factory reset to restrore the device to a useable state*
=========================================================

**List of GMS apps per OS**
==============
As far as I know the list of GMS applications on each desert flavour is NDA so I can't include those in this application.  If you just clone and build the master branch the list of applications which can be enabled or disabled is built dynamically from the list of installed applications.
1. By default, the list of applications you can choose from will be those returned by pm.getInstalledApplications(PackageManager.MATCH_SYSTEM_ONLY);  _BE VERY CAREFUL HERE NOT TO DISABLE ALL APPLICATIONS_
2. If you wish to use a default list of applications which may or may not be installed (safer if you accidentally disable all applications), set the 'autoDetectApplications' variable to false: https://github.com/darryncampbell/Disable-System-Apps/blob/master/app/src/main/java/com/darryncampbell/disablesystemapps/MainActivity.java#L25 and ensure the import to the non-existant file is commented out: https://github.com/darryncampbell/Disable-System-Apps/blob/master/app/src/main/java/com/darryncampbell/disablesystemapps/MainActivity.java#L16.  Sample packages are listed under https://github.com/darryncampbell/Disable-System-Apps/blob/master/app/src/main/java/com/darryncampbell/disablesystemapps/PackagesList.java.
3. If you get access to the GMS packages list you can uncomment the import statement in MainActivity.java (https://github.com/darryncampbell/Disable-System-Apps/blob/master/app/src/main/java/com/darryncampbell/disablesystemapps/MainActivity.java#L14) and place the java file at the appropriate location (com/darryncampbell/disablesystemapps/disablesystemapps/UNDER_NDA/).
