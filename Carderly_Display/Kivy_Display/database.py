import json


class Database:
    def __init__(self,filename):
        self.filename= filename
        self.contact = {}
        self.load()
        print(self.contact)

    def load(self):
        self.file = open(self.filename, "r")
        for line in self.file:
            name, email = line.strip().split(";")
            self.contact[name] = email
        self.file.close()