package com.cywl.launcher.filemanager.model;

import android.os.Environment;
import android.util.Log;

import com.cywl.launcher.filemanager.model.bean.FileBean;
import com.cywl.launcher.filemanager.model.tools.FileOperationTool;
import com.cywl.launcher.filemanager.model.tools.StoragePathTools;
import com.cywl.launcher.filemanager.model.tools.TypeFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MediaData {
    private static final String TAG = "MediaData";
    private List<FileBean> localList = new ArrayList<>();
    private List<FileBean> sdList = new ArrayList<>();

    public String localPath;
    public String sdPath;

    public void rescan() {
/*        StoragePathTools.getStoragePath(context);
        localPath = StoragePathTools.locatPath;
        sdPath = StoragePathTools.sdCardPath;
        if (localPath != null) {
            showAllByPath(localPath, CustomValue.PATH_TYPE_LOCAL);
        }
        if (sdPath != null) {
            showAllByPath(sdPath, CustomValue.PATH_TYPE_SD_CARD);
        }*/
        localPath = CustomValue.LOCAL_PATH;
        sdPath = CustomValue.SD_CARD_PATH;
        Log.d(TAG, "rescan: StoragePathTools "+ StoragePathTools.getStoragePath(true));
//        Log.d(TAG, "onCreate: " + StoragePathTools.getStoragePath(context,
//                true));
//        Log.d(TAG, "onCreate:sd " + StoragePathTools.getStoragePath(context,
//                false));
        showAllByPath(localPath, CustomValue.PATH_TYPE_LOCAL);
        showAllByPath(sdPath, CustomValue.PATH_TYPE_SD_CARD);
        Log.d("StoragePathTools", "localPath: " + localPath + " \n adpath " + sdPath);
        Log.d("StoragePathTools", "localsize: " + localList.size() + " \n sdsize " + sdList.size());

    }

    private void checkFile(File file, String path, int pathType) {
        FileBean fileBean = new FileBean();
//        int type = TypeFilter.checkFile(path);
        int type = TypeFilter.getFileType(path);
        fileBean.setFile(true);
        fileBean.setName(file.getName());
        fileBean.setPath(path);
        fileBean.setType(type);
        fileBean.setTime(FileOperationTool.getFileLastModifiedTime(file));
        if (pathType == 0) {
            localList.add(fileBean);
        } else {
            sdList.add(fileBean);
        }
    }

    private void showAllByPath(String path, int type) {
        Log.d(TAG, "showAllByPath: path "+path);
        if (path == null) {
            return;
        }
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        List<File> chilPathList = Arrays.asList(file.listFiles());
        Collections.sort(chilPathList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        for (File currentFile : chilPathList) {
            String s = currentFile.toString();
            Log.d(TAG, "showAllByPath:s true " + s);
            File childFile = new File(s);
            int fileType;
            boolean isFile;
            String name = childFile.getName();
            if (childFile.isFile()) {
                fileType = TypeFilter.getFileType(path);
                isFile = true;
            } else {
                if (name.equals(".android_secure")) {
                    break;
                }
                fileType = 0;
                isFile = false;
            }
            FileBean fileBean = new FileBean();
            fileBean.setName(name);
            fileBean.setPath(path);
            fileBean.setFile(isFile);
            fileBean.setType(fileType);
            fileBean.setTime(FileOperationTool.getFileLastModifiedTime(childFile));
            if (type == 0) {
                localList.add(fileBean);
            } else {
                sdList.add(fileBean);
            }
        }

/*        int length = chilPathList.size();
        for (int i = 0; i < length; i++) {
            String s = chilPathList.get(i).toString();
            File childFile = new File(s);
            FileBean fileBean = new FileBean();
            fileBean.setName(childFile.getName());
            fileBean.setPath(path);
            fileBean.setTime(FileOperationTool.getFileLastModifiedTime(childFile));
            if (childFile.isFile()) {
//                int fileType = TypeFilter.checkFile(path);
                int fileType = TypeFilter.getFileType(path);
                fileBean.setFile(true);
                fileBean.setType(fileType);
                Log.d(TAG, "showAllByPath:s true " + s);
            } else {
                fileBean.setName(childFile.getName());
                if (childFile.getName().equals(".android_secure")) {
                    break;
                }
                fileBean.setType(0);
                fileBean.setFile(false);
                Log.d(TAG, "showAllByPath:s false " + s);
            }
            if (type == 0) {
                localList.add(fileBean);
            } else {
                sdList.add(fileBean);
            }
        }*/
    }

    public List<FileBean> getDataByPath(String path) {
        Log.d(TAG, "getDataByPath: path " + path);
        List<FileBean> list = new ArrayList<>();
        File file = new File(path);
        List<File> chilPathList = Arrays.asList(file.listFiles());
        Collections.sort(chilPathList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o2.getName().compareTo(o1.getName());
            }
        });
        for (File currentFile : chilPathList) {
            String s = currentFile.toString();
            File childFile = new File(s);
            if (childFile.getName().equals(".android_secure")) {
                break;
            }
            FileBean fileBean = new FileBean();
            fileBean.setName(childFile.getName());
            fileBean.setPath(s);
            fileBean.setTime(FileOperationTool.getFileLastModifiedTime(childFile));
            if (childFile.isFile()) {
//                int iconType = TypeFilter.checkFile(s);
                int iconType = TypeFilter.getFileType(s);
                fileBean.setFile(true);
                Log.d(TAG, "getDataByPath:iconType " + iconType);
                fileBean.setType(iconType);
            } else {
                fileBean.setType(0);
                fileBean.setFile(false);
            }
            list.add(fileBean);
        }
 /*       int length = chilPathList.size();
        for (int i = 0; i < length; i++) {
            String s = chilPathList.get(i).toString();
            File childFile = new File(s);
            if (childFile.getName().equals(".android_secure")) {
                break;
            }
            FileBean fileBean = new FileBean();
            fileBean.setName(childFile.getName());
            fileBean.setPath(s);
            fileBean.setTime(FileOperationTool.getFileLastModifiedTime(childFile));
            if (childFile.isFile()) {
//                int iconType = TypeFilter.checkFile(s);
                int iconType = TypeFilter.getFileType(s);
                fileBean.setFile(true);
                Log.d(TAG, "getDataByPath:iconType " + iconType);
                fileBean.setType(iconType);
            } else {
                fileBean.setType(0);
                fileBean.setFile(false);
            }
            list.add(fileBean);
        }*/
        return list;
    }

    public List<FileBean> getDataList(int pathType) {
        return pathType == 0 ? localList : sdList;
    }

    private String getSDPath() {
        File sdDir;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录 }
            return sdDir.toString();
        }
        return null;
    }
}
