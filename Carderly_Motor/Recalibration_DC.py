import RPi.GPIO as GPIO
import math
from time import sleep
FREQ = 100
FULL_SPEED = 17*math.pi/16 #rot/s

class DCMotor:

    def clockwise(self,duty_cycle=100,period=0.2):

        print("clockwise motion")
        self.EN1.ChangeDutyCycle(duty_cycle)

        GPIO.output(self.PIN['input1'], GPIO.HIGH)
        GPIO.output(self.PIN['input2'], GPIO.LOW)
        print(period)
        sleep(period)

        print("STOP")
        self.EN1.ChangeDutyCycle(0)

        sleep(0.2)

    def counter_clockwise(self,duty_cycle=100,period=0.2):

        print("counter clockwise motion")
        self.EN1.ChangeDutyCycle(duty_cycle)

        GPIO.output(self.PIN['input1'], GPIO.LOW)
        GPIO.output(self.PIN['input2'], GPIO.HIGH)
        print(period)
        sleep(period)

        print("STOP")
        self.EN1.ChangeDutyCycle(0)

        sleep(0.1)

    def __init__(self, EN, input1, input2):

        GPIO.setmode(GPIO.BCM)
        GPIO.setwarnings(False)
        self.PIN = {'EN': EN, 'input1': input1, 'input2': input2}
        for x in self.PIN:
            GPIO.setup(self.PIN[x], GPIO.OUT)
        self.EN1 = GPIO.PWM(self.PIN['EN'], FREQ) #GPIO EN for PWM with FREQ Hz
        self.EN1.start(0)

    def run_arm(self):
        self.clockwise(period = 0.1)
        self.counter_clockwise(period = 0)

    def cleanup(self):
        GPIO.cleanup()

if __name__ == "__main__":
    Motor = DCMotor(13,19,26)
    Motor.run_arm()


