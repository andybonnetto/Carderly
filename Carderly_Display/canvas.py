import tkinter as tk
from PIL import ImageTk, Image

class CanvasButton:
    def __init__(self, canvas):
        self.canvas = canvas
        self.number = tk.IntVar()
        self.button = tk.Button(canvas, textvariable=self.number,
                                command=self.buttonclicked)
        self.id = canvas.create_window(100, 100, width=50, height=100,
                                       window=self.button)
    def buttonclicked(self):
        self.number.set(self.number.get()+1)  # auto updates Button

root = tk.Tk()
root.resizable(width=False, height=False)
root.wm_attributes("-topmost", 1)

imgpath = "background.png"
img = Image.open(imgpath)
photo = ImageTk.PhotoImage(img)

canvas = tk.Canvas(root,width=500,height=500, bd=0, highlightthickness=0)
canvas.pack()
canvas.create_image(0, 0, image=photo)

CanvasButton(canvas)  # create a clickable button on the canvas

root.mainloop()