from kivy.app import App
from kivy.lang import Builder
from kivy.uix.screenmanager import ScreenManager, Screen
from kivy.properties import ObjectProperty
from kivy.uix.popup import Popup
from kivy.uix.label import Label
from database import Database
from kivy.clock import Clock


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
    MyMainApp().run()