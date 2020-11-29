from kivy.app import App
from kivy.lang import Builder
from kivy.uix.screenmanager import ScreenManager, Screen
from kivy.properties import ObjectProperty
from kivy.uix.popup import Popup
from kivy.uix.label import Label
from database import Database
from kivy.clock import Clock
from kivy.graphics import *
import RPi.GPIO as GPIO
buttonPIN = 40
GPIO.setmode(GPIO.BOARD)
GPIO.setup(buttonPIN, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
import pyrebase

config = {
    "apiKey": "",
    "authDomain": "carderlydatabase.firebaseapp.com",
    "databaseURL": "https://carderlydatabase.firebaseio.com/",
    "storageBucket": "carderlydatabase.appspot.com"
}
firebase = pyrebase.initialize_app(config)
database = firebase.database()

class MainWindow(Screen):
    def __init__(self,**kwargs):
        super(MainWindow,self).__init__(**kwargs)
        if GPIO.input(buttonPIN) == GPIO.HIGH:
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
        contacts = database.child('rooms').child('Andy').get()                 #TODO Enter the final name of the room
        space = 0
        for contact in contacts.each():
            if space != 0:
                self.add_widget(Label(text=contact.val(), font_size=50, pos=(-500 + space * 230, 0)))
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
        contacts = database.child('rooms').child('Andy').get()                 #TODO Enter the final name of the room
        space = 0
        for contact in contacts.each():
            if space != 0:
                self.add_widget(Label(text=contact.val(), font_size=50, pos=(-450 + space * 220, -(space%2)*100+100)))
            space += 1
        if space >= 4:
            self.full = True
    def __init__(self,**kwargs):
        super(GameWindow, self).__init__(**kwargs)
        self.yourturn = Label(text="", pos=(-10,-80), font_size=50,markup=True)
        self.add_widget(self.yourturn)
        self.name_display_game()
        Clock.schedule_interval(self.highlight_turn, 0.5)


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
