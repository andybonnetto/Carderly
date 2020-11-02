## Card recognition python programm
In Windows, you will need to connect via ssh using Xming also (see X11 in puTTy) or launch the following command from a gitbash in order to open the camera stream window (doesn't really work sometimes, puTTy is better)
```
ssh -X pi@<ip_address>
```
The basic Card recognition adapted for USB camera can be launch by typing the code below in the terminal after the usb camera is plugged
```
export DISPLAY=":0"
python Opencv-Playing-Card-Detector/CardDetector.py
```
## Snapshot test program
In /Vision_Card_Detector, you can launch *Snapshots.py* to take 10 pictures separated by 10 seconds. They will be saved in the /Snapshots directory, you can visualize them with Xming by typing in the terminal:
```
xpeg-open snapshot<whateverthenumber>.png
```
## Magic Commands for the Cameras
```
fswebcam <image_name.jpg>
```
Take snapshot names image_name.jpg in the folder in which you are running the command
```
sudo service motion start
sudo service motion stop
sudo service motion restart
```
Load the camera stream on the port *8081* of the card's IP address, can be visualized using a browser (commands are sent on port *8080*, can be changed in configs)
```
sudo nano  /etc/motion/motion.conf
```
Modify the configurations of motion
```
workon cv3
```
A virtual environment has been created for the python packaging related to opencv, *workon* connects to the environnement (*deactivate* to quit any environnement)

