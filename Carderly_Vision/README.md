## TODO for the VISION
* Preprocess_card </br>
Compare all the cards seen to the hand of player 1 in database and send only the estimated card played in the database, that can be done at the end of Activate Vision
* Condition for while loop </br> (to decide) 
For now the vision is in a *while true* loop, we can either make a function and put the loop outside (in the lauch file) or make it run at all time
* Find the setup for changing cameras (Andy) </br>
I don't quite understand in which position the cameras are saved everytime I turn off and on the raspi, this needs to be setted in *Activate_vision.py; cv2.VideoCapture(cam_num)*
* Training for ditribution </br> (Andy)
New training needs to be done for the distribution with one of the cameras, which means they are not gonna be launched with the same tflite model.
* Make call_vision() </br>
Read in the database and detect if there is no card for distribution -> discard all cards.

## Object detection with API
A graph had been trained with 32 and converted into "detect.tflite". With the company of the "labelmap.txt" file, it contains the dection model which can be tested by launching the 
```
python TensorFlow-Lite-Object-Detection-on-Android-and-Raspberry-Pi/Activate_vision.py –-modeldir=../TFlite_models/32 
```
for now it prints the value of all the cards seen for each frame.

I can write a program to read the value in the database so we only only care about the vision when we want just by reading in the database at the right time.
```
python call_vision
```
tutorials: </br>
https://medium.com/@Elenche.Zetetique/object-detection-with-tensorflow-42eda282d915 </br>
https://github.com/EdjeElectronics/TensorFlow-Object-Detection-API-Tutorial-Train-Multiple-Objects-Windows-10 </br>
the dataset was created using : </br>
https://github.com/geaxgx/playing-card-detection/blob/master/creating_playing_cards_dataset.ipynb </br>
and 52 homemade videos of cards. </br>

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

