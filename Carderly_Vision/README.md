## Object detection with API
A graph had been trained with 52 cards (soon with 32) and converted into "detect.tflite". With the company of the "labelmap.txt" file, it contains the dection model which can be tested by launching the 
```
python /path/to/TFLite_detection_webcam.py –modeldir=/path/to/tflite_folder 
```
(soon files and paths on github) </br>
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

