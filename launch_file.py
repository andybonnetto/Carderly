import threading
from Carderly_Motor import call_motor
import numpy as np
import pyrebase
import time
import os

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

#reset variables into DB
database.child("StartGame").set(0)
database.child("PlayGame").set(0)
database.child("CountPlayer").set(1)
database.child("PlayedCard").set(1)
database.child("Vision").set(0)

#Constant variable
ROOM_NAME = "Dani"

# Path to files
PathActivateVision1 = "Carderly_Vision/TensorFlow-Lite-Object-Detection-on-Android-and-Raspberry-Pi/Activate_Vision1.py"
PathActivateVision2 = "Carderly_Vision/TensorFlow-Lite-Object-Detection-on-Android-and-Raspberry-Pi/Activate_Vision2.py"
PathMain = "Carderly_Display/Kivy_Display/main.py"
PathModel = "Cardely_Vision/TFlite_models/32"

# Variable initialization
Game = 1
# CE QUI EST EN MAJ EST CE QUI DOIT ETRE AJUSTEEEE



def main():
    # Run the display continuously in background
    while Game:
        # exec(open(PathMain).read())
        os.system("KIVY_BCM_DISPMANX_ID=1 python3 " + PathMain)
        time.sleep(2)

def vision():
    # Run the vision continuously in background, changing between 2 states (vision in the machine / on the table)
    while Game:
        
        # Initialize state to switch from ActivateVision1 to 2
        state_vision = 0

        # ActivateVision1 when putting cards into the wheel
        # while state_vision == 0:  #Pas besoin de while ici, ya déjà un while dans vision
        # exec(open(PathActivateVision1).read())
        os.system("python3 " + PathActivateVision1)
        time.sleep(2)
        start_game = database.child("StartGame").get()
        if start_game.val() == 1: state_vision = 1

        # ActivateVision2 when playing the game
        # while state_vision == 1:
        # exec(open(PathActivateVision2).read())
        os.system("python3 " + PathActivateVision2 + " --modeldir=" + PathModel)
        time.sleep(2)
        start_game = database.child("StartGame").get()
        if start_game.val() == 0:
            state_vision = 0
            # break


def wait_deck():
    # Checking when the deck has been inserted when vision detects a card
    deck_insert = 0
    while deck_insert==0:
        card = database.child("Vision").get()
        if card.val()!=0: deck_insert=1
        time.sleep(2)
        print("Wait for deck")        


def wait_player():
    # Counting the number of player until it reaches 4
    nb_player=1
    while nb_player<4:
        player_DB = database.child("CountPlayer").get()
        nb_player = player_DB.val()
        time.sleep(2)
        print("Wait for player") 


def GameFunc():
    #Initialize step motor
    step_motor = call_motor.define_step_motor()

    # Wait for deck to be inserted
    wait_deck()    

    # Put the cards into the wheel when deck inserted, putting them into array to know their positions
    cards=np.array([])
    for i in range(31):
        # Get the card detected on the DB
        card_detected_DB = database.child("Vision").get()
        # Add it to the array
        cards = np.append(cards, card_detected_DB.val())
        # Put it into the wheel
        call_motor.call_servo_360("input")
        time.sleep(2)
        call_motor.shuffle(step_motor)
        time.sleep(2) # Time to put the card into the wheel + time for the vision to detect new cards (ADD CONDITION CHGMT CARTE?)
   
    #print("cards = " + str(cards))

    # Wait for 4 players
    wait_player()

    # Start the distribution when 4 players connected, switch variable on DB for the APP and display
    database.child("StartGame").set(1)

    # Get the old player's cards from the DB (A CHANGER SELON DB ORGANISATION)
    old_cards=np.array([])
    for j in range(8): 
        temp_DB=database.child("rooms").child(ROOM_NAME).child("Player 1").child("Card "+str(j+1)).get()
        old_cards=np.append(old_cards, temp_DB.val())
    
    #print("old=" +str(old_cards))

    # Distribute the cards of the old player's 
    for k in range(8):
        # Search position of the old player's card in the DB 
        res=np.where(cards==old_cards[k])
        position=res[0]
        # Take the card out 
        call_motor.discard(step_motor,position)
        time.sleep(2)
        call_motor.call_dc()
        time.sleep(2)
        call_motor.call_servo_angle()
        time.sleep(2)
        call_motor.call_servo_360("output")
        time.sleep(2) #TO BE ADJUSTED 

    # Start the game when card distributed, change variable in the DB for the APP
    database.child("PlayGame").set(1)

    # Get the end of game variable from the DB 
    EndOfGame = database.child("EndGame").get()

    # Game part 
    while not EndOfGame.val():

        # Get whose turn it is 
        turn_DB=database.child("Current to play").get()

        # old player's turn 
        if turn_DB.val()==1: 
            time.sleep(3) # OU ATTENDRE QUE CARTE CHANGE ??

        # other's turn 
        else: 
            # Check when new card played (JSP SI CONDITION OKKKK, sinon ajouter nparray des cartes jouées)
            temp = database.child("rooms").child(ROOM_NAME).child("PlayedCard").get()
            time.sleep(1) 
            card_played_DB = database.child("rooms").child(ROOM_NAME).child("PlayedCard").get()
            while(temp.val() == card_played_DB.val()):
                temp = database.child("rooms").child(ROOM_NAME).child("PlayedCard").get()
                time.sleep(1)
                card_played_DB = database.child("rooms").child(ROOM_NAME).child("PlayedCard").get()

            # Get the position of the card
            res=np.where(cards==card_played_DB.val())
            position=res[0]
            # Take the card out 
            call_motor.discard(step_motor,position)
            time.sleep(2)
            call_motor.call_dc()
            time.sleep(2)
            call_motor.call_servo_angle()
            time.sleep(2)
            call_motor.call_servo_360("output")
            time.sleep(2) # A AJUSTEEEEER
        
        # End of game (CONDITION TO BE DETERMINED)
        EndOfGame = database.child("EndGame").get()

    print("reste")

thDisplay=threading.Thread(target=main)
thVision=threading.Thread(target=vision)
thGame=threading.Thread(target=GameFunc)

thDisplay.start()
thVision.start()
thGame.start()   
