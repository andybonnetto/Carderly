# Carderly Display

Carderly Display is using the Kivy package installed for Raspberry Pi. Very Handful tutorials gives the basics of Kivy functionalities at https://www.youtube.com/playlist?list=PLzMcBGfZo4-kSJVMyYeOQ8CXJ3z1k7gHn 
The example display from the last video is stored in the *Kivy_GUI_Example* depositery

## TODO Display
* <b> Show card to validate </b></br>
Show the card seen in database already preprocessed which needs to be accepted by button in order to send this value to the App.
* <b> Make commands for buttons </b>(Andy)</br>
Kivy buttons works when you click on it but needs to work when color button is pressed
* <b> Add "Insert Deck" Window </b></br>
Necessary window in between Main page and Waiting room, this window needs to show the Label "Insert Deck" and popup problems with the shuffling, if possible automatically change to waiting room when shuffling is done.
* <b> Choose the room name to join </b> </br>
Diplay need to read the names in the person's waiting and for that needs a name.
* <b> Autoconnect of the elderliy's account </b> </br>

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
