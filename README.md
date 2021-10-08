# PC-Remote-Control

So to first understand project better you need to understand my setup.
I have 3 screens and a tv that I only use when laying in bed chilling at night.

For mouse control I use Javas Robot class. 

List of commands:
Play button: Presses the space button using java.Robot.
Mouse control (touch, R-click, L-click): Uses java.robot.
Up button: Presses scroll up using java.Robot
Down button: Presses scroll down using java.Robot

Settings:
  Mute: Uses amixer which is command line mixer for ALSA(Advanced Linux Sound Architecture)
  Screen 1, Screen 2, Screen 3: Uses xrandr which is a configuration utility that lets you manipulate screen settings.
  WatchTV: Xrandr command that turns off my screens, turns on my tv, and mirrors it to my main screen.
  Turn off PC: Uses linux shell command Poweroff that saves and closes all the programs before shutting off the PC.
  Suspend PC: Uses linux shell command to suspend PC.
  Volume control: A seekbar from that adjusts the volume. Design from MarcinMoskala.

