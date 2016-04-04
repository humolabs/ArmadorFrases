package com.humolabs.debut;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private boolean flagSurprise;

    Spinner colSpinner1;
    Spinner colSpinner2;
    Spinner colSpinner4;

    String strResultado = null;

    ArrayAdapter<CharSequence> colAdapter1;
    ArrayAdapter<CharSequence> colAdapter2;
    ArrayAdapter<CharSequence> colAdapter4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        colSpinner1 = (Spinner) findViewById(R.id.columna1);
        colSpinner2 = (Spinner) findViewById(R.id.columna2);
        colSpinner4 = (Spinner) findViewById(R.id.columna4);

        colAdapter1 = ArrayAdapter.createFromResource(this, R.array.columna1, R.layout.customspinner);
        colAdapter2 = ArrayAdapter.createFromResource(this, R.array.columna2, R.layout.customspinner);
        colAdapter4 = ArrayAdapter.createFromResource(this, R.array.columna4, R.layout.customspinner);

        // Inicializo los combos
        colAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (colSpinner1 != null) colSpinner1.setAdapter(colAdapter1);
        if (colSpinner2 != null) colSpinner2.setAdapter(colAdapter2);
        if (colSpinner4 != null) colSpinner4.setAdapter(colAdapter4);

        Button btnGeneraVerso = (Button) findViewById(R.id.btnGeneraVerso);
        if (btnGeneraVerso != null) btnGeneraVerso.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                flagSurprise=false;
                dameLaMagia(flagSurprise);
            }
        });

        Button btnSorpresa = (Button) findViewById(R.id.btnSorpresa);
        if (btnSorpresa != null) btnSorpresa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                flagSurprise=true;
                dameLaMagia(flagSurprise);
            }
        });
    }

    private void dameLaMagia(Boolean flagSurprise) {
        final Context context = MainActivity.this;
        Random random = new Random();

        if(!flagSurprise) {
            final TextView txt = (TextView) findViewById(R.id.columna3);

            if (colSpinner2 != null) {
                strResultado = String.valueOf(colSpinner1 != null ? colSpinner1.getSelectedItem().toString() : null) + " " +
                        String.valueOf(colSpinner2.getSelectedItem().toString()) + " " +
                        (txt != null ? txt.getText() : null) + " " +
                        String.valueOf(colSpinner4 != null ? colSpinner4.getSelectedItem().toString() : null);
            }
        }else{
            String[] dataCol1 = getResources().getStringArray(R.array.columna1);
            String randomCol1 = dataCol1[random.nextInt(dataCol1.length)];

            String[] dataCol2 = getResources().getStringArray(R.array.columna2);
            String randomCol2 = dataCol2[random.nextInt(dataCol2.length)];

            String[] dataCol4 = getResources().getStringArray(R.array.columna4);
            String randomCol4 = dataCol4[random.nextInt(dataCol4.length)];

            strResultado = randomCol1 +" "+ randomCol2 +" porque "+ randomCol4;
        }
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.resultado);
        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText(strResultado);

        Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button dialogButtonShare = (Button) dialog.findViewById(R.id.dialogButtonShare);
        dialogButtonShare.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Whatsapp", "Gmail", "Mensaje de Texto"};

                Builder builder = new Builder(context);
                builder.setTitle("Elegì una opción");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                        Intent sendIntent = new Intent();

                        if(items[item]=="Whatsapp"){
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, strResultado);
                            sendIntent.setPackage("com.whatsapp");
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);
                        }else if (items[item]=="Gmail"){
                            Intent send = new Intent(Intent.ACTION_SENDTO);
                            String uriText = "mailto:" + Uri.encode("email@gmail.com") +
                                    "?subject=" + Uri.encode("Ausencia") +
                                    "&body=" + Uri.encode(strResultado);
                            Uri uri = Uri.parse(uriText);

                            send.setData(uri);
                            startActivity(Intent.createChooser(send, "Send mail..."));
                        }else{
                            Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                            smsIntent.setType("vnd.android-dir/mms-sms");
                            smsIntent.putExtra("address","busque su contacto...");
                            smsIntent.putExtra("sms_body",strResultado);
                            startActivity(smsIntent);
                        }

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        dialog.show();
    }
}

