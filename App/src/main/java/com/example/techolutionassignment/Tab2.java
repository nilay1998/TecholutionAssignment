package com.example.techolutionassignment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class Tab2 extends Fragment {

    ViewGroup rootView;
    private final int CAMERRA_REQUEST_CODE = 2;
    boolean hasCameraFlash = false;
    ToggleButton toggleButton;
    TextView wifi_tv;
    RecyclerView recyclerView;
    WifiManager wifiManager;

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

    void connectWifi()
    {
        wifi_tv=rootView.findViewById(R.id.wifi_tv);
        recyclerView=rootView.findViewById(R.id.recyclerView);
        wifi_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Intent panelIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                    startActivityForResult(panelIntent, 0);
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
