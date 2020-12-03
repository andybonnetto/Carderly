from kivy.app import App
from kivy.lang import Builder
from kivy.uix.screenmanager import ScreenManager, Screen
from kivy.properties import ObjectProperty
from kivy.uix.popup import Popup
from kivy.uix.label import Label
from kivy.uix.button import Button
from database import Database
from kivy.clock import Clock
from kivy.graphics import *
import RPi.GPIO as GPIO
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

class MainWindow(Screen):

    def __init__(self,**kwargs):
        super(MainWindow,self).__init__(**kwargs)
        Clock.schedule_interval(self.button_callback, 0.01)

    def button_callback(self,token):
        if GPIO.input(40) == GPIO.LOW:
            print("button pushed")
            self.shift_to_waiting()

    def shift_to_waiting(self):
        sm.current = "waiting"

    def shift_to_settings(self):
        sm.current = "settings"

    def shift_to_contact(self):
        sm.current = "contact"

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
        contacts = database.child('rooms').child(ROOM_NAME).get()                 #TODO Enter the final name of the room
        space = 0
        for contact in contacts.each():
            if space != 0:
                self.add_widget(Label(text=contact.val()["Name"], font_size=50, pos=(-500 + space * 230, 0)))
            space += 1
        if space >= 3:
            self.full = True

class GameWindow(Screen):
    def shift_to_waiting(self):
        sm.current = "waiting"

    def highlight_turn(self,token):
    #Draw green rectangle during player's turn or show "Your turn"
        player_turn = database.child('Current to play').get().val()
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
        contacts = database.child('rooms').child(ROOM_NAME).get()                 #TODO Enter the final name of the room
        space = 0
        for contact in contacts.each():
            if space != 0:
                self.add_widget(Label(text=contact.val()["Name"], font_size=50, pos=(-450 + space * 220, -(space%2)*100+100)))
            space += 1
        if space >= 4:
            self.full = True
    def __init__(self,**kwargs):
        super(GameWindow, self).__init__(**kwargs)
        self.yourturn = Label(text="", pos=(-10,-80), font_size=50,markup=True)
        self.add_widget(self.yourturn)
        self.name_display_game()
        self.choose_atout()
        Clock.schedule_interval(self.highlight_turn, 0.5)
    def choose_atout(self):
        def choose_spade(instance):
            print("you choose {}".format(instance.text))
            self.atout = "spade"
            self.remove_buttons()
        def choose_heart(instance):
            print("you choose {}".format(instance.text))
            self.atout = "heart"
            self.remove_buttons()
        def choose_diamond(instance):
            print("you choose {}".format(instance.text))
            self.atout = "diamond"
            self.remove_buttons()
        def choose_clubs(instance):
            print("you choose {}".format(instance.text))
            self.atout = "clubs"
            self.remove_buttons()

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
    GPIO.setmode(GPIO.BCM)
    GPIO.setwarnings(False)
    GPIO.setup(40, GPIO.IN, pull_up_down=GPIO.PUD_UP)

# def popup_change():
#     pop = Popup(title='Change Account',
#                   content=Label(text=''),
#                   size_hint=(None, None), size=(400, 400))
#     pop.open()

sm = WindowManager()
kv = Builder.load_file("my.kv")
screens = [MainWindow(name="main_win"), WaitingRoom(name="waiting"),GameWindow(name="game"),Settings(name="settings"),ContactWindow(name="contact")]
for screen in screens:
    sm.add_widget(screen)

sm.current = "main_win"

class MyMainApp(App):
    def build(self):
        return sm

if __name__ == "__main__":
    # database = firebase.database()
    # player = database.child("Player 1")
    # card = player.child("Card 1").get().val()
    # print(card)
    MyMainApp().run()
