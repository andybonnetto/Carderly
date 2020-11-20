from kivy.app import App
from kivy.lang import Builder
from kivy.uix.screenmanager import ScreenManager, Screen
from kivy.properties import ObjectProperty
from kivy.uix.popup import Popup
from kivy.uix.label import Label
from database import Database
from kivy.clock import Clock
from kivy.graphics import *
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
    def shift_to_waiting(self):
        sm.current = "waiting"
    def shift_to_settings(self):
        sm.current = "settings"
    def shift_to_contact(self):
        sm.current = "contact"

class WaitingRoom(Screen):
    def shift_to_game(self):
        if self.full:
            sm.current = "game"
        else:
            sm.current = "waiting"
    def shift_to_main(self):
        sm.current = "main_win"
    def read_waiting_file(self):
        wf = Database("waiting.txt")
        return wf
    def __init__(self,**kwargs):
        self.full = False
        super(WaitingRoom,self).__init__(**kwargs)
        token = False
        self.name_display(token)
        Clock.schedule_interval(self.name_display, 3)

    def name_display(self,token):
        wf = self.read_waiting_file()
        space = 0
        for contact in wf.contact:
            self.add_widget(Label(text=contact, font_size=30, pos=(-150 + space*120, 130 - space*120)))
            space += 1
        if space >=3:
            self.full = True

class GameWindow(Screen):
    def shift_to_waiting(self):
        sm.current = "waiting"
    def highlight_turn(self,token):
        player_turn = database.child('Current to play').get().val()
        self.yourturn.text= ""
        self.canvas.add(Color(0,1,0,0.5, mode='rgba'))
        try:
            self.canvas.remove(self.rect)
        except:
            pass

        if player_turn ==1:
            self.rect = Rectangle(pos=(320,300),size=(200,100))
            self.canvas.add(self.rect)
        elif player_turn==2:
            self.rect = Rectangle(pos=(170,150),size=(200,100))
            self.canvas.add(self.rect)
        elif player_turn==3:
            self.rect = Rectangle(pos=(500,150),size=(200,100))
            self.canvas.add (self.rect)
        elif player_turn==4:
            self.yourturn.text="Your turn"
    def __init__(self,**kwargs):
        super(GameWindow, self).__init__(**kwargs)
        self.yourturn = Label(text="", pos=(self.height / 2, self.width / 2), font_size=50)
        self.add_widget(self.yourturn)
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
