package com.cywl.launcher.filemanager.model.tools;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.cywl.launcher.filemanager.R;
import com.cywl.launcher.filemanager.model.listener.OnDialogListener;

/**
 * @author Altair
 * @date :2019.11.21 下午 03:38
 * @description:
 */
public class FileManagementDialogTool {
    private Context mContext;
    private AlertDialog longClickDialog;
    private ProgressDialog waitingDialog;
    private AlertDialog deleteDiaglog = null;
    private AlertDialog reNameDialog = null;
    private OnDialogListener mListener;
    private EditText etName;


    public FileManagementDialogTool(Context context, OnDialogListener listener) {

        this.mContext = context;
        this.mListener = listener;
    }

    private void sendToAativity(int type) {
        if (mListener != null) {
            mListener.doSomeThing(type);
        }
    }

    public void showLongClickDialog() {
        if (longClickDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(R.string.operation_file)
                    .setItems(R.array.file_operation_list, (dialog, which) -> {
                        switch (which) {
                            case 0:
                                showDelete();
                                break;
                            case 1:
                                showReNameDialog();
                                break;
                            case 2:
                                sendToAativity(1);
                                break;
                            default:
                                sendToAativity(2);
                                break;
                        }
                    });
            longClickDialog = builder.create();
            longClickDialog.show();
            Window dialogWindow = longClickDialog.getWindow();
            assert dialogWindow != null;
            WindowManager.LayoutParams p = dialogWindow.getAttributes();
            p.width=200;
            dialogWindow.setAttributes(p);
        }
        if (!longClickDialog.isShowing()) {
            longClickDialog.show();
        }

    }

    public void showWaitingDialog(int message) {
        if (waitingDialog == null) {
            waitingDialog = new ProgressDialog(mContext);
            waitingDialog.setMessage(mContext.getText(message));
            waitingDialog.setIndeterminate(true);
            waitingDialog.setCancelable(false);//点击返回键或者dialog四周是否关闭dialog  true表示可以关闭 false表示不可关闭
            WindowManager.LayoutParams params =
                    waitingDialog.getWindow().getAttributes();
            params.width = 500;
            waitingDialog.getWindow().setAttributes(params);
            waitingDialog.setOnKeyListener((dialog, keyCode, event) -> false);
        }
        waitingDialog.setMessage(mContext.getText(message));
        waitingDialog.show();
    }

    private void showDelete() {
        if (deleteDiaglog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(R.string.delete)
                    .setMessage(R.string.really_delete)
                    .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                        deleteDiaglog.dismiss();
                        showWaitingDialog(R.string.perform_operation);
                        sendToAativity(3);
                    })
                    .setNegativeButton(android.R.string.cancel, null);
            deleteDiaglog = builder.create();
        }
        deleteDiaglog.show();
    }

    private void showReNameDialog() {
        if (reNameDialog == null) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_edittext, null);
            etName = view.findViewById(R.id.ed_dialog);
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(R.string.input_name)
                    .setView(view)
                    .setPositiveButton(android.R.string.ok, null)
                    .setNegativeButton(android.R.string.cancel, null);
            reNameDialog = builder.create();
            reNameDialog.setCanceledOnTouchOutside(false);
            reNameDialog.show();
            reNameDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendToAativity(4);
                }
            });
        }
        if (!reNameDialog.isShowing()) {
            reNameDialog.show();
        }
    }

    public EditText getEdiText() {
        return etName;
    }

    public AlertDialog getLongClickDialog() {
        return longClickDialog;
    }

    public ProgressDialog getWaitingDialog() {
        return waitingDialog;
    }

    public AlertDialog getDeleteDiaglog() {
        return deleteDiaglog;
    }

    public AlertDialog getReNameDialog() {
        return reNameDialog;
    }
}
