import threading
from Carderly_Motor import call_motor
import numpy as np
import pyrebase
import time
import os
import argparse

# Firebase configuration
config = {
    "apiKey": "",
    "authDomain": "carderlydatabase.firebaseapp.com",
    "databaseURL": "https://carderlydatabase.firebaseio.com/",
    "storageBucket": "carderlydatabase.appspot.com"
}
firebase = pyrebase.initialize_app(config)
database = firebase.database()

# Global variables
global Game
global nb_player
global deck_insert

# Constant variable
parser = argparse.ArgumentParser()
parser.add_argument('--room_name', help='give the name of the room', default="Dani")
args = parser.parse_args()
ROOM_NAME = args.room_name

# reset variables into DB
database.child("rooms").child(ROOM_NAME).child("StartGame").set(0)
database.child("rooms").child(ROOM_NAME).child("PlayGame").set(0)
database.child("rooms").child(ROOM_NAME).child("PlayedCard").set(1)
database.child("rooms").child(ROOM_NAME).child("Vision").set(0)
database.child("rooms").child(ROOM_NAME).child("DeckInserted").set(0)
database.child("rooms").child(ROOM_NAME).child("DeckPresent").set(0)
database.child("rooms").child(ROOM_NAME).child("CardOut").set(0)

# Path to files
PathActivateVision1 = "Carderly_Vision/TensorFlow-Lite-Object-Detection-on-Android-and-Raspberry-Pi/Activate_Vision1.py"
PathActivateVision2 = "Carderly_Vision/TensorFlow-Lite-Object-Detection-on-Android-and-Raspberry-Pi/Activate_Vision2.py"
PathMain = "Carderly_Display/Kivy_Display/main.py"
PathModel = "Carderly_Vision/TFlite_models/32"

# Variable initialization
Game = 1


# CE QUI EST EN MAJ EST CE QUI DOIT ETRE AJUSTEEEE


def main():
    # Run the display continuously in background
    while Game:
        # exec(open(PathMain).read())
        os.system("KIVY_NO_ARGS=1 KIVY_BCM_DISPMANX_ID=0 python3 " + PathMain + " --room_name=" + ROOM_NAME)
        time.sleep(2)


def vision():
    # Run the vision continuously in background, changing between 2 states (vision in the machine / on the table)
    while Game:

        # Initialize state to switch from ActivateVision1 to 2
        state_vision = 0

        # ActivateVision1 when putting cards into the wheel
        os.system("python3 " + PathActivateVision1 + " --room_name=" + ROOM_NAME)
        time.sleep(2)
        start_game = database.child("rooms").child(ROOM_NAME).child("StartGame").get()
        if start_game.val() == 1: state_vision = 1

        # ActivateVision2 when playing the game
        if state_vision == 1:
            os.system("python3 " + PathActivateVision2 + " --modeldir=" + PathModel + " --room_name=" + ROOM_NAME)
            time.sleep(2)
            start_game = database.child("rooms").child(ROOM_NAME).child("StartGame").get()
            if start_game.val() == 0:
                state_vision = 0


def wait_deck():
    # Checking when the deck has been inserted when vision detects a card
    deck_insert = 0
    while deck_insert == 0:
        card = database.child("rooms").child(ROOM_NAME).child("Vision").get()
        if card.val() != 0: deck_insert = 1
        time.sleep(2)
        print("Wait for deck")


def wait_player():
    # Counting the number of player until it reaches 4
    nb_player = 1
    while nb_player < 4:
        player_DB = database.child("rooms").child(ROOM_NAME).child("CountPlayer").get()
        nb_player = player_DB.val()
        time.sleep(2)
        print("Wait for player")


def discard_all(step_motor):
    for i in range(31):
        call_motor.discard(step_motor, i)
        time.sleep(2)
        call_motor.call_dc()
        time.sleep(2)
        call_motor.call_servo_360("output")
        time.sleep(2)


def GameFunc():
    # Initialize step motor
    step_motor = call_motor.define_step_motor()

    # Wait for deck to be inserted
    wait_deck()
    database.child("rooms").child(ROOM_NAME).child("DeckPresent").set(1)
    # Put the cards into the wheel when deck inserted, putting them into array to know their positions
    cards = np.array([])
    # automatic card generation
    for i in range(4):
        for j in range(8):
            count = (i + 1) * 100 + (j + 7)
            cards = np.append(cards, count)

    np.delete(cards, [30])

    print("cards = " + str(cards))
    database.child("rooms").child(ROOM_NAME).child("StartGame").set(1)
    # Put it into the wheel
    call_motor.call_servo_360("input")
    time.sleep(4)
    # call_motor.shuffle(step_motor)
    call_motor.discard(step_motor,0)
    time.sleep(4)
    call_motor.call_dc()

    # Deck is inserted, shift display to waiting room
    database.child("rooms").child(ROOM_NAME).child("DeckInserted").set(1)

thDisplay = threading.Thread(target=main)
thVision = threading.Thread(target=vision)
thGame = threading.Thread(target=GameFunc)

thDisplay.start()
thVision.start()
thGame.start()
