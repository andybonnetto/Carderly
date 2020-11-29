import RPi.GPIO as GPIO
from time import sleep

GPIO.setmode(GPIO.BCM)

Motor2 = {'EN': 13, 'input1': 19, 'input2': 26}

for x in Motor2:
    GPIO.setup(Motor2[x], GPIO.OUT)

EN2 = GPIO.PWM(Motor2['EN'], 100)
EN2.start(0)


print("FORWARD MOTION")
EN2.ChangeDutyCycle(40)

GPIO.output(Motor2['input1'], GPIO.HIGH)
GPIO.output(Motor2['input2'], GPIO.LOW)

sleep(5)

print("STOP")
EN2.ChangeDutyCycle(0)

sleep(2)

print("BACKWARD MOTION")
EN2.ChangeDutyCycle(40)
GPIO.output(Motor2['input1'], GPIO.LOW)
GPIO.output(Motor2['input2'], GPIO.HIGH)
sleep(5)

print("STOP")
EN2.ChangeDutyCycle(0)

sleep(5)

GPIO.cleanup()
