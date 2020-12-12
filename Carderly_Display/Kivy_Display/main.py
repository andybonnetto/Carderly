from kivy.app import App
from kivy.lang import Builder
from kivy.uix.screenmanager import ScreenManager, Screen
from kivy.properties import ObjectProperty,NumericProperty
from kivy.uix.popup import Popup
from kivy.uix.label import Label
from kivy.uix.button import Button
from database import Database
from kivy.clock import Clock
from functools import partial
from kivy.graphics import *
import RPi.GPIO as GPIO

PIN_BLUE = 40
PIN_GREEN = 38
PIN_RED = 32
PIN_GREY = 36

GPIO.setmode(GPIO.BOARD)
GPIO.setwarnings(False)
GPIO.setup(PIN_GREEN, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
GPIO.setup(PIN_BLUE, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
GPIO.setup(PIN_GREY, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
GPIO.setup(PIN_RED, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)

import pyrebase

config = {
    "apiKey": "",
    "authDomain": "carderlydatabase.firebaseapp.com",
    "databaseURL": "https://carderlydatabase.firebaseio.com/",
    "storageBucket": "carderlydatabase.appspot.com"
}
firebase = pyrebase.initialize_app(config)
database = firebase.database()
ROOM_NAME = 'Dani'
blue_button_state = False
red_button_state = False
green_button_state = False
grey_button_state = False

def num_to_label(num):
    color_num = int(num/100)
    if color_num == 1:
        color = "Clubs"
    elif color_num == 2:
        color = "Spade"
    elif color_num == 3:
        color = "Diamond"
    else:
        color = "Heart"
    val_num = num - color_num*100
    if val_num == 11:
        val = "Jack"
    elif val_num == 12:
        val = "Queen"
    elif val_num == 13:
        val = "King"
    elif val_num == 14:
        val = "Ace"
    else:
        val = str(val_num)
    label = val + " of " + color
    return label

def num_to_trump(atout):
    if atout == 1:
        return "spade"
    elif atout == 2:
        return "clubs"
    elif atout == 3:
        return "diamond"
    else:
        return "heart"
class MainWindow(Screen):

    def __init__(self,**kwargs):
        super(MainWindow,self).__init__(**kwargs)
        Clock.schedule_interval(self.blue_button_callback, 0.1)
        Clock.schedule_interval(self.green_button_callback, 0.1)
        Clock.schedule_interval(self.red_button_callback, 0.1)
        Clock.schedule_interval(self.grey_button_callback, 0.1)


    def blue_button_callback(self,token):
        global blue_button_state
        if not blue_button_state:
            blue_button_state = False
            if GPIO.input(PIN_BLUE) == GPIO.HIGH:
                if sm.current == "main_win":
                    self.shift_to_insert()
                    blue_button_state = True
                    return
                elif sm.current == "insert_deck":
                    self.shift_to_waiting()
                    blue_button_state = True
                    return
                elif sm.current == "waiting":
                    self.shift_to_game()
                    blue_button_state = True
                    return
                elif sm.current == "contact":
                    self.shift_to_main()
                    blue_button_state = True
                    return
                # elif sm.current == "game":
                #     db = database.child("rooms").child(ROOM_NAME).child("OldPersonTrump").get().val()
                #     if db:
                #         self.choose_atout()
                #         self.choose_atout.choose_spade()
                #         self.remove_button()
                #         blue_button_state = True

        else:
            if GPIO.input(PIN_BLUE) == GPIO.LOW:
                blue_button_state = False

    def red_button_callback(self,token):
        global red_button_state
        if not red_button_state:
            red_button_state = False
            if GPIO.input(PIN_RED) == GPIO.HIGH:
                if sm.current == "main_win":
                    self.shift_to_contact()
                    red_button_state = True
                    return
                if sm.current == "insert_deck":
                    self.shift_to_main()
                    red_button_state = True
                    return
                if sm.current == "waiting":
                    self.shift_to_insert()
                    red_button_state = True
                    return
                if sm.current == "settings":
                    self.shift_to_main()
                    red_button_state = True
                    return
        else:
            if GPIO.input(PIN_BLUE) == GPIO.LOW:
                red_button_state = False

    def green_button_callback(self, token):
        global green_button_state
        if not green_button_state:
            green_button_state = False
            if GPIO.input(PIN_GREEN) == GPIO.HIGH:
                if sm.current == "insert_deck":
                    print("green")
                    green_button_state = True
                    return
                if sm.current == "main_win":
                    self.shift_to_settings()
                    green_button_state = True
                    return
        else:
            if GPIO.input(PIN_BLUE) == GPIO.LOW:
                green_button_state = False

    def grey_button_callback(self, token):
        global grey_button_state
        if not grey_button_state:
            grey_button_state = False
            if GPIO.input(PIN_GREY) == GPIO.HIGH:
                if sm.current == "main_win":
                    self.shift_to_insert()
                    grey_button_state = True
                    return
                if sm.current == "insert_deck":
                    self.shift_to_waiting()
                    grey_button_state = True
                    return
                if sm.current == "waiting":
                    self.shift_to_game()
                    grey_button_state = True
                    return
        else:
            if GPIO.input(PIN_BLUE) == GPIO.LOW:
                grey_button_state = False

    def shift_to_insert(self):
        sm.current = "insert_deck"

    def shift_to_settings(self):
        sm.current = "settings"

    def shift_to_contact(self):
        sm.current = "contact"

    def shift_to_main(self):
        sm.current = "main_win"

    def shift_to_game(self):
        sm.current = "game"

    def shift_to_waiting(self):
        sm.current = "waiting"

class InsertDeck(Screen):
    mess = ObjectProperty(None)

    def __init__(self,**kwargs):
        super(InsertDeck,self).__init__(**kwargs)
        Clock.schedule_interval(self.get_shift_on, 3)
        Clock.schedule_interval(self.get_shift_off, 4)
        Clock.schedule_interval(self.get_status, 0.1)

    def shift_to_waiting(self):
        sm.current = "waiting"
    def get_status(self,token):
        if database.child("StartGame").get().val():
            if sm.current == "insert_deck":
                self.shift_to_waiting()
    def get_shift_on(self,token):
        self.mess = ""
    def get_shift_off(self,token):
        self.mess = "Card treatment, please wait..."

class WaitingRoom(Screen):

    p1 = ObjectProperty(None)
    p2 = ObjectProperty(None)
    p3 = ObjectProperty(None)

    def shift_to_game(self):
        sm.current = "game"

    def shift_to_main(self):
        sm.current = "main_win"

    def get_status(self,token):
        if database.child("PlayGame").get().val():
            if sm.current == "waiting":
                self.shift_to_game()
            pass

    def __init__(self,**kwargs):
        super(WaitingRoom,self).__init__(**kwargs)
        token = False
        self.name_display(token)
        Clock.schedule_interval(self.name_display, 0.1)
        Clock.schedule_interval(self.get_status, 0.1)

    def name_display(self,token):
        contacts = [database.child("rooms").child(ROOM_NAME).child("Player 1").child("Name").get().val(),
                    database.child("rooms").child(ROOM_NAME).child("Player 2").child("Name").get().val(),
                    database.child("rooms").child(ROOM_NAME).child("Player 3").child("Name").get().val(),
                    database.child("rooms").child(ROOM_NAME).child("Player 4").child("Name").get().val()]
        if contacts[1]:
            self.p1 = contacts[1]
        if contacts[2]:
            self.p2 = contacts[2]
        if contacts[3]:
            self.p3 = contacts[3]

class GameWindow(Screen):
    atout_kv = ObjectProperty(None)
    im_atout_kv = ObjectProperty(None)
    card_vis = ObjectProperty(None)
    yourturn = ObjectProperty(None)
    r1 = ObjectProperty(0.0)
    r2 = ObjectProperty(0.0)
    r3 = ObjectProperty(0.0)
    r4 = ObjectProperty(0.0)
    p1 = ObjectProperty(None)
    p2 = ObjectProperty(None)
    p3 = ObjectProperty(None)

    def __init__(self,**kwargs):
        super(GameWindow, self).__init__(**kwargs)
        self.name_display_game()
        self.choose_atout()
        Clock.schedule_interval(self.highlight_turn, 0.1)
        Clock.schedule_interval(self.show_atout,0.01)
        Clock.schedule_interval(self.show_vis,0.05)

    def shift_to_waiting(self):
        sm.current = "waiting"

    def highlight_turn(self,token):
    #Draw green rectangle during player's turn or show "Your turn"

        player_turn = database.child("rooms").child(ROOM_NAME).child('Current to play').get().val()

        self.r1 = 0
        self.r2 = 0
        self.r3 = 0
        self.r4 = 0
        self.yourturn = ""

        if player_turn == 2:
            self.r1 = 0.7
        elif player_turn == 3:
            self.r2 = 0.7
        elif player_turn == 4:
            self.r3 = 0.7
        else:
            self.yourturn = "Your Turn"
            self.r4 = 0.7


    def name_display_game(self):
        contacts = [database.child("rooms").child(ROOM_NAME).child("Player 1").child("Name").get().val(),
                    database.child("rooms").child(ROOM_NAME).child("Player 2").child("Name").get().val(),
                    database.child("rooms").child(ROOM_NAME).child("Player 3").child("Name").get().val(),
                    database.child("rooms").child(ROOM_NAME).child("Player 4").child("Name").get().val()]

        if contacts[1]:
            self.p1 = contacts[1]
        if contacts[2]:
            self.p2 = contacts[2]
        if contacts[3]:
            self.p3 = contacts[3]

    def choose_atout(self):
        def choose_spade(instance):
            db_atout = database.child("rooms").child(ROOM_NAME).child("Trump")
            print("you choose {}".format(instance.text))
            self.atout = "spade"
            self.remove_buttons()
            db_atout.set(1)
        def choose_heart(instance):
            db_atout = database.child("rooms").child(ROOM_NAME).child("Trump")
            print("you choose {}".format(instance.text))
            self.atout = "heart"
            self.remove_buttons()
            db_atout.set(4)
        def choose_diamond(instance):
            db_atout = database.child("rooms").child(ROOM_NAME).child("Trump")
            print("you choose {}".format(instance.text))
            self.atout = "diamond"
            self.remove_buttons()
            db_atout.set(3)
        def choose_clubs(instance):
            db_atout = database.child("rooms").child(ROOM_NAME).child("Trump")
            print("you choose {}".format(instance.text))
            self.atout = "clubs"
            self.remove_buttons()
            db_atout.set(2)

        self.Spade = Button(text="spade", size_hint=(0.3,0.1), pos=(280,200), font_size=(self.width + self.height)/5,background_color=(1,1,0))
        self.Spade.bind(on_press=choose_spade)
        self.Heart = Button(text="heart", size_hint=(0.3,0.1), pos=(100,280), font_size=(self.width + self.height)/5,background_color=(1,0,0))
        self.Heart.bind(on_press=choose_heart)
        self.Diamond = Button(text="diamond", size_hint=(0.3,0.1), pos=(300,380), font_size=(self.width + self.height)/5,background_color=(0,1,0))
        self.Diamond.bind(on_press=choose_diamond)
        self.Clubs = Button(text="clubs", size_hint=(0.3,0.1), pos=(500,280), font_size=(self.width + self.height)/5,background_color=(0,0,1))
        self.Clubs.bind(on_press=choose_clubs)
        self.add_widget(self.Spade)
        self.add_widget(self.Heart)
        self.add_widget(self.Diamond)
        self.add_widget(self.Clubs)

    def remove_buttons(self):
        self.remove_widget(self.Spade)
        self.remove_widget(self.Heart)
        self.remove_widget(self.Diamond)
        self.remove_widget(self.Clubs)
    def show_atout(self,token):
        atout = database.child("rooms").child(ROOM_NAME).child("Trump").get().val()
        trump = num_to_trump(atout)
        if atout:
            self.atout_kv = trump
            self.im_atout_kv = "{}.png".format(trump)
    def show_vis(self,token):
        db_vision = database.child("Vision").get()
        card_seen = db_vision.val()
        if card_seen:
            label = num_to_label(card_seen)
            self.card_vis = label
        elif card_seen == 0:
            self.card_vis = ""



class Settings(Screen):
    def shift_to_main(self):
        sm.current = "main_win"
    def change_account(self):
        # popup_change()
        pass

class ContactWindow(Screen):
    def shift_to_main(self):
        sm.current = "main_win"
    def read_contacts(self):
        db = Database("contact.txt")
        return db
    def __init__(self,**kwargs):
        super(ContactWindow,self).__init__(**kwargs)
        db = self.read_contacts()
        space = 0
        for contact in db.contact:
            self.add_widget(Label(text = "{}\n".format(contact), font_size="30",pos=(0,50+space)))
            space += 30

class WindowManager(ScreenManager):
    pass

# def popup_change():
#     pop = Popup(title='Change Account',
#                   content=Label(text=''),
#                   size_hint=(None, None), size=(400, 400))
#     pop.open()

sm = WindowManager()
kv = Builder.load_file("my.kv")
screens = [MainWindow(name="main_win"), WaitingRoom(name="waiting"),GameWindow(name="game"),Settings(name="settings"),ContactWindow(name="contact"),InsertDeck(name="insert_deck")]
for screen in screens:
    sm.add_widget(screen)

sm.current = "main_win"

class MyMainApp(App):
    def build(self):
        return sm

def main():
    MyMainApp().run()

if __name__ == "__main__":
    MyMainApp().run()
