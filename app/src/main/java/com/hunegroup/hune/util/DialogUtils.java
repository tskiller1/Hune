package com.hunegroup.hune.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.hunegroup.hune.R;

/**
 * Created by apple on 11/15/17.
 */

public class DialogUtils {
    public interface DialogCallBack {
        void onYesClickListener();

        void onCancelClickListener();
    }

    public static Dialog dialogQuestion(Context context, String text, final DialogCallBack callBack) {
        Dialog dialogQuestion = new Dialog(context);
        if (dialogQuestion.getWindow() != null) {
            dialogQuestion.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialogQuestion.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                dialogQuestion.getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.translution));
            }
        }
        dialogQuestion.setCanceledOnTouchOutside(false);
        dialogQuestion.setContentView(R.layout.dialog_question_tuyendung);
        Button btnOK = (Button) dialogQuestion.findViewById(R.id.btn_ok);
        TextView tvCancel = (TextView) dialogQuestion.findViewById(R.id.tv_cancel);
        TextView tvContent = (TextView) dialogQuestion.findViewById(R.id.tv_content);

        if (!TextUtils.isEmpty(text)) {
            tvContent.setText(text);
        }

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onYesClickListener();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onCancelClickListener();
            }
        });
        dialogQuestion.show();
        return dialogQuestion;
    }

    public static Dialog dialogQuestionTimViec(Context context, String text, final DialogCallBack callBack) {
        Dialog dialogQuestion = new Dialog(context);
        if (dialogQuestion.getWindow() != null) {
            dialogQuestion.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialogQuestion.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                dialogQuestion.getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.translution));
            }
        }
        dialogQuestion.setCanceledOnTouchOutside(false);
        dialogQuestion.setContentView(R.layout.dialog_question_timviec);
        Button btnOK = (Button) dialogQuestion.findViewById(R.id.btn_ok);
        TextView tvCancel = (TextView) dialogQuestion.findViewById(R.id.tv_cancel);
        TextView tvContent = (TextView) dialogQuestion.findViewById(R.id.tv_content);

        if (!TextUtils.isEmpty(text)) {
            tvContent.setText(text);
        }

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onYesClickListener();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onCancelClickListener();
            }
        });
        dialogQuestion.show();
        return dialogQuestion;
    }

    public static Dialog dialogSuccess(Context context, String text) {
        final Dialog dialogMessage = new Dialog(context);
        if (dialogMessage.getWindow() != null) {
            dialogMessage.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialogMessage.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                dialogMessage.getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.translution));
            }
        }
        dialogMessage.setCanceledOnTouchOutside(false);
        dialogMessage.setContentView(R.layout.dialog_message_tuyendung);
        Button btnOK = (Button) dialogMessage.findViewById(R.id.btn_ok);
        TextView tvContent = (TextView) dialogMessage.findViewById(R.id.tv_content);

        if (!TextUtils.isEmpty(text)) {
            tvContent.setText(text);
        }
        dialogMessage.setCanceledOnTouchOutside(false);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMessage.dismiss();
            }
        });
        dialogMessage.show();
        return dialogMessage;
    }

    public static Dialog dialogSuccess(Context context, String text, final DialogCallBack callBack) {
        final Dialog dialogMessage = new Dialog(context);
        if (dialogMessage.getWindow() != null) {
            dialogMessage.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialogMessage.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                dialogMessage.getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.translution));
            }
        }
        dialogMessage.setCanceledOnTouchOutside(false);
        dialogMessage.setContentView(R.layout.dialog_message_tuyendung);
        Button btnOK = (Button) dialogMessage.findViewById(R.id.btn_ok);
        TextView tvContent = (TextView) dialogMessage.findViewById(R.id.tv_content);

        if (!TextUtils.isEmpty(text)) {
            tvContent.setText(text);
        }

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onYesClickListener();
            }
        });
        dialogMessage.show();
        return dialogMessage;
    }

    public static Dialog dialogSuccessTimViec(Context context, String text) {
        final Dialog dialogMessage = new Dialog(context);
        if (dialogMessage.getWindow() != null) {
            dialogMessage.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialogMessage.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                dialogMessage.getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.translution));
            }
        }
        dialogMessage.setCanceledOnTouchOutside(false);
        dialogMessage.setContentView(R.layout.dialog_message_timviec);
        Button btnOK = (Button) dialogMessage.findViewById(R.id.btn_ok);
        TextView tvContent = (TextView) dialogMessage.findViewById(R.id.tv_content);

        if (!TextUtils.isEmpty(text)) {
            tvContent.setText(text);
        }

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMessage.dismiss();
            }
        });
        dialogMessage.show();
        return dialogMessage;
    }

    public static Dialog dialogSuccessTimViec(Context context, String text, final DialogCallBack callBack) {
        final Dialog dialogMessage = new Dialog(context);
        if (dialogMessage.getWindow() != null) {
            dialogMessage.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialogMessage.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                dialogMessage.getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.translution));
            }
        }
        dialogMessage.setCanceledOnTouchOutside(false);
        dialogMessage.setContentView(R.layout.dialog_message_timviec);
        Button btnOK = (Button) dialogMessage.findViewById(R.id.btn_ok);
        TextView tvContent = (TextView) dialogMessage.findViewById(R.id.tv_content);

        if (!TextUtils.isEmpty(text)) {
            tvContent.setText(text);
        }

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onYesClickListener();
            }
        });
        dialogMessage.show();
        return dialogMessage;
    }

    public static Dialog dialogError(Context context, String text) {
        final Dialog dialogMessage = new Dialog(context);
        if (dialogMessage.getWindow() != null) {
            dialogMessage.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialogMessage.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                dialogMessage.getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.translution));
            }
        }
        dialogMessage.setCanceledOnTouchOutside(false);
        dialogMessage.setContentView(R.layout.dialog_error_tuyendung);
        Button btnOK = (Button) dialogMessage.findViewById(R.id.btn_ok);
        TextView tvContent = (TextView) dialogMessage.findViewById(R.id.tv_content);

        if (!TextUtils.isEmpty(text)) {
            tvContent.setText(text);
        }
        dialogMessage.setCanceledOnTouchOutside(false);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMessage.dismiss();
            }
        });
        dialogMessage.show();
        return dialogMessage;
    }

    public static Dialog dialogErrorTimViec(Context context, String text) {
        final Dialog dialogMessage = new Dialog(context);
        if (dialogMessage.getWindow() != null) {
            dialogMessage.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialogMessage.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                dialogMessage.getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.translution));
            }
        }
        dialogMessage.setCanceledOnTouchOutside(false);
        dialogMessage.setContentView(R.layout.dialog_error_timviec);
        Button btnOK = (Button) dialogMessage.findViewById(R.id.btn_ok);
        TextView tvContent = (TextView) dialogMessage.findViewById(R.id.tv_content);

        if (!TextUtils.isEmpty(text)) {
            tvContent.setText(text);
        }
        dialogMessage.setCanceledOnTouchOutside(false);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMessage.dismiss();
            }
        });
        dialogMessage.show();
        return dialogMessage;
    }

    public static Dialog dialogProgress(Context context) {
        final Dialog dialogMessage = new Dialog(context);
        if (dialogMessage.getWindow() != null) {
            dialogMessage.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialogMessage.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                dialogMessage.getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.translution));
            }
        }
        dialogMessage.setCanceledOnTouchOutside(false);
        dialogMessage.setContentView(R.layout.dialog_progress);
        return dialogMessage;
    }

}
