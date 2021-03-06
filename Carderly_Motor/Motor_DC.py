import RPi.GPIO as GPIO
import math
from time import sleep
FREQ = 100
FULL_SPEED = 17*math.pi/16 #rot/s

class DCMotor:

    # def __init__(self, EN, input1, input2):
    #     GPIO.setmode(GPIO.BCM)
    #     GPIO.setwarnings(False)
    #     self.PIN = {'EN': EN, 'input1': input1 , 'input2': input2}
    #     for x in self.PIN:
    #         GPIO.setup(self.PIN[x], GPIO.OUT)
    #     self.EN1 = GPIO.PWM(self.PIN['EN'], FREQ) #GPIO EN for PWM with FREQ Hz
    #     self.current_pos = 1
    #
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
    #
    # def go_to_position(self,pos):
    #     mov_step = self.find_dir(pos)
    #     period = self.step_to_period(mov_step)
    #     if mov_step > 0:
    #         self.clockwise(period=period)  #TODO define speed (testing)
    #     elif mov_step < 0:
    #         self.counter_clockwise(period=period)  #TODO define speed (testing)
    #     self.current_pos = (self.current_pos - mov_step)%32
    #
    # def find_dir(self,pos):
    #     mov_step = self.current_pos - pos
    #     if abs(mov_step) < 16:
    #
    #         return mov_step
    #     else:
    #         return -mov_step
    # def step_to_period(self,mov_step):
    #     step_turn_time = math.pi/FULL_SPEED # for 17/16pi per second speed
    #     period = abs(mov_step)*step_turn_time
    #
    #     return period

    def __init__(self, EN, input1, input2):

        GPIO.setmode(GPIO.BCM)
        GPIO.setwarnings(False)
        self.PIN = {'EN': EN, 'input1': input1, 'input2': input2}
        for x in self.PIN:
            GPIO.setup(self.PIN[x], GPIO.OUT)
        self.EN1 = GPIO.PWM(self.PIN['EN'], FREQ) #GPIO EN for PWM with FREQ Hz
        self.EN1.start(0)
        # self.current_pos = 1

    def run_arm(self):
        self.clockwise(period = 0.8)
        self.counter_clockwise(period = 0.71)

    def cleanup(self):
        GPIO.cleanup()
