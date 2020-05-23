package com.example.techolutionassignment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Tab2 extends Fragment {

    ViewGroup rootView;
    private final int CAMERRA_REQUEST_CODE = 2;
    boolean hasCameraFlash = false;
    ToggleButton toggleButton;
    LinearLayout wifi_tv;
    RecyclerView recyclerView;
    WifiManager wifiManager;
    boolean flag=false;
    ProgressBar progressBar;


    BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            boolean success = intent.getBooleanExtra(
                    WifiManager.EXTRA_RESULTS_UPDATED, false);
            progressBar.setVisibility(View.INVISIBLE);
            if (success) {
                List<ScanResult> wifiScanList = wifiManager.getScanResults();
                Log.e(TAG, "onReceive: "+ wifiScanList.size());
                recyclerView.setAdapter(new RecyclerViewAdapter(wifiScanList,getContext()));
            } else {
                // scan failure handling
                Toast.makeText(getContext(), "Failed",Toast.LENGTH_LONG).show();
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_tab2, container, false);

        connectWifi();
        changeBrightess();
        switchFlashlight();

        return rootView;
    }

    @Override
    public void onPause() {
        if(flag)
            getContext().unregisterReceiver(wifiScanReceiver);
        super.onPause();
    }

    void connectWifi()
    {
        wifi_tv=rootView.findViewById(R.id.wifi_tv);
        recyclerView=rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar=rootView.findViewById(R.id.progbar);
        wifi_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Intent panelIntent = new Intent(Settings.Panel.ACTION_WIFI);
                    startActivityForResult(panelIntent, 0);
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    flag=true;
                    wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(true);
                    getContext().registerReceiver(wifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                    boolean success = wifiManager.startScan();
                    Log.e(TAG, "onClick: Scanning "+success);
                }
            }
        });
    }

    void switchFlashlight(){
        hasCameraFlash = getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        toggleButton=rootView.findViewById(R.id.toggleButton);
        askPermission(Manifest.permission.CAMERA, CAMERRA_REQUEST_CODE);
    }

    void flashLight()
    {
        if(hasCameraFlash){
            toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        flashLightOn();
                    }
                    else{
                        flashLightOff();
                    }
                }
            });
        }
        else {
            Toast.makeText(getContext(), "No Flash Available on your device",Toast.LENGTH_LONG).show();
        }
    }

    private void askPermission(String permission, int requestCode) {

        if(ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
        }
        else {
            flashLight();
        }

    }

    private void flashLightOn() {
        CameraManager cameraManager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
        } catch (CameraAccessException e) {
        }
    }

    private void flashLightOff() {
        CameraManager cameraManager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
        } catch (CameraAccessException e) {
        }
    }

    void changeBrightess(){
        SeekBar seekBar=rootView.findViewById(R.id.seekbar);
        int brightness = Settings.System.getInt(getContext().getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, 0);
        seekBar.setProgress(brightness);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Settings.System.putInt(getContext().getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS, i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
