import RPi.GPIO as GPIO
import time
FREQ = 50
WAITING_TIME = 1.5 #in second
MAX_DUTY_CYCLE = 20
class Servo360:
    def __init__(self, pin):
        self.PIN = pin
        GPIO.setmode(GPIO.BCM)
        GPIO.setup(self.PIN, GPIO.OUT)
        self.pwm = GPIO.PWM(self.PIN, FREQ) # GPIO 17 for PWM with 50Hz
        self.pwm.start(0)

    def activate(self,period):
        self.pwm.ChangeDutyCycle(MAX_DUTY_CYCLE)
        time.sleep(period)


    def cleanup(self):
        self.pwm.stop()
        GPIO.cleanup()
