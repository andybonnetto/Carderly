import RPi.GPIO as GPIO
import time

servoPIN = 27
GPIO.setmode(GPIO.BCM)
GPIO.setup(servoPIN, GPIO.OUT)

servo2PIN = 18
GPIO.setmode(GPIO.BCM)
GPIO.setup(servo2PIN, GPIO.OUT)

s = GPIO.PWM(servo2PIN,50)
s.start(2.5)

p = GPIO.PWM(servoPIN, 50) # GPIO 17 for PWM with 50Hz
p.start(2.5) # Initialization
try:
  while True:
    p.ChangeDutyCycle(5)
    time.sleep(0.5)
    p.ChangeDutyCycle(7.5)
    time.sleep(0.5)
    p.ChangeDutyCycle(10)
    time.sleep(0.5)
    p.ChangeDutyCycle(12.5)
    time.sleep(0.5)
    s.ChangeDutyCycle(10)
    time.sleep(0.5)
    s.ChangeDutyCycle(7.5)
    time.sleep(0.5)
    s.ChangeDutyCycle(5)
    time.sleep(0.5)
    s.ChangeDutyCycle(2.5)
    time.sleep(0.5)
except KeyboardInterrupt:
  p.stop()
  GPIO.cleanup()
