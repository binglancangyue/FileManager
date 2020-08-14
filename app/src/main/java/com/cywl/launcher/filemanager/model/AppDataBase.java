/*
package com.cywl.launcher.documentmanagement.model;

import android.net.Uri;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.provider.ContentUri;
import com.raizlabs.android.dbflow.annotation.provider.TableEndpoint;

*/
/**
 * dbflow 数据库
 *//*

@Database(name = AppDataBase.NAME, version = AppDataBase.VERSION)
public class AppDataBase {
    public static final String NAME = "AppDataBase";
    public static final int VERSION = 1;

    public static final String AUTHORITY = "com.cywl.launcher.documentmanagement.provider";

    public static final String BASE_CONTENT_URI = "content://";

    private static Uri buildUri(String... paths) {
        Uri.Builder builder =
                Uri.parse(AppDataBase.BASE_CONTENT_URI + AppDataBase.AUTHORITY).buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    // Declare endpoints here
    @TableEndpoint(name = UserProviderModel.ENDPOINT, contentProvider = AppDataBase.class)
    public static class UserProviderModel {
        public static final String ENDPOINT = "User";

        @ContentUri(path = UserProviderModel.ENDPOINT,
                type = ContentUri.ContentType.VND_MULTIPLE + ENDPOINT)
        public static final Uri CONTENT_URI = buildUri(ENDPOINT);
    }
}
*/
