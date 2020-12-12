############## Python-OpenCV Playing Card Detector ###############
#
# Author: Evan Juras
# Date: 9/5/17
# Description: Python script to detect and identify playing cards
# from a PiCamera video feed.
#

# Import necessary packages
import cv2
import numpy as np
import time
import os
import Cards
import VideoStream
import pyrebase

def card_to_label(rank,suit):
    if rank == "Ace":
        r = "A"
    elif rank == "Two":
        r = "2"
    elif rank == "Three":
        r = "3"
    elif rank == "Four":
        r = "4"
    elif rank == "Five":
        r = "5"
    elif rank == "Six":
        r = "6"
    elif rank == "Seven":
        r = "7"
    elif rank == "Eight":
        r = "8"
    elif rank == "Nine":
        r = "9"
    elif rank == "Ten":
        r = "10"
    elif rank == "Jack":
        r = "J"
    elif rank == "Queen":
        r = "Q"
    elif rank == "King":
        r = "K"
    else:
        r ="U"

    if suit == "Spades":
        s = "s"
    elif suit == "Hearts":
        s = "h"
    elif suit == "Diamonds":
        s = "d"
    elif suit == "Clubs":
        s = "c"
    else:
        s = "U"
    label = r+s
    return label

def label_to_num(label):
    label_list = list(label)
    if label_list[-1] == "c":
        first_num = 1
    elif label_list[-1] == "s":
        first_num = 2
    elif label_list[-1] == "d":
        first_num = 3
    elif label_list[-1] == "h":
        first_num = 4
    else:
        return 0
    if label_list[0] == "U":
        return 0
    elif label_list[0] == "1":
        second_num = 10
    elif label_list[0] == "J":
        second_num = 11
    elif label_list[0] == "Q":
        second_num = 12
    elif label_list[0] == "K":
        second_num = 13
    elif label_list[0] == "A":
        second_num = 14
    else:
        second_num = int(label_list[0])
    num = first_num*100+second_num
    return num


config = {
    "apiKey": "",
    "authDomain": "carderlydatabase.firebaseapp.com",
    "databaseURL": "https://carderlydatabase.firebaseio.com/",
    "storageBucket": "carderlydatabase.appspot.com"
}
firebase = pyrebase.initialize_app(config)
database = firebase.database()


### ---- INITIALIZATION ---- ###
# Define constants and initialize variables

## Camera settings
IM_WIDTH = 720
IM_HEIGHT = 480
FRAME_RATE = 25
BOX_HEIGHT = 356
BOX_WIDTH = 232
## Initialize calculated frame rate because it's calculated AFTER the first time it's displayed
frame_rate_calc = 1
freq = cv2.getTickFrequency()

## Define font to use
font = cv2.FONT_HERSHEY_SIMPLEX

# Initialize camera object and video feed from the camera. The video stream is set up
# as a seperate thread that constantly grabs frames from the camera feed. 
# See VideoStream.py for VideoStream class definition
## IF USING USB CAMERA INSTEAD OF PICAMERA,
## CHANGE THE THIRD ARGUMENT FROM 1 TO 2 IN THE FOLLOWING LINE:
videostream = VideoStream.VideoStream((IM_WIDTH,IM_HEIGHT),FRAME_RATE,1,0).start()
time.sleep(1) # Give the camera time to warm up

# Load the train rank and suit images
path = os.path.dirname(os.path.abspath(__file__))
train_ranks = Cards.load_ranks( path + '/Card_Imgs/')
train_suits = Cards.load_suits( path + '/Card_Imgs/')


### ---- MAIN LOOP ---- ###
# The main loop repeatedly grabs frames from the video stream
# and processes them to find and identify playing cards.

cam_quit = 0 # Loop control variable

# Begin capturing frames
while cam_quit == 0:

    # Grab frame from video stream
    image = videostream.read()

    # Start timer (for calculating frame rate)
    t1 = cv2.getTickCount()

    # Pre-process camera image (gray, blur, and threshold it)
    pre_proc = Cards.preprocess_image(image)
    mask = np.empty_like(pre_proc)*0
    box = np.array([255] * BOX_HEIGHT * BOX_WIDTH).reshape(BOX_HEIGHT, BOX_WIDTH)
    x = 100
    y = 250
    mask[x:x + BOX_HEIGHT, y:y + BOX_WIDTH] = box
    # Find and sort the contours of all cards in the image (query cards)
    cnts_sort, cnt_is_card = Cards.find_cards(mask)

    # If there are no contours, do nothing
    if len(cnts_sort) != 0:

        # Initialize a new "cards" list to assign the card objects.
        # k indexes the newly made array of cards.
        cards = []
        k = 0

        # For each contour detected:
        for i in range(len(cnts_sort)):
            if (cnt_is_card[i] == 1):
                # Create a card object from the contour and append it to the list of cards.
                # preprocess_card function takes the card contour and contour and
                # determines the cards properties (corner points, etc). It generates a
                # flattened 200x300 image of the card, and isolates the card's
                # suit and rank from the image.
                cards.append(Cards.preprocess_card(cnts_sort[i],image))

                # Find the best rank and suit match for the card.
                cards[k].best_rank_match,cards[k].best_suit_match,cards[k].rank_diff,cards[k].suit_diff = Cards.match_card(cards[k],train_ranks,train_suits)

                # Draw center point and match result on the image.
                image = Cards.draw_results(image, cards[k])
                k = k + 1
	    
        # Draw card contours on image (have to do contours all at once or
        # they do not show up properly for some reason)
        if (len(cards) != 0):
            temp_cnts = []
            # Open File and write card value in it
            # f = open("CardValues.txt", "w")
            for i in range(len(cards)):
            #     if(cards[i].best_rank_match != "Unknown" and cards[i].best_suit_match != "Unknown"):
            #         f.write("{} {}".format(cards[i].best_rank_match,cards[i].best_suit_match))
                temp_cnts.append(cards[i].contour)
            # f.close()
            cv2.drawContours(image,temp_cnts, -1, (255,0,0), 2)
            label = card_to_label(cards[0].best_rank_match,cards[0].best_suit_match)
            cardseen = label_to_num(label)
            database.child("Vision").set(cardseen)
        
        
    # Draw framerate in the corner of the image. Framerate is calculated at the end of the main loop,
    # so the first time this runs, framerate will be shown as 0.
    # cv2.putText(image,"FPS: "+str(int(frame_rate_calc)),(10,26),font,0.7,(255,0,255),2,cv2.LINE_AA)
    # Finally, display the image with the identified cards!
    cv2.imshow("Card Detector",image)

    # Calculate framerate
    # t2 = cv2.getTickCount()
    # time1 = (t2-t1)/freq
    # frame_rate_calc = 1/time1
    
    # Poll the keyboard. If 'q' is pressed, exit the main loop.
    key = cv2.waitKey(1) & 0xFF
    if key == ord("q"):
        cam_quit = 1

# Close all windows and close the PiCamera video stream.
# cv2.destroyAllWindows()
videostream.stop()

