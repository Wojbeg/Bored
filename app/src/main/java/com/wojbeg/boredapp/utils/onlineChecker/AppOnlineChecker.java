package com.wojbeg.boredapp.utils.onlineChecker;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class AppOnlineChecker implements OnlineChecker{

    private final ConnectivityManager connectivityManager;

    public AppOnlineChecker(ConnectivityManager connectivityManager){
        this.connectivityManager = connectivityManager;
    }

    @Override
    public boolean isNetworkAvailable() {


        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo !=null && networkInfo.isConnectedOrConnecting();
        }else{
            Network network = connectivityManager.getActiveNetwork();
            if(network !=null ){
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);

                return (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                );

            }else{
                return false;
            }
        }

    }
}
