package com.sreeyainfotech.currentlocation.model;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;


public class Utilities {
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT_SERVER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");//yyyy-MM-dd'T'HH:mm:ss
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT1 = new SimpleDateFormat("MMM dd, yyyy hh:mm aa");//MMM dd, yyyy hh:mm:ss aa
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy  HH:mm");//MMM dd, yyyy hh:mm:ss aa
    public static final SimpleDateFormat CUSTOM_DATE_FORMAT = new SimpleDateFormat("MM/dd");
    public static final SimpleDateFormat CUSTOM_TIME_FORMAT = new SimpleDateFormat("hh:mm aa");
    private static final int MAX_LENGTH = 15;
    public static boolean isLoged_In;

//    public static JsonArray rewardsJson = new JsonArray();
//    public static JsonArray locationsJson = new JsonArray();

    public static int screenWidth, screenHeight;
    public static boolean isDelivered;
    public static boolean from_document_button;
    public static boolean isLastStop;
    public static boolean callPopBackStack;
    public static boolean callMove;
    public static boolean isPrevious_StopSchouldclose;
    public static boolean pointConverted;
    public static boolean uploadBtnClicked;
    public static boolean location_start;
    public static String Load_Number;
    public static String Stop_Id;
    public static boolean loadConfirmed;
    public static boolean service;

    public static void showAlert(final Context mContext, String message) {
        final AlertDialog.Builder alert_Dialog = new AlertDialog.Builder(mContext);
        alert_Dialog.setCancelable(false);
        alert_Dialog.setTitle("Alert");
        alert_Dialog.setMessage(message);
        alert_Dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert_Dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert_Dialog.show();
    }

    public static void hideKeyboard(View v) {
        InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isFailover())
            return false;
        else if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }

    public static void saveLoginPref(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences("Login_Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getLoginPref(Context context, String key, String defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences("Login_Preferences", Context.MODE_PRIVATE);
        return sharedPref.getString(key, defaultValue);
    }

    public static void saveLoginbooleanPref(Context context, String key, boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences("Login_Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static Boolean getLoginBooleanPref(Context context, String key, boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences("Login_Preferences", Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, value);
    }

    public static void clearLoginPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("Login_Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
    }

    public static void savebooleanPref(Context context, String key, boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences("Login_Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBooleanPref(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences("Login_Preferences", Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, defaultValue);
    }

    public static void savePref(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences("MobileApp_Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getPref(Context context, String key, String defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences("MobileApp_Preferences", Context.MODE_PRIVATE);
        return sharedPref.getString(key, defaultValue);
    }

    public static void saveIntegerPref(Context context, String key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences("MobileApp_Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getIntegerPref(Context context, String key, int defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences("MobileApp_Preferences", Context.MODE_PRIVATE);
        return sharedPref.getInt(key, defaultValue);
    }

    public static void removePreferencesValue(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences("MobileApp_Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        editor.apply();
        editor.commit();
    }

//    public static void saveToDefaultPreference(String key, String value){
//        MyAppFont.preferences.edit().putString(key, value).apply();
//    }
//
//    public static String getFromDefaultPreference(String key, String defaultValue){
//        return MyAppFont.preferences.getString (key, defaultValue);
//    }

    public static void clearPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("MobileApp_Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
        editor.commit();
    }

    public static void saveFCM_TOKEN(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences("FCM_TOKEN_Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
        editor.commit();
    }

    public static String getFCM_TOKEN(Context context, String key, String defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences("FCM_TOKEN_Preferences", Context.MODE_PRIVATE);
        return sharedPref.getString(key, defaultValue);
    }

    public static void showToast(Context mContext, String mesg) {
        Toast.makeText(mContext, mesg, Toast.LENGTH_SHORT).show();
    }

    public static String getConvertedDate(String date) {
        try {
            //SIMPLE_DATE_FORMAT_SERVER.setTimeZone(TimeZone.getTimeZone("UTC"));
            return SIMPLE_DATE_FORMAT.format(SIMPLE_DATE_FORMAT_SERVER.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    public static String getConvertedDateForChat(String date) {
        try {
            //SIMPLE_DATE_FORMAT_SERVER.setTimeZone(TimeZone.getTimeZone("UTC"));
            return SIMPLE_DATE_FORMAT1.format(SIMPLE_DATE_FORMAT_SERVER.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    public static String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    /**
     * Function to get Progress percentage
     *
     * @param currentDuration
     * @param totalDuration
     */
    public static int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    /**
     * Function to change progress to timer
     *
     * @param progress      -
     * @param totalDuration returns current duration in milliseconds
     */
    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy hh:mm:ss a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String fileSize(int size) {
        String hrSize = "";
        int k = size;
        //double m = size / 1024.00;
        //double g = size / 1048576.00;
        //double t = size / 1073741824.00;

        double bytes = size;
        double kilobytes = (bytes / 1024);
        double m = (kilobytes / 1024);
        double g = (m / 1024);
        double t = (g / 1024);

        DecimalFormat dec = new DecimalFormat("0.00");

        if (t > 1) {
            hrSize = dec.format(t).concat(" TB");
        } else if (g > 1) {
            hrSize = dec.format(g).concat(" GB");
        } else if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else {
            hrSize = dec.format(size).concat(" KB");
        }

        return hrSize;
    }

    public static Bitmap StringToBitMap(String encodedString) {
        Bitmap bitmap = null;
        try {
            byte[] encodeByte = Base64.decode(encodedString.getBytes(), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return bitmap;

        }
    }

    public static String getcustomDateFormat(String date) {
        try {
            //SIMPLE_DATE_FORMAT_SERVER.setTimeZone(TimeZone.getTimeZone("UTC"));
            return CUSTOM_DATE_FORMAT.format(SIMPLE_DATE_FORMAT_SERVER.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    /**
     * function md5 encryption for passwords
     *
     * @return passwordEncrypted
     */
    public static String convertPassMd5(String pass) {
        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pass.getBytes(), 0, pass.length());
            pass = new BigInteger(1, mdEnc.digest()).toString(16);
            while (pass.length() < 32) {
                pass = "0" + pass;
            }
            password = pass;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return password;
    }

    public static String getCurrentVersion(Context mContext) {
        String version = "";
        try {
            version = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static int getStatusBarHeight(Context mContext) {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    public static String generateUsername() {
        String generatedString = "";
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        generatedString = buffer.toString();

        return generatedString;
    }

    public static void showSettingsAlert(final Context mContext) {
        // TODO Auto-generated method stub
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(mContext);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }


    public static boolean isServiceRunning(Context mContext) {
        boolean serviceRunning = false;
        ActivityManager manager = (ActivityManager) mContext.getSystemService(mContext.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.protrans.driverapp.backgroundServices.LocationService".equals(service.service.getClassName())) {
                serviceRunning =  true;
            }
        }
         return serviceRunning;
    }
}
