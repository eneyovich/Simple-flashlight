package com.dzondza.vasya.flashlight;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

/*
simple flashlight for android Jelly Bean and older
 */

public class MainScreenActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraManager mCameraManager = null;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Switch switchButton = (Switch) findViewById(R.id.switch_button);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    flashlightOn();
                } else {
                    flashlightOff();
                }
            }
        });
    }


    private void flashlightOn() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mCamera = Camera.open();
            cameraParameters(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.startPreview();
        } else {
            flashlightMarshmallow(true);
        }
        mRelativeLayout.setBackground(getResources().getDrawable(R.drawable.flashlight_on_image));
    }

    private void flashlightOff() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (mCamera != null) {
                cameraParameters(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        } else {
            flashlightMarshmallow(false);
        }
        mRelativeLayout.setBackground(getResources().getDrawable(R.drawable.flashlight_off_image));
    }


    private void cameraParameters(String flashMode) {
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFlashMode(flashMode);
        mCamera.setParameters(parameters);
    }

    private void flashlightMarshmallow(boolean onOrOff) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            try {
                String[] cameraId = mCameraManager.getCameraIdList();
                mCameraManager.setTorchMode(cameraId[0], onOrOff);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }
}