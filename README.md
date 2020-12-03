# Carderly

## TODO general
* <b> make the launch file </b> </br>
Launch file will call *call_motor.py*, *main.py* in Kivy_display, *Activate_vision.py* and do some sub fonctions which can be for example written in *subfunction.py* file in here.
* ~<b> Display + SSH </b> (Andy) </br>
For some reason the display turns off, maybe it is Ximing~ </br>
Solution, use  *KIVY_BCM_DISPMANX_ID=0 python main.py* and works even better for the display

## Connecting to raspberry pi on windows
* Connect the raspi by changing the *wpa_supplicant.conf* wifi parameters in boot of the sd card
* Install putty and advanced IP scanner on your computer
* Once sd card plugged and raspi power supplied, search for IP adress using IP scanner (you should be connected to the same wifi)
* Put the IP adress on putty should open a terminal in the card
* If you want a nice display you can use windows remote desktop and put the same IP adress
It can work by ethernet but I never tried

## Magic Command for the raspberry pi
```
$ sudo raspi-config
```
Goes to the config menu of the card.
```
$ ifconfig
```
Shows IP adresses and connectivity
```
$ sudo apt-get <module name>
```
Install some module in general but always look at the documentation first
```
du -h
df -h
free -h
```
Three different commands to check used memory, free memory and RAM (-h means human readible format of numbers)
### Linux commands
```
$ mkdir      create directory
$ cd         goes to directory
$ cd ..      goes to mother directory
$ ls         shows list of stuff in directory
$ rm         remove a file
$ rm -r      remove a directory
$ sudo       for securised access
$ nano       text editor
$ vim        text editor
```

## Git commands
git simulator : https://learngitbranching.js.org/?NODEMO=&locale=fr_FR
```
$ git init                    create a git depositery(.git)
$ git clone <link_to_github>  clone repositery and create remote
$ git pull                    fetch changes from server
$ git status                  shows current status
$ git add <file_name>         add file to next commit (put a . for all files)
$ git commit -m "message"     create the commit
$ git push origin <branch_name>     send the commit
$ git log                     list the history of commits
$ git branch <branch_name>    create new branch
$ git checkout <branch_name>  move to branch
$ git rebase <commit_bash>    change the current position in the history
$ git merge <branch_name>     merge branch_name to current branch
```
