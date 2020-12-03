import RPi.GPIO as GPIO
import time
FREQ = 100
class Servo360:
    def __init__(self, pin=18):
        self.PIN = pin
        GPIO.setmode(GPIO.BCM)
        GPIO.setup(self.PIN, GPIO.OUT)
        self.pwm = GPIO.PWM(self.PIN, FREQ) # GPIO 17 for PWM with 50Hz
        self.pwm.start(0)

    def activate(self,period,dc):
        self.pwm.ChangeDutyCycle(dc)
        time.sleep(period)


    def cleanup(self):
        self.pwm.stop()
        GPIO.cleanup()
