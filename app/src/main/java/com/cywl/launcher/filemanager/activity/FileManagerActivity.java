package com.cywl.launcher.filemanager.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cywl.launcher.filemanager.R;
import com.cywl.launcher.filemanager.adapter.RecyclerViewAdapter;
import com.cywl.launcher.filemanager.adapter.ViewPageAdapter;
import com.cywl.launcher.filemanager.model.CustomValue;
import com.cywl.launcher.filemanager.model.MediaData;
import com.cywl.launcher.filemanager.model.bean.FileBean;
import com.cywl.launcher.filemanager.model.listener.OnDialogListener;
import com.cywl.launcher.filemanager.model.listener.OnRecyclerViewListener;
import com.cywl.launcher.filemanager.model.tools.FileManagementDialogTool;
import com.cywl.launcher.filemanager.model.tools.FileOperationTool;
import com.cywl.launcher.filemanager.model.tools.RequestPermissionsTool;
import com.cywl.launcher.filemanager.model.tools.ToastTool;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.RxActivity;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FileManagerActivity extends RxActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener, OnRecyclerViewListener, OnDialogListener {
    private static final String TAG = "FileManagerMentActivity";
    private Context mContext;
    /*private LinearLayout llBack;
    private LinearLayout llLocal;
    private LinearLayout llSDCard;
    private LinearLayout llPaste;*/
    private LinearLayout llBack;
    private TextView llLocal;
    private TextView llSDCard;
    private TextView llPaste;
    private List<FileBean> localList;
    private List<FileBean> sdList;
    private ViewPager viewPager;
    private RecyclerViewAdapter localAdapter;
    private RecyclerViewAdapter sdAdapter;
    private XRecyclerView localXRecyclerView;
    private XRecyclerView sdXRecyclerView;
    private ViewPageAdapter adapter;
    private List<View> viewList;
    private int localCount = 15;
    private int sdCount = 15;
    private MediaData mediaData;
    private int mCurrentItem = 0;
    private String mcurrentLocalPath;
    private String mcurrentSDPath;
    private TextView tvPath;
    private InnerHandler mHandler = new InnerHandler(this);
    private AlertDialog reNameDialog = null;
    private AlertDialog deleteDiaglog = null;
    private AlertDialog longClickDialog;
    private ProgressDialog waitingDialog;

    private String mLongClickPath = null;
    private boolean isExit = false;
    private String copyFilePath = null;
    private int operationType = -1;
    private int pasteIndex = -1;
    private int cutIndex = -1;
    private String mCopyPath;
    private String targetPath;
    private boolean copyOrCut = false;
    private CompositeDisposable compositeDisposable;
    private boolean isSameDirectory;
    private RequestPermissionsTool requestPermissionsTools;
    private FileManagementDialogTool mDialogTool;
    private boolean showPalseBtn;
    private FrameLayout flPaste;
    private TextView tvCanal;


    private static class InnerHandler extends Handler {
        private final WeakReference<FileManagerActivity> activityWeakReference;

        private InnerHandler(FileManagerActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            FileManagerActivity activity = activityWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case CustomValue.HANDLE_UPDATA_TEXTVIEW:
                        activity.updataPathName();
                        break;
                    case CustomValue.HANDLE_RENAME:
                        activity.reNameFile();
                        break;
                    case CustomValue.HANDLE_EXIT_APP:
                        activity.isExit = false;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_management);
        this.mContext = this;
        compositeDisposable = new CompositeDisposable();
        requestPermissionsTools = new RequestPermissionsTool();
        mDialogTool = new FileManagementDialogTool(this, this);
        initView();
        getFileData();
    }

    private void getFileData() {
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        requestPermissions(FileManagerActivity.this);
                        setViewPagerAdapter();
//                        initFirstLocalPagerData();
//                        initFirstSDCadrPagerData();
                    }
                });
            }
        });
    }

    @SuppressLint("CheckResult")
    public void requestPermissions(Activity activity) {

        String[] strings = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        RxPermissions rxPermission = new RxPermissions(activity);
        rxPermission.requestEach(strings)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) {
                        if (permission.granted) {// 用户已经同意该权限
                            initFirstData(0, "0", CustomValue.LOCAL_PATH,
                                    CustomValue.LOCAL_NAME);
                            initFirstData(1, "Tfcard", CustomValue.SD_CARD_PATH,
                                    CustomValue.SD_CARD_NAME);
                            updataPathName();
                            setXRecyclerViewAdapter();
                            setListener();
                            getData();
                            Log.d(TAG, permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            Log.d(TAG, permission.name + " is denied. More info should be " +
                                    "provided.");
                            finish();
                        } else { // 用户拒绝了该权限，并且选中『不再询问』
                            finish();
                            Log.d(TAG, permission.name + " is denied.");
                        }
                    }
                });
    }

    private void initData() {
        mediaData = new MediaData();
        mediaData.rescan();
        localList = mediaData.getDataList(CustomValue.PATH_TYPE_LOCAL);
        sdList = mediaData.getDataList(CustomValue.PATH_TYPE_SD_CARD);
        Log.d(TAG, "localsize: " + localList.size() + " \n sdsize " + sdList.size());
    }

    private void initView() {
        viewPager = findViewById(R.id.viewpager);
        tvPath = findViewById(R.id.tv_path);
        llBack = findViewById(R.id.ctl_back);
        llLocal = findViewById(R.id.ctl_local);
        llPaste = findViewById(R.id.ctl_paste);
        llSDCard = findViewById(R.id.ctl_sd_card);
        flPaste = findViewById(R.id.fl_paste);
        tvCanal = findViewById(R.id.tv_canal);

        viewpagerAddItemView();
        pagerViewClick(0);

    }

    private void viewpagerAddItemView() {
        viewList = new ArrayList<>();
        setRecyclerView();
    }

    private void setRecyclerView() {
        localXRecyclerView = new XRecyclerView(mContext);
        sdXRecyclerView = new XRecyclerView(mContext);

        LinearLayoutManager managerLocal = new LinearLayoutManager(mContext);
        managerLocal.setOrientation(XRecyclerView.VERTICAL);
        localXRecyclerView.setLayoutManager(managerLocal);

        LinearLayoutManager managerSDCard = new LinearLayoutManager(mContext);
        managerSDCard.setOrientation(XRecyclerView.VERTICAL);
        sdXRecyclerView.setLayoutManager(managerSDCard);

        ((DefaultItemAnimator) localXRecyclerView.getItemAnimator()).setSupportsChangeAnimations
                (false);
        ((DefaultItemAnimator) sdXRecyclerView.getItemAnimator()).setSupportsChangeAnimations
                (false);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(mContext,
                XRecyclerView.VERTICAL);
        itemDecoration.setDrawable((Objects.requireNonNull(ContextCompat.getDrawable(mContext,
                R.drawable.transparent_dividing_verticall_line))));
        localXRecyclerView.addItemDecoration(itemDecoration);
        sdXRecyclerView.addItemDecoration(itemDecoration);

        localXRecyclerView.setHasFixedSize(true);
        localXRecyclerView.setItemViewCacheSize(20);
        localXRecyclerView.setDrawingCacheEnabled(true);
        localXRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        sdXRecyclerView.setHasFixedSize(true);
        sdXRecyclerView.setItemViewCacheSize(20);
        sdXRecyclerView.setDrawingCacheEnabled(true);
        sdXRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        viewList.add(localXRecyclerView);
        viewList.add(sdXRecyclerView);
    }

    /**
     * @param type          0:用于每次打开初始化和按Back键返回到本地存储最外层
     *                      1:用于每次打开初始化和按Back键返回到SD卡最外层
     * @param name          存储最外层名字
     * @param path          根目录路径
     * @param currentSDPath 当前路径
     */
    private void initFirstData(int type, String name, String path, String currentSDPath) {
        sdList = new ArrayList<>();
        FileBean fileBean = new FileBean();
        fileBean.setFile(false);
        fileBean.setType(0);
        fileBean.setPath(path);
        fileBean.setName(name);
        if (type == 0) {
            mcurrentLocalPath = currentSDPath;
            localList = new ArrayList<>();
            localList.add(fileBean);
        } else {
            mcurrentSDPath = currentSDPath;
            sdList = new ArrayList<>();
            sdList.add(fileBean);
        }
    }

    private void setViewPagerAdapter() {
        adapter = new ViewPageAdapter(viewList);
        viewPager.setCurrentItem(0);
        viewPager.setAdapter(adapter);
    }

    private void setXRecyclerViewAdapter() {
        localAdapter = new RecyclerViewAdapter(mContext, localList);
        sdAdapter = new RecyclerViewAdapter(mContext, sdList);

        localAdapter.setHasStableIds(true);
        sdAdapter.setHasStableIds(true);

        localXRecyclerView.setAdapter(localAdapter);
        sdXRecyclerView.setAdapter(sdAdapter);
    }

    private void setListener() {
        viewPager.addOnPageChangeListener(this);
        llBack.setOnClickListener(this);
        llLocal.setOnClickListener(this);
        llPaste.setOnClickListener(this);
        llSDCard.setOnClickListener(this);
        tvCanal.setOnClickListener(this);
        setRecyclerViewLoadMoreListener();
        localAdapter.setOnRecyclerViewItemListener(this);
        sdAdapter.setOnRecyclerViewItemListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ctl_back:
                doBack();
                break;
            case R.id.ctl_local:
                pagerViewClick(0);
                mHandler.sendEmptyMessage(CustomValue.HANDLE_UPDATA_TEXTVIEW);
                break;
            case R.id.ctl_sd_card:
                pagerViewClick(1);
                mHandler.sendEmptyMessage(CustomValue.HANDLE_UPDATA_TEXTVIEW);
                break;
            case R.id.ctl_paste:
                if (copyFilePath == null) {
                    return;
                }
                flPaste.setVisibility(View.GONE);
                mDialogTool.showWaitingDialog(R.string.perform_operation);
//                showWaitingDialog(R.string.perform_operation);
                pasteIndex = mCurrentItem;
                doPasteFile();
                break;
            case R.id.tv_canal:
                doCannal();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClickListener(int position, String path) {
        if (path == null) {
            Log.e(TAG, "onItemClickListener:path == null ");
            ToastTool.showToast(R.string.sd_card_null);
            return;
        }
        File file = new File(path);
        if (!file.exists()) {
            if (path.equals(CustomValue.SD_CARD_PATH)) {
                Log.e(TAG, "onItemClickListener:path == SD_CARD_PATH ");
                ToastTool.showToast(R.string.sd_card_null);
            } else {
                ToastTool.showToast(R.string.unable_open_directory);
            }
            return;
        }
        Log.d(TAG, "onItemClickListener: positon " + position + " name : " + path);
        if (!file.isFile()) {
            updataAdapter(path);
        } else {
            FileOperationTool.openFile(path);
        }
    }

    @Override
    public void onItemLongClickListener(int position, String path) {
        Log.d(TAG, "onItemLongClickListener: positon " + position + " " + path);
        mLongClickPath = path;
        doCannal();
        mDialogTool.showLongClickDialog();
    }

    private void setRecyclerViewLoadMoreListener() {
        localXRecyclerView.setPullRefreshEnabled(false);
        sdXRecyclerView.setPullRefreshEnabled(false);
        localXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                localCount += 10;
                localAdapter.setItemCount(localCount);
                localXRecyclerView.loadMoreComplete();
            }
        });
        sdXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                sdCount += 10;
                Log.d(TAG, "onLoadMore: " + sdCount);

                sdAdapter.setItemCount(sdCount);
                sdXRecyclerView.loadMoreComplete();
            }
        });
    }

    @Override
    public void doSomeThing(int type) {
        switch (type) {
            case 1:
                flPaste.setVisibility(View.VISIBLE);
                copyFile();
                break;
            case 2:
                flPaste.setVisibility(View.VISIBLE);
                cutFile();
                break;
            case 3:
                doDeleteFile();
                break;
            default:
                mHandler.sendEmptyMessage(CustomValue.HANDLE_RENAME);
                break;
        }
    }

    /**
     * 全局刷新
     *
     * @param path 更新地址
     */
    private void updataAdapter(String path) {
        List<FileBean> list;
        if (path.equals(CustomValue.SD_CARD_NAME) || path.equals(CustomValue.LOCAL_NAME)) {
            return;
        }
        if (mCurrentItem == CustomValue.PATH_TYPE_LOCAL) {
            mcurrentLocalPath = path;
            list = mediaData.getDataByPath(path);
            localAdapter.setData(list);
            localAdapter.notifyDataSetChanged();
        } else {
            mcurrentSDPath = path;
            list = mediaData.getDataByPath(path);
            sdAdapter.setData(list);
            sdAdapter.notifyDataSetChanged();
        }
        mHandler.sendEmptyMessage(CustomValue.HANDLE_UPDATA_TEXTVIEW);
    }

    /**
     * 指定currentItem刷新
     *
     * @param path        更新地址
     * @param currentItem 更新的currentItem
     */
    private void updataAdapter(String path, int currentItem) {
        List<FileBean> list;
        if (path.equals(CustomValue.SD_CARD_NAME) || path.equals(CustomValue.LOCAL_NAME)) {
            return;
        }
        if (currentItem == CustomValue.PATH_TYPE_LOCAL) {
            mcurrentLocalPath = path;
            list = mediaData.getDataByPath(path);
            localAdapter.setData(list);
            localAdapter.notifyDataSetChanged();
        } else {
            mcurrentSDPath = path;
            list = mediaData.getDataByPath(path);
            sdAdapter.setData(list);
            sdAdapter.notifyDataSetChanged();
        }
    }


    private void updateButtonBg(int positon) {
        if (positon == 0) {
            llLocal.setSelected(true);
            llSDCard.setSelected(false);
        } else {
            llLocal.setSelected(false);
            llSDCard.setSelected(true);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mHandler.sendEmptyMessage(CustomValue.HANDLE_UPDATA_TEXTVIEW);
    }

    @Override
    public void onPageSelected(int position) {
        pagerViewClick(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void pagerViewClick(int position) {
        viewPager.setCurrentItem(position);
        mCurrentItem = position;
        updateButtonBg(position);
    }

    /**
     * 返回上一页和退出App
     */
    private void doBack() {
        int index;
        String previousPath;
        if (mCurrentItem == 0) {
            Log.d(TAG, "doBack: " + mcurrentLocalPath);
            if (mcurrentLocalPath.equals(CustomValue.LOCAL_NAME)) {
                exit();
                return;
            }
            if (mcurrentLocalPath.equals(mediaData.localPath)) {
                initFirstData(0, "0", CustomValue.LOCAL_PATH, CustomValue.LOCAL_NAME);
                mHandler.sendEmptyMessage(CustomValue.HANDLE_UPDATA_TEXTVIEW);
                localAdapter.setData(localList);
                localAdapter.notifyDataSetChanged();
            } else {
                index = mcurrentLocalPath.lastIndexOf("/");
                previousPath = mcurrentLocalPath.substring(0, index);
                mcurrentLocalPath = previousPath;
                updataAdapter(mcurrentLocalPath);
            }
        } else {
            if (mcurrentSDPath.equals(CustomValue.SD_CARD_NAME)) {
                exit();
                return;
            }
            if (mcurrentSDPath.equals(mediaData.sdPath)) {
                initFirstData(1, "Tfcard", CustomValue.SD_CARD_PATH,
                        CustomValue.SD_CARD_NAME);
                mHandler.sendEmptyMessage(CustomValue.HANDLE_UPDATA_TEXTVIEW);
                sdAdapter.setData(sdList);
                sdAdapter.notifyDataSetChanged();
            } else {
                index = mcurrentSDPath.lastIndexOf("/");
                previousPath = mcurrentSDPath.substring(0, index);
                mcurrentSDPath = previousPath;
                updataAdapter(mcurrentSDPath);
            }
        }
    }

    private void doCannal() {
        copyFilePath = null;
        flPaste.setVisibility(View.GONE);
    }

    private void updataPathName() {
        if (mCurrentItem == 0) {
            tvPath.setText(mcurrentLocalPath);
        } else {
            tvPath.setText(mcurrentSDPath);
        }
    }


    @SuppressLint("CheckResult")
    private void getData() {
        compositeDisposable.add(Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                initData();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {

                    }
                }));
    }

    private void reNameFile() {
        EditText reNameEdiText = mDialogTool.getEdiText();
        if (reNameEdiText == null) {
            return;
        }
        String name = reNameEdiText.getText().toString();
        reNameEdiText.setText("");
        if (name.length() <= 0) {
            ToastTool.showToast(R.string.name_cannot_empty);
            return;
        }
        Log.d(TAG, "reNameFile: " + mLongClickPath + " " + name);
        boolean success = FileOperationTool.reNameFile(mLongClickPath, name);
        if (success) {
            if (mCurrentItem == CustomValue.PATH_TYPE_LOCAL) {
                updataAdapter(mcurrentLocalPath);
            } else {
                updataAdapter(mcurrentSDPath);
            }
            dialogDismiss(mDialogTool.getReNameDialog());
            ToastTool.showToast(R.string.rename_success);
        } else {
            if (FileOperationTool.getFailureCode() == 1) {
                ToastTool.showToast(R.string.name_already_exists);
            } else {
                dialogDismiss(mDialogTool.getReNameDialog());
                ToastTool.showToast(R.string.rename_failure);
            }
        }
    }

    @SuppressLint("CheckResult")
    private void doDeleteFile() {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                if (mLongClickPath.equals(CustomValue.LOCAL_PATH)
                        || mLongClickPath.equals(CustomValue.SD_CARD_PATH)) {
                    emitter.onNext(false);
                } else {
                    File file = new File(mLongClickPath);
                    FileOperationTool.deleteAllFile(file);
                    emitter.onNext(true);
                }
            }
        }).subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            if (mCurrentItem == CustomValue.PATH_TYPE_LOCAL) {
                                updataAdapter(mcurrentLocalPath);
                            } else {
                                updataAdapter(mcurrentSDPath);
                            }
                            ToastTool.showToast(R.string.delete_success);
                        } else {
                            ToastTool.showToast(R.string.cannot_delete_file_to_here);
                        }
                        dialogDismiss(mDialogTool.getWaitingDialog());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void copyFile() {
        operationType = CustomValue.OPERATION_TYPE_COPY;
        copyFilePath = mLongClickPath;
    }

    private void cutFile() {
        cutIndex = mCurrentItem;
        operationType = CustomValue.OPERATION_TYPE_CUT;
        copyFilePath = mLongClickPath;
    }

    private void dialogDismiss(Dialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @SuppressLint("CheckResult")
    private void doPasteFile() {
        compositeDisposable.add(Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                pasteFile(emitter);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) {
                        if (aBoolean) {
                            if (copyOrCut) {// copy
                                updataAdapter(targetPath);
                            } else {// cut
                                if (cutIndex == pasteIndex) {
                                    updataAdapter(targetPath);
                                } else {
                                    //更新当前页面
                                    updataAdapter(targetPath);
                                    //更新被剪贴的页面
                                    updataAdapter(mCopyPath, cutIndex);
                                }
                            }
                            ToastTool.showToast(R.string.paste_success);
                        } else {
                            if (isSameDirectory) {
                                isSameDirectory = false;
                                ToastTool.showToast(R.string.same_directory);
                            } else {
                                ToastTool.showToast(R.string.paste_failure);
                            }
                        }
                        dialogDismiss(mDialogTool.getWaitingDialog());
                    }
                }));
    }

    /**
     * mCurrentItem viewPager的当前页数
     * copyFilePath 复制文件的绝对路径
     * targetPath 将要粘贴的路径
     * newFilePath 将要粘贴的文件的绝对路径
     */
    private void pasteFile(ObservableEmitter<Boolean> emitter) {
        File file = new File(copyFilePath);
        String fileName = file.getName();
        boolean success = false;
        if (mCurrentItem == CustomValue.PATH_TYPE_LOCAL) {
            targetPath = mcurrentLocalPath;
        } else {
            targetPath = mcurrentSDPath;
        }
        Log.d(TAG,
                "pasteFile:current copyFilePath " + copyFilePath + "\n targetPath " + targetPath);
        if (operationType == CustomValue.OPERATION_TYPE_CUT) {//cut file
            String copyFiledDirectoryPath = copyFilePath.substring(0, copyFilePath.lastIndexOf(
                    "/"));
            if (copyFiledDirectoryPath.equals(targetPath)) {
                copyFilePath = null;
                isSameDirectory = true;
                emitter.onNext(false);
                return;
            }
            String fileNewPath = getFileNewPath(targetPath, fileName);
            Log.d(TAG, "pasteFile:current " + copyFilePath + " \n " + fileNewPath);
            success = FileOperationTool.copyAll(copyFilePath, fileNewPath);
            Log.d(TAG, "pasteFile:current success " + success);
            if (success) {
                File f = new File(copyFilePath);
                FileOperationTool.deleteAllFile(f);
            }
            String copyPath = copyFilePath.substring(0, copyFilePath.lastIndexOf("/"));
            if (copyPath.equals(CustomValue.LOCAL_NAME) || copyPath.equals(CustomValue.SD_CARD_NAME)) {
                copyPath = copyFilePath;
            }
            mCopyPath = copyPath;
            copyOrCut = false;
            copyFilePath = null;
            emitter.onNext(success);
        } else {//copy file
            if (targetPath.equals(CustomValue.SD_CARD_NAME)
                    || targetPath.equals(CustomValue.LOCAL_NAME)) {
                copyFilePath = null;
                ToastTool.showToast(R.string.cannot_copy_file_to_here);
                emitter.onNext(false);
                return;
            }
            String newFilePath = getFileNewPath(targetPath, fileName);
            Log.d(TAG, "pasteFile:current newFilePath  " + newFilePath);
            success = FileOperationTool.copyAll(copyFilePath, newFilePath);
            copyOrCut = true;
            copyFilePath = null;
            emitter.onNext(success);
        }
    }

    /**
     * 粘贴文件命名
     *
     * @param targetPath  目标目录
     * @param oldFileName 文件名
     * @return
     */
    private String getFileNewPath(String targetPath, String oldFileName) {
        String newFilePath = targetPath + "/" + oldFileName;
        if (new File(newFilePath).exists()) {
            newFilePath = targetPath + "/" + "复制_" + oldFileName;
            Log.d(TAG, "getFileNewPath:targetPath " + targetPath + " oldFileName "
                    + oldFileName + " new " + newFilePath);
            if (new File(newFilePath).exists()) {
                int i = 1;
                while (new File(newFilePath).exists()) {
                    i++;
                    newFilePath = targetPath + "/" + "复制_" + i + "_" + oldFileName;
                }
            }
        }
        return newFilePath;
    }

    private void dialogDissMiss() {
        dialogDismiss(mDialogTool.getLongClickDialog());
        dialogDismiss(mDialogTool.getReNameDialog());
        dialogDismiss(mDialogTool.getDeleteDiaglog());
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            ToastTool.showToast(R.string.exit_app);
            mHandler.sendEmptyMessageDelayed(CustomValue.HANDLE_EXIT_APP, 2000);
        } else {
//            finish();
//            finishAffinity();
            System.exit(0);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onStop() {
        super.onStop();
        dialogDissMiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
        }
        dialogDismiss(mDialogTool.getWaitingDialog());
    }

}
