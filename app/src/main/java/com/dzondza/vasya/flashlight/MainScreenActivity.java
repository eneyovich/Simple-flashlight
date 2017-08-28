package com.dzondza.vasya.flashlight;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

/**
 * simple flashlight for android Jelly Bean and higher
 */

public class MainScreenActivity extends AppCompatActivity {

    private Camera mCamera;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        SwitchCompat switchButton = (SwitchCompat) findViewById(R.id.switch_on_off);
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


    //makes flashlight on
    private void flashlightOn() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mCamera = Camera.open();
            cameraParameters(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.startPreview();
        } else {
            flashlightMarshmallow(true);
        }
        mRelativeLayout.setBackground(getResources().getDrawable(R.drawable.drawable_flashlight_on));
    }

    //makes flashlight off
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
        mRelativeLayout.setBackground(getResources().getDrawable(R.drawable.drawable_flashlight_off));
    }


    //sets camera parameters for camera
    private void cameraParameters(String flashMode) {
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFlashMode(flashMode);
        mCamera.setParameters(parameters);
    }


    //turns flashlight on/off for android versions marshmallow and upper
    private void flashlightMarshmallow(boolean onOrOff) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CameraManager mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            try {
                String[] cameraId = mCameraManager.getCameraIdList();
                mCameraManager.setTorchMode(cameraId[0], onOrOff);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }
}