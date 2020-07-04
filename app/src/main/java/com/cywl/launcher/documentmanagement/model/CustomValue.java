package com.cywl.launcher.documentmanagement.model;

import com.cywl.launcher.documentmanagement.R;
import com.cywl.launcher.documentmanagement.model.tools.StoragePathTools;

public class CustomValue {
    public final static int PATH_TYPE_LOCAL = 0;
    public final static int PATH_TYPE_SD_CARD = 1;
    public static final int FILE_TYPE_NO = 0;

    public final static int HANDLE_UPDATA_TEXTVIEW = 1;
    public final static int HANDLE_INIT_DATA = 2;

    public final static int HANDLE_RENAME = 4;
    public final static int HANDLE_EXIT_APP = 5;
    public final static int HANDLE_DELETE_FILE = 6;
    public final static int HANDLE_PASTE_FILE = 7;

    public final static int OPERATION_TYPE_COPY = 0;
    public final static int OPERATION_TYPE_CUT = 1;

    public static final String LOCAL_PATH = "/storage/emulated/0";
    //    public static final String SD_CARD_PATH = "/storage/0000-0000";// 0000-006F
    public static final String SD_CARD_PATH = StoragePathTools.getStoragePath(true);

    public static final String LOCAL_NAME = "内部存储";
    public static final String SD_CARD_NAME = "外部存储";


    public static final int FILE_TYPE_MUSIC = 1;
    public static final int FILE_TYPE_VIDEO = 2;
    public static final int FILE_TYPE_PICTURE = 3;
    public static final int FILE_TYPE_TEXT = 4;
    public static final int FILE_TYPE_PDF = 5;
    public static final int FILE_TYPE_WORD = 6;
    public static final int FILE_TYPE_EXCEL = 7;
    public static final int FILE_TYPE_PPT = 8;
    public static final int FILE_TYPE_HTML = 9;
    public static final int FILE_TYPE_APK = 10;
    public static final int FILE_TYPE_ISO = 11;
    public static final int[] FILE_TYPE = {
            FILE_TYPE_NO, FILE_TYPE_MUSIC, FILE_TYPE_VIDEO, FILE_TYPE_PICTURE,
            FILE_TYPE_TEXT, FILE_TYPE_PDF, FILE_TYPE_WORD, FILE_TYPE_EXCEL, FILE_TYPE_PPT,
            FILE_TYPE_HTML, FILE_TYPE_APK, FILE_TYPE_ISO};

    public static final int[] FILE_ICON = {
            R.drawable.litter_file, R.drawable.litter_music, R.drawable.litter_video,
            R.drawable.litter_picture, R.drawable.litter_txt, R.drawable.litter_pdf,
            R.drawable.litter_doc, R.drawable.litter_xls, R.drawable.litter_ppt,
            R.drawable.litter_xml, R.drawable.litter_apk, R.drawable.litter_disk};
}
