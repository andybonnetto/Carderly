# Carderly Display
Carderly Display is using the Kivy package installed for Raspberry Pi. Very Handful tutorials gives the basics of Kivy functionalities at https://www.youtube.com/playlist?list=PLzMcBGfZo4-kSJVMyYeOQ8CXJ3z1k7gHn 
The example display from the last video is stored in the *Kivy_GUI_Example* depositery
## Making on Windows
  After installing Kivy with conda, you can mess up with the two important files for the kivy display which are *main.py* and *my.kv* (some kv file, looks like OpenGL btw)
Kind of like HTML/CSS the kv file is responsible for all the layout part and the python handles the functions
## Test on Raspberry
  to launch the kivy programm on raspberry using putty on SSH, you will need to specify the display (default value LCD) in order for Ximing to recognize it type the following in a terminal (change to 0 for LCD display)
```
KIVY_BCM_DISPMANX_ID=1 python main.py
```
## For now
Basic functional layout displays, <br/>
Can pop up windows, <br/>
Buttons to change screens, <br/>
Reading from firebase database contact name for waiting room and game, <br/>
Turn selecition using firebase database next turn variable, <br/>
Choice de l'atout done in the game window, <br/>


## Next step
Configure settings <br/>
