package com.cywl.launcher.documentmanagement.model.tools;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

import static android.content.ContentValues.TAG;

/**
 * @author Altair
 * @date :2019.10.25 下午 02:08
 * @description:
 */
public class RequestPermissionsTool {
    private boolean isTrue = false;

    @SuppressLint("CheckResult")
    public void requestPermissions(Activity activity) {
        isTrue = false;
        String[] strings = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        RxPermissions rxPermission = new RxPermissions(activity);
        rxPermission.requestEach(strings)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) {
                        if (permission.granted) {// 用户已经同意该权限
                            isTrue = true;
                            Log.d(TAG, permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            Log.d(TAG, permission.name + " is denied. More info should be " +
                                    "provided.");
                            isTrue = false;
                        } else { // 用户拒绝了该权限，并且选中『不再询问』
                            isTrue = false;
                            Log.d(TAG, permission.name + " is denied.");
                        }
                    }
                });
    }

    public boolean getGrant() {
        return isTrue;
    }
}
