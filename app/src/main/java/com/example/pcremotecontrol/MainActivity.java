package com.example.pcremotecontrol;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.pcremotecontrol.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener{

    EditText text;
    TextView mousePad;
    Button playButton;
    Button upButton;
    Button downButton;
    Button settingsButton;
    //Button raiseVolume;
    //Button lowerVolume;
    Button leftMouseClick;
    Button rightMouseClick;


    private final boolean isConnected = false;
    private boolean mouseMoved=false;
    private PrintWriter out;
    private float initX = 0;
    private float initY = 0;
    private float disX = 0;
    private float disY = 0;

    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button instances
        mousePad = (TextView) findViewById(R.id.mousePad);
        text = (EditText) findViewById(R.id.messageTextField);
        playButton = (Button) findViewById(R.id.playButton);
        upButton = (Button) findViewById(R.id.upButton);
        downButton = (Button) findViewById(R.id.downButton);
        settingsButton = (Button) findViewById(R.id.settingsButton);
        //raiseVolume = (Button) findViewById(R.id.raiseVolume);
        //lowerVolume = (Button) findViewById(R.id.lowerVolume);
        leftMouseClick = (Button) findViewById(R.id.leftMouseClick);
        rightMouseClick = (Button) findViewById(R.id.rightMouseClick);

        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetectorCompat(this,this);
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);

        //Opens dialogFragment
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                SettingsFragment settings = new SettingsFragment();
                settings.show(getSupportFragmentManager(), "settingsFragment");
            }
        });


        // Mousepad controls
        mousePad.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                onTouchEvent(event);
                messageSender sendMessage = new messageSender();
                try {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //save X and Y positions when user touches the TextView
                            initX = event.getX();
                            initY = event.getY();
                            mouseMoved = false;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            disX = event.getX() - initX;
                            disY = event.getY() - initY;
                            initX = event.getX();
                            initY = event.getY();
                            if (disX != 0 || disY != 0) {
                                sendMessage.execute(disX + "," + disY);
                            }
                            mouseMoved=true;
                            break;
                        case MotionEvent.ACTION_UP:
                            //consider a tap only if usr did not move mouse after ACTION_DOWN
                            if(!mouseMoved){
                                out.println("left_click");
                            }
                    }
                } catch (Exception e) {
                    System.out.println(e + " ERROR sendMouse");
                }
                return true;
            }
        });
    }

    // triggers when sendButton is clicked
    public void sendButton(View v) {
        Log.d("text", text.getText().toString());
        messageSender sendMessage = new messageSender();
        sendMessage.execute(text.getText().toString());
    }
    public void sendPlay(View v) {
        messageSender sendMessage = new messageSender();
        sendMessage.execute(playButton.getText().toString().toLowerCase(Locale.ROOT));
    }
    public void sendUp(View v){
        messageSender sendMessage = new messageSender();
        sendMessage.execute(upButton.getText().toString().toLowerCase(Locale.ROOT));
    }
    public void sendDown(View v){
        messageSender sendMessage = new messageSender();
        sendMessage.execute(downButton.getText().toString().toLowerCase(Locale.ROOT));
    }

    public void sendLeftMouseClick(View v){
        messageSender sendMessage = new messageSender();
        sendMessage.execute(leftMouseClick.getText().toString());
    }
    public void sendRightMouseClick(View v){
        messageSender sendMessage = new messageSender();
        sendMessage.execute(rightMouseClick.getText().toString());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (this.mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }
    // Copied straight from https://developer.android.com/training/gestures/detector
    @Override
    public boolean onDown(MotionEvent event) {
        Log.d(DEBUG_TAG,"onDown: " + event.toString());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
                            float distanceY) {
        Log.d("scroll", "onScroll: " + event1.toString() + event2.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        Log.d("doubleClick", "onDoubleTapEvent: " + event.toString());
        messageSender sendMessage = new messageSender();
        sendMessage.execute("doubleClick");
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        messageSender sendMessage = new messageSender();
        sendMessage.execute(leftMouseClick.getText().toString());
        return true;
    }


}


