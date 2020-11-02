
# Import necessary packages
import cv2
import time

cam = cv2.VideoCapture(0)

for i in range(10): 
    ret,frame = cam.read()
    if ret:
        cv2.imwrite('/home/pi/Carderly/Carderly_Vision/Vision_Card_Detection/Snapshots/snapshot{}.png'.format(i),frame)
    time.sleep(1)
cam.release()    
