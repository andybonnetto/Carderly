import RPi.GPIO as GPIO
import time
from Motor_servo_angle import ServoAngle
from Motor_servo_360 import Servo360
from Motor_step import StepMotor
from Motor_DC import DCMotor

#Define motor Classes with PINS


def call_servo_angle():
    servo_motor = ServoAngle(5,3)
    servo_motor.activate(3)
    servo_motor.cleanup()
    
def call_stepper():
    StepPins = [17, 4, 23, 24]
    step_motor = StepMotor(StepPins)
    nbStepsPerRev = 30
    step_motor.define_sequence('half')
    step_motor.run_arm(nbStepsPerRev)
    pass
def call_servo_360(status = 'input'):
    servo_motor360 = Servo360(18)
    if status == 'input':
        servo_motor360.activate(1)
    elif status == 'output':
        pass
    servo_motor360.cleanup()

def shuffle(DC_motor):
    #call the DC motor to go to next step separated by an interval of ? sec
    for current_pos in range(1,32,1):
        DC_motor.go_to_position(current_pos+1)
        time.sleep(0.5) #sleeps one second between each steps

def discard(pos):
    #call the DC motor to go to card in the database position (np.where in saved list)
    DC_motor.go_to_pos(pos)

def call_dc():
    DC_motor = DCMotor(13, 19, 26)
    DC_motor.run_arm()

if __name__ == "__main__":
    call_dc()
    call_servo_angle()
    call_servo_360()
