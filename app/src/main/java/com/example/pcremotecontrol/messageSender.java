package com.example.pcremotecontrol;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class messageSender extends AsyncTask<String, Void, Boolean> {
    Socket socket;
    DataOutputStream dataOutput;
    PrintWriter writer;


    @Override
    protected Boolean doInBackground(String... voids){
        String message = voids[0];
        try{
            socket = new Socket("192.168.0.100", 6689);
            writer = new PrintWriter(socket.getOutputStream());

            writer.write(message);
            writer.flush();
            writer.close();
            socket.close();


        }catch(IOException e){
            System.out.println(e + "ERROR IN doInBackground()");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Boolean result){
        //MainActivity activity = new MainActivity();
        //activity.toast();

    }

}
