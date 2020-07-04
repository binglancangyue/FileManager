package com.cywl.launcher.documentmanagement.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class InitDataService extends IntentService {

    public InitDataService() {
        super("InitDataService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //初始化DBFLOW
//        FlowManager.init(this);
//        FlowManager.init(new FlowConfig.Builder(this).build());
    }
}
