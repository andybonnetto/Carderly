import RPi.GPIO as GPIO
import time

servoPIN = 5
GPIO.setmode(GPIO.BCM)
GPIO.setup(servoPIN, GPIO.OUT)

p = GPIO.PWM(servoPIN, 60) # GPIO 17 for PWM with 50Hz
p.start(2.5) # Initialization where dc is the duty cycle (0.0 <= dc <= 100.0)
try:
  while True:
    p.ChangeDutyCycle(12.5)
    time.sleep(1)
    p.ChangeDutyCycle(3)
    time.sleep(1)
except KeyboardInterrupt:
  p.stop()
  GPIO.cleanup()
