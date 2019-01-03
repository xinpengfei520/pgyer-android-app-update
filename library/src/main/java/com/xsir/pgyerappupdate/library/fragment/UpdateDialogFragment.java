package com.xsir.pgyerappupdate.library.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.util.Objects;

/**
 * Created by x-sir on 2019/1/3 :)
 * Function:
 */
public class UpdateDialogFragment extends DialogFragment {

    private final static String DIALOG_TITLE = "dialog_title";
    private final static String DIALOG_MESSAGE = "dialog_message";
    private OnButtonClickListener mOnButtonClickListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = "";
        String message = "";
        if (getArguments() != null) {
            title = getArguments().getString(DIALOG_TITLE);
            message = getArguments().getString(DIALOG_MESSAGE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle(title).setMessage(message).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mOnButtonClickListener != null) {
                    mOnButtonClickListener.onOkBtnClick(dialogInterface);
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mOnButtonClickListener != null) {
                    mOnButtonClickListener.onCancelBtnClick(dialogInterface);
                }
            }
        });

        return builder.create();
    }

    public static UpdateDialogFragment newInstance(String title, String message) {
        UpdateDialogFragment updateDialogFragment = new UpdateDialogFragment();
        Bundle args = new Bundle();
        args.putString(DIALOG_TITLE, title);
        args.putString(DIALOG_MESSAGE, message);
        updateDialogFragment.setArguments(args);
        return updateDialogFragment;
    }

    public interface OnButtonClickListener {
        void onOkBtnClick(DialogInterface dialogInterface);

        void onCancelBtnClick(DialogInterface dialogInterface);
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.mOnButtonClickListener = onButtonClickListener;
    }
}
