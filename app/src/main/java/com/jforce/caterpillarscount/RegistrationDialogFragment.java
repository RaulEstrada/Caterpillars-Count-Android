package com.jforce.caterpillarscount;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.jforce.caterpillarscount.R;

/**
 * Created by justinforsyth on 10/1/14.
 */
public class RegistrationDialogFragment extends DialogFragment {




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle extras = getArguments();
        String email = extras.getString("email");

        String line1 = getString(R.string.registration_dialog1) + " " + email + "\n\n";
        String line2 = getString(R.string.registration_dialog2);
        String line3 = getString(R.string.registration_dialog3);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(line1 + line2 + line3)
                .setTitle("Confirmation Email")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
