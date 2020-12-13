# Carderly Vision

## TODO for the VISION
* ~<b>Preprocess_card</b> </br>
Compare all the cards seen to the hand of player 1 in database and send only the estimated card played in the database, that can be done at the end of Activate Vision~
Solution : Preprocess function is done by comparing card values to dataset, only the card estimated is saved and if other card showed save a 0 value.
* ~<b>Condition for while loop</b> (to decide) </br> 
For now the vision is in a *while true* loop, we can either make a function and put the loop outside (in the lauch file) or make it run at all time and put a condition in the database~ </br>
Solution : external condition in the database when all players are here and distribution is finished
* ~<b>Find the setup for changing cameras</b> (Andy) </br>
I don't quite understand in which position the cameras are saved everytime I turn off and on the raspi, this needs to be setted in *Activate_vision.py; cv2.VideoCapture(cam_num)*~ </br>
Solution: You need to plug the USB after the raspberry pi starts to get the picamer to auto-config correctly, *Activate_vision1* starts the pi-camera and *Activate_vision2* starts the vision with the webcam.
* ~<b>Training for ditribution</b> (Andy) </br> 
New training needs to be done for the distribution with one of the cameras, which means they are not gonna be launched with the same tflite model. ~
Solution : Everyrthing works with opencv, need to calibrate the mask position when the camera is fixed

## Object detection with API
A graph had been trained with 32 and converted into "detect.tflite". With the company of the "labelmap.txt" file, it contains the dection model which can be tested by launching the 
```
python TensorFlow-Lite-Object-Detection-on-Android-and-Raspberry-Pi/Activate_vision2.py –-modeldir=../TFlite_models/32 
```
Prints the value of the card seen on the terminal, compare it to the olderly's hand and write in "Vision" the value of the expected card to play

```
python call_vision
```
tutorials: </br>
https://medium.com/@Elenche.Zetetique/object-detection-with-tensorflow-42eda282d915 </br>
https://github.com/EdjeElectronics/TensorFlow-Object-Detection-API-Tutorial-Train-Multiple-Objects-Windows-10 </br>
the dataset was created using : </br>
https://github.com/geaxgx/playing-card-detection/blob/master/creating_playing_cards_dataset.ipynb </br>
and 52 homemade videos of cards. </br>

## Card recognition for the distribution
The distribution vision program works direcly with Opencv and was adapted not to detect the card position anymore (see following section for the program source). By the implementation of a mask, it assume the card to be at its position at all time and only write in the database under "Vision" when he recognize a symbole in the upper right corner.
Some calibration must be done in order for the program to work, when the pi-camera is fixed we need to adjust the box position and size in the mask to where the card is suppose to be in the image. Light needs to be provided, its framerate is higher than the training and it looks more precise.

```
python TensorFlow-Lite-Object-Detection-on-Android-and-Raspberry-Pi/Activate_vision1.py
```

For now a visualization window is opening for the calibration, can be turned into a parameter.
## Card recognition python programm with OpenCV
In Windows, you will need to connect via ssh using Xming also (see X11 in puTTy) or launch the following command from a gitbash in order to open the camera stream window (doesn't really work sometimes, puTTy is better)
```
ssh -X pi@<ip_address>
```
The basic Card recognition adapted for USB camera can be launch by typing the code below in the terminal after the usb camera is plugged
```
export DISPLAY=":0"
python Opencv-Playing-Card-Detector/CardDetector.py
```

tutorial : https://github.com/EdjeElectronics/OpenCV-Playing-Card-Detector
## Snapshot test program
In */Vision_Card_Detector*, you can launch *Snapshot.py* to take 10 pictures separated by 10 seconds. They will be saved in the */Snapshots* directory, you can visualize them with Xming by typing in the terminal:
```
xdg-open snapshot<whateverthenumber>.jpg
```
## Magic Commands for the Cameras
```
fswebcam <image_name.jpg>
```
Take snapshot names image_name.jpg in the folder in which you are running the command
``` 
sudo modprobe bcm2835-v4l2 #make the camera visible
v4l2-ctl --all
```` 
List of all the cameras seen by the raspberry

<b>Note</b>: motion have been removed because it sucks
