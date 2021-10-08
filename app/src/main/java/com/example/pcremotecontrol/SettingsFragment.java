package com.example.pcremotecontrol;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.marcinmoskala.arcseekbar.ArcSeekBar;
import com.marcinmoskala.arcseekbar.ProgressListener;

public class SettingsFragment extends DialogFragment {

    Button muteSwitch;
    Switch watchTV;
    Button turnOffPC;
    Button suspendPC;
    Switch screen1;
    Switch screen2;
    Switch screen3;
    ArcSeekBar seekBar;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        muteSwitch =  view.findViewById(R.id.muteSwitch);
        muteSwitch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                messageSender sendMessage = new messageSender();
                Log.d("hej", muteSwitch.getText().toString());
                sendMessage.execute((String) muteSwitch.getText());
            }
        });
        screen1 = view.findViewById(R.id.screen1);
        screen1.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            messageSender sendMessage = new messageSender();
            if (isChecked){
                sendMessage.execute("screen1On");
            }
            else  {
                sendMessage.execute("screen1Off");
            }
        });

        screen2 =  view.findViewById(R.id.screen2);
        screen2.setChecked(true); // true is open, false is close.
        screen2.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            messageSender sendMessage = new messageSender();
            if (isChecked){
                sendMessage.execute("screen2On");
            }
            else  {
                sendMessage.execute("screen2Off");
            }
        });
        screen3 =  view.findViewById(R.id.screen3);
        screen3.setChecked(true); // true is open, false is close.
        screen3.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            messageSender sendMessage = new messageSender();
            if (isChecked){
                sendMessage.execute("screen3On");
            }
            else  {
                sendMessage.execute("screen3Off");
            }
        });

        watchTV =  view.findViewById(R.id.watchTV);
        watchTV.setChecked(true); // true is open, false is close.
        watchTV.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            messageSender sendMessage = new messageSender();
            if (isChecked){
                sendMessage.execute("watchTV_true");
            }
            else  {
                sendMessage.execute("watchTV_false");
            }
        });

        turnOffPC =  view.findViewById(R.id.turnOffPC);
        turnOffPC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("TURN OFF PC");
                builder.setMessage("Are you sure you want to turn off PC?");
                builder.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                messageSender sendMessage = new messageSender();
                                sendMessage.execute((String) turnOffPC.getText());
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        suspendPC =  view.findViewById(R.id.suspendPC);
        suspendPC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("SUSPEND PC");
                builder.setMessage("Are you sure you want to suspend PC?");
                builder.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                messageSender sendMessage = new messageSender();
                                sendMessage.execute((String) suspendPC.getText());
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        seekBar = view.findViewById(R.id.seekBar); // initiate the progress bar
        seekBar.setMaxProgress(200); // 200 maximum value for the Seek bar
        seekBar.setProgress(100); // 50 default progress value

        ProgressListener progressListener = progress ->{
            messageSender sendMessage = new messageSender();
            sendMessage.execute("progressChange:" + progress);;
        };
        seekBar.setOnProgressChangedListener(progressListener);

        return view;
    }


}
