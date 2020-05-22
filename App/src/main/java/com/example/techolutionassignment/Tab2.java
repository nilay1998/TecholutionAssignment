package com.example.techolutionassignment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Tab2 extends Fragment {

    ViewGroup rootView;
    private final int CAMERRA_REQUEST_CODE = 2;
    boolean hasCameraFlash = false;
    ToggleButton toggleButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_tab2, container, false);

        changeBrightess();
        switchFlashlight();

        return rootView;
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
