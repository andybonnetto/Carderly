# Carderly Display
Carderly Display is using the Kivy package installed for Raspberry Pi. Very Handful tutorials gives the basics of Kivy functionalities at https://www.youtube.com/playlist?list=PLzMcBGfZo4-kSJVMyYeOQ8CXJ3z1k7gHn 
The example display from the last video is stored in the *Kivy_GUI_Example* depositery

## TODO Display
* ~<b> Show card to validate </b></br>
Show the card seen in database already preprocessed which needs to be accepted by button in order to send this value to the App.~
Solution: passive field in the database created called Vision where the values of the cards in numbers show up, conversion from number to card value is done in main.py
* ~<b> Make commands for buttons </b>(Andy)</br>
Kivy buttons works when you click on it but needs to work when color button is pressed~ </br>
Solution: now buttons can be used to change the screen, most of the buttons commands are in the *Main_Window* screen some other are in the *Game* screen, I still have no idea why it works that way but well..it works
* ~<b> Add "Insert Deck" Window </b></br>
Necessary window in between Main page and Waiting room, this window needs to show the Label "Insert Deck" and popup problems with the shuffling, if possible automatically change to waiting room when shuffling is done.~ </br>
Solution : The window is created and show dynamic label of waiting, linked with database condition to show text and swipe to waiting room.
* <b> Choose the room name to join </b> </br>
Diplay need to read the names in the person's waiting and for that needs a name.
* <b> Autoconnect of the elderliy's account </b> </br>
* <b> Block go back in waiting_room and insert deck </b> </br>
* ~<b> Automatic go to game feature </b>~
* ~<b> Show l'atout on game window </b>~
* ~<b> Button commands to choose trump </b>~
* ~<b> Button commands to validate the card seen by the vision during olderly's turn </b>~
* ~<b> Automatic go to waiting room feature~

![display](https://github.com/andybonnetto/Carderly/blob/main/Carderly_Display/Display.PNG?raw=false) 

## Making on Windows
  After installing Kivy with conda, you can mess up with the two important files for the kivy display which are *main.py* and *my.kv* (some kv file, looks like OpenGL btw)
Kind of like HTML/CSS the kv file is responsible for all the layout part and the python handles the functions. In order to use Kivy you will need to install the kivy packaging by using
```
pip install kivy
```
in your python environment or add the package in the python interpreter of PyCharm to do small tests. This will give the possiblity to use sub kivy packaging such as the Buttons the Labels or even the ScreenManager.
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
Trump choice done in the game window with buttons, <br/>
Show card seen by the vision (print the database state) <br/>
