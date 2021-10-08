package com.company;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;

import jxgrabkey.HotkeyConflictException;
import jxgrabkey.HotkeyListener;
import jxgrabkey.JXGrabKey;

import javax.sound.sampled.*;


public class Main {

    private static Robot robot;
    private static boolean isConnected = true;
    private static String line;

    public static void main(String[] args) {
        try {
            robot = new Robot();
            ServerSocket serverSocket = new ServerSocket(6689);
            while (isConnected) {
                Socket socket = serverSocket.accept();
                InputStreamReader inputStream = new InputStreamReader(socket.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStream);

                String message = bufferedReader.readLine();
                robotMovements(message);
            }
        } catch (NullPointerException | AWTException | IOException | InterruptedException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.out.println(e + " ERROR IN SERVER");
        }

    }

    public static void robotMovements(String message) throws InterruptedException, LineUnavailableException, IOException, UnsupportedAudioFileException {
        if (message != null) {
            ProcessBuilder processBuilder = new ProcessBuilder();
            //System.out.println(message);

            if (message.equalsIgnoreCase("play")) {     // Presses the SPACE bar
                robot.keyPress(KeyEvent.VK_SPACE);
                robot.keyRelease(KeyEvent.VK_SPACE);
            }
            // ---------------------------SCROLL UP AND DOWN
            else if (message.equalsIgnoreCase("up")) {          // Scrolls up
                robot.mouseWheel(-1);
            } else if (message.equalsIgnoreCase("down")) {           // Scrolls down
                robot.mouseWheel(1);
            }
            //  ---------------------------SOUND
             else if (message.equalsIgnoreCase("Mute")) {            // Toggle mute/unmute
                processBuilder.command("/bin/bash", "-c", "amixer -D pulse set Master 1+ toggle");
                processBuilder.start();
            } else if (message.contains("progressChange:")) {
                try {
                    String[] changeNumber = message.split(":");
                    int volume = Integer.parseInt(changeNumber[1]) / 2;
                    System.out.println("amixer set Master " + volume + "%");
                    processBuilder.command("/bin/bash", "-c", "amixer set Master " + volume + "%");
                    processBuilder.start();
                } catch (Exception ignored) {}
            }
            // ---------------------------SCREENS
            // WatchTV - Turns off my screens and mirrors one of them to my tv.
            else if (message.equalsIgnoreCase("watchTV_true")) {
                processBuilder.command("/bin/bash", "-c", "xrandr --output DisplayPort-2 --auto\nxrandr --output DisplayPort-2 --same-as HDMI-A-0");
                processBuilder.start();
            }else if (message.equalsIgnoreCase("watchTV_false")) {
                processBuilder.command("/bin/bash", "-c", "xrandr --output DisplayPort-2 --off");
                processBuilder.start();
            }
            // SCREEN 1
            else if (message.equalsIgnoreCase("screen1On")) {
                processBuilder.command("/bin/bash", "-c", "xrandr --output DisplayPort-0 --brightness 1");
                processBuilder.start();
            } else if (message.equalsIgnoreCase("screen1Off")) {
                processBuilder.command("/bin/bash", "-c", "xrandr --output DisplayPort-0 --brightness 0");
                processBuilder.start();
            }
            // SCREEN 2
            else if (message.equalsIgnoreCase("screen2On")) {
                processBuilder.command("/bin/bash", "-c", "xrandr --output HDMI-A-0 --brightness 1");
                processBuilder.start();
            } else if (message.equalsIgnoreCase("screen2Off")) {
                processBuilder.command("/bin/bash", "-c", "xrandr --output HDMI-A-0 --brightness 0");
                processBuilder.start();
            }
            // SCREEN 3
            else if (message.equalsIgnoreCase("screen3On")) {
                processBuilder.command("/bin/bash", "-c", "xrandr --output DisplayPort-1 --brightness 1");
                processBuilder.start();
            } else if (message.equalsIgnoreCase("screen3Off")) {
                processBuilder.command("/bin/bash", "-c", "xrandr --output DisplayPort-1 --brightness 0");
                processBuilder.start();
            }
            // --------------------Turn off PC / Suspend PC
            else if (message.equalsIgnoreCase("suspendPC")) {
                processBuilder.command("/bin/bash", "-c", "systemctl suspend");
                processBuilder.start();
            }else if (message.equalsIgnoreCase("turnOffPC")) {
                processBuilder.command("/bin/bash", "-c", "Poweroff");
                processBuilder.start();
            }
            // ---------------------------MOUSEPAD/ ARROW MANIPULATION
            else if (message.contains(",")) {
                float moveX = Float.parseFloat(message.split(",")[0]);          //extract movement in x direction
                float moveY = Float.parseFloat(message.split(",")[1]);          //extract movement in y direction
                Point point = MouseInfo.getPointerInfo().getLocation();                 //Get current mouse position
                float nowX = point.x;
                float nowY = point.y;
                robot.mouseMove((int) (nowX + moveX), (int) (nowY + moveY));                //Move mouse pointer to new location
            } else if (message.equals("L-Click")) {
                robot.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);
            } else if (message.equals("R-Click")) {
                robot.mousePress(KeyEvent.BUTTON3_DOWN_MASK);
                robot.mouseRelease(KeyEvent.BUTTON3_DOWN_MASK);
            } else if (message.equals("doubleClick")) {
                robot.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);
                Thread.sleep(250);
                robot.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);
            }
        }
    }


}

