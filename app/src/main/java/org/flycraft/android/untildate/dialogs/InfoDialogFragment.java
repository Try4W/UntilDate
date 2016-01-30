package org.flycraft.android.untildate.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.flycraft.android.untildate.R;

public class InfoDialogFragment extends DialogFragment {

    private TextView vkLink;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_info, null);
        setupVkLink(view);

        builder.setView(view)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss(); // close dialog
                    }
                });
        return builder.create();
    }

    private void setupVkLink(View view) {
        vkLink = (TextView) view.findViewById(R.id.vk_link);
        vkLink.setText(
                Html.fromHtml("<a href=\"http://vk.com/try4w\">vk.com/try4w</a>")
        );
        vkLink.setMovementMethod(LinkMovementMethod.getInstance());
    }

}