package com.octopus.android.car.apps.radio;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.octopus.android.car.apps.R;


/**
 * PTY 弹框选项
 */
public class CustomDialogPTY {

    public static Dialog CustomDialogPTY(Context context, String[] items, DialogInterface.OnClickListener onClickListener) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog_pty);
        dialog.setTitle("Custom Dialog");

        ListView listView = dialog.findViewById(R.id.dialog_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        Button cancelButton = dialog.findViewById(R.id.dialog_cancel);
        Button okButton = dialog.findViewById(R.id.dialog_ok);
        cancelButton.setOnClickListener(view -> dialog.dismiss());
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }
}
