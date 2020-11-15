from tkinter import *
from PIL import ImageTk,Image

root=Tk()

#set width and height

canvas=Canvas(root,width=1500,height=1500)

#give this image path. image should be in png format.

#Example: "C:\\Users\\ASUS\\OneDrive\\Pictures\\image.png"

image=ImageTk.PhotoImage(Image.open("background.png"))

canvas.create_image(0,0,anchor=NW,image=image)
canvas.pack()
root.mainloop()