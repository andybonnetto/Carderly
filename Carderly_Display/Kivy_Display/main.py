from kivy.app import App
from kivy.lang import Builder
from kivy.uix.screenmanager import ScreenManager, Screen
from kivy.properties import ObjectProperty,StringProperty
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
ROOM_NAME = 'Faf'

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
    val_num = num - color_num
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
    label = val + "of" + color
    return label

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
                if sm.current == "insert_deck":
                    self.shift_to_waiting()
                    blue_button_state = True
                    return
                if sm.current == "waiting":
                    self.shift_to_game()
                    blue_button_state = True
                    return
                if sm.current == "contact":
                    self.shift_to_main()
                    blue_button_state = True
                    return
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

    def shift_to_waiting(self):
        sm.current = "waiting"
    def get_status(self):
        pass
        #read database shuffling starts
    def get_shift_on(self,token):
        self.mess = ""
    def get_shift_off(self,token):
        self.mess = "Card treatment, please wait..."

class WaitingRoom(Screen):

    def shift_to_game(self):
        # if self.full:
        #     sm.current = "game"
        # else:
        #     sm.current = "waiting"
        sm.current = "game"

    def shift_to_main(self):
        sm.current = "main_win"

    def __init__(self,**kwargs):
        self.full = False
        super(WaitingRoom,self).__init__(**kwargs)
        token = False
        self.name_display(token)
        Clock.schedule_interval(self.name_display, 3)

    def name_display(self,token):
        # contacts = database.child('rooms').child(ROOM_NAME).get()                 #TODO Enter the final name of the room
        # space = 0
        # contact_name = [0, 0, 0, 0]
        # for i in range(4):
        #     contact_name[i] = Label(text="", font_size=50, pos=(-500 + space * 230, 0))
        # for contact in contacts.each():
        #     if space != 0:
        #         contact_name[space].text = contact.val()["Name"]
        #         self.add_widget(contact_name[space])
        #     space += 1
        # if space >= 3:
        #     self.full = True
        pass

class GameWindow(Screen):
    atout_kv = ObjectProperty(None)
    im_atout_kv = ObjectProperty(None)
    card_vis = ObjectProperty(None)
    def shift_to_waiting(self):
        sm.current = "waiting"

    def highlight_turn(self,token):
    #Draw green rectangle during player's turn or show "Your turn"
        # player_turn = database.child('Current to play').get().val()
        player_turn = 2 #CHAAAAAAAAAAAAAAAAANGE
        self.yourturn.text= ""
        self.canvas.add(Color(0,1,0,0.5, mode='rgba'))
        try:
            self.canvas.remove(self.rect)
        except:
            print("your turn")

        if player_turn == 2:
            self.rect = Rectangle(pos=(80,250),size=(200,100))
            self.canvas.add(self.rect)
        elif player_turn == 3:
            self.rect = Rectangle(pos=(320,350),size=(200,100))
            self.canvas.add(self.rect)
        elif player_turn == 4:
            self.rect = Rectangle(pos=(520,250),size=(200,100))
            self.canvas.add (self.rect)
        elif player_turn == 1:
            self.canvas.add(Color(1, 1, 1, 0.2, mode='rgba'))
            self.rect = Rectangle(pos=(265,150),size=(250,100))
            self.canvas.add(self.rect)
            self.yourturn.text="[color=111111]Your turn[/color]"

    def name_display_game(self):
        # contacts = database.child('rooms').child(ROOM_NAME).get()                 #TODO Enter the final name of the room
        # space = 0
        # for contact in contacts.each():
        #     if space != 0:
        #         self.add_widget(Label(text=contact.val()["Name"], font_size=50, pos=(-450 + space * 220, -(space%2)*100+100)))
        #     space += 1
        # if space >= 4:
        #     self.full = True
        pass
    def __init__(self,**kwargs):
        super(GameWindow, self).__init__(**kwargs)
        self.yourturn = Label(text="", pos=(-10,-80), font_size=50,markup=True)
        self.add_widget(self.yourturn)
        self.name_display_game()
        self.choose_atout()
        Clock.schedule_interval(self.highlight_turn, 0.5)
        Clock.schedule_interval(self.show_atout,0.05)
        Clock.schedule_interval(self.show_vis,0.05)
    def choose_atout(self):
        def choose_spade(instance):
            db_atout = database.child("Atout")
            print("you choose {}".format(instance.text))
            self.atout = "spade"
            self.remove_buttons()
            db_atout.set(self.atout)
        def choose_heart(instance):
            db_atout = database.child("Atout")
            print("you choose {}".format(instance.text))
            self.atout = "heart"
            self.remove_buttons()
            db_atout.set(self.atout)
        def choose_diamond(instance):
            db_atout = database.child("Atout")
            print("you choose {}".format(instance.text))
            self.atout = "diamond"
            self.remove_buttons()
            db_atout.set(self.atout)
        def choose_clubs(instance):
            db_atout = database.child("Atout")
            print("you choose {}".format(instance.text))
            self.atout = "clubs"
            self.remove_buttons()
            db_atout.set(self.atout)

        self.Spade = Button(text="spade", size_hint=(0.3,0.1), pos=(300,200))
        self.Spade.bind(on_press=choose_spade)
        self.Heart = Button(text="heart", size_hint=(0.3,0.1), pos=(100,300))
        self.Heart.bind(on_press=choose_heart)
        self.Diamond = Button(text="diamond", size_hint=(0.3,0.1), pos=(300,400))
        self.Diamond.bind(on_press=choose_diamond)
        self.Clubs = Button(text="clubs", size_hint=(0.3,0.1), pos=(500,300))
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
        db_atout = database.child("Atout")
        atout = db_atout.get().val()
        if atout:
            self.atout_kv = atout
            self.im_atout_kv = "{}.png".format(atout)
    def show_vis(self,token):
        db_vision = database.child("Vision").get()
        card_seen = db_vision.val()
        if card_seen:
            label = num_to_label(card_seen)
            self.card_vis = label



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
