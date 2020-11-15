import tkinter as tk
from PIL import ImageTk, Image

class Page(tk.Frame):
    def __init__(self, *args, **kwargs):
        tk.Frame.__init__(self, *args, **kwargs)
    def show(self):
        self.lift()

class Page1(Page):
   def __init__(self, *args, **kwargs):
       Page.__init__(self, *args, **kwargs)
       label = tk.Label(self, text="This is page 1")
       label.pack(side="top", fill="both", expand=True)

class Page2(Page):
   def __init__(self, *args, **kwargs):
       Page.__init__(self, *args, **kwargs)
       label = tk.Label(self, text="This is page 2")
       label.pack(side="top", fill="both", expand=True)

class Page3(Page):
   def __init__(self, *args, **kwargs):
       Page.__init__(self, *args, **kwargs)
       label = tk.Label(self, text="This is page 3")
       label.pack(side="top", fill="both", expand=True)

class MainView(tk.Frame):
    def __init__(self, *args, **kwargs):
        tk.Frame.__init__(self, *args, **kwargs)
        p1 = Page1(self)
        p2 = Page2(self)
        p3 = Page3(self)

        buttonframe = tk.Frame(self)
        container = tk.Frame(self)
        buttonframe.pack(side="top", fill="x", expand=False)
        container.pack(side="top", fill="both", expand=True)
        
        
        imgpath = "background.png"
        img = Image.open(imgpath)
        photo = ImageTk.PhotoImage(img)
        
        p1.place(in_=container, x=0, y=0, relwidth=1, relheight=1)
        p2.place(in_=container, x=0, y=0, relwidth=1, relheight=1)
        p3.place(in_=container, x=0, y=0, relwidth=1, relheight=1)

        canvas = tk.Canvas(root,width=500,height=500, bd=0, highlightthickness=0)
        canvas.pack()
        canvas.create_image(0, 0, image=photo)
        
        CanvasButton(canvas,"Page 1",p1)
        #b1 = tk.Button(buttonframe, text="Page 1", command=p1.lift)
        #b2 = tk.Button(buttonframe, text="Page 2", command=p2.lift)
        #b3 = tk.Button(buttonframe, text="Page 3", command=p3.lift)

        #b1.pack(side="left")
        #b2.pack(side="left")
        #b3.pack(side="left")

        p1.show()
        
class CanvasButton:
    def __init__(self, canvas,txt,page):
        self.canvas = canvas
        self.number = tk.IntVar()
        self.button = tk.Button(canvas, textvariable=txt,
                                command=page.lift)
        self.id = canvas.create_window(50, 50, width=50, height=50,
                                       window=self.button)
    def buttonclicked(self):
        self.number.set(self.number.get()+1)  # auto updates Button

if __name__ == "__main__":
    root = tk.Tk()
    main = MainView(root)
    #main.pack(side="top", fill="both", expand=True)
    #root.wm_geometry("400x400")
    root.mainloop()
