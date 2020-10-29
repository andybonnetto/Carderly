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
