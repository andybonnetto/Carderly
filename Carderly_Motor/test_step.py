import RPi.GPIO as GPIO
import time
PIN1 = 17
PIN2 = 4
PIN3 = 23
PIN4 = 24
GPIO.setmode(GPIO.BCM)
GPIO.setup(PIN1, GPIO.OUT)
GPIO.setup(PIN2, GPIO.OUT)
GPIO.setup(PIN3, GPIO.OUT)
GPIO.setup(PIN4, GPIO.OUT)
GPIO.output(PIN1, False)
GPIO.output(PIN2, False)
GPIO.output(PIN3, False)
GPIO.output(PIN4, False)

GPIO.output(PIN1,True)
GPIO.output(PIN2,True)
GPIO.output(PIN3,False)
GPIO.output(PIN4,False)
time.sleep(2)
GPIO.output(PIN1,False)
GPIO.output(PIN2,True)
GPIO.output(PIN3,True)
GPIO.output(PIN4,False)
time.sleep(21)