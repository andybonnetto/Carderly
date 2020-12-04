import RPi.GPIO as GPIO
GPIO.setmode(GPIO.BOARD)
GPIO.setwarnings(False)
GPIO.setup(40, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
button = True
while button:
    button = True
    if GPIO.input(40) == GPIO.HIGH:
        print("button pushed")
        button = False
