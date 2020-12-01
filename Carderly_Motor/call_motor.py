import RPi.GPIO as GPIO
import time
from Motor_servo_angle import ServoMotor
from Motor_step import StepMotor
from Motor_DC import DCMotor

#Define motor Classes with PINS


def call_servo_angle():
    servo_motor = ServoMotor(5,2.5)
    # servo_motor.activate(2.5)
    # servo_motor.cleanup()
    pass
def call_stepper():
    StepPins = [17, 4, 23, 24]
    step_motor = StepMotor(StepPins)
    nbStepsPerRev = 30
    step_motor.define_sequence('half')
    step_motor.run_arm(nbStepsPerRev)
    pass
def call_servo_360(status = 'input'):
    if status == 'input':
        pass
    elif status== 'output':
        pass

def shuffle(DC_motor):
    #call the DC motor to go to next step separated by an interval of ? sec
    for current_pos in range(1,32,1):
        DC_motor.go_to_position(current_pos+1)
        time.sleep(0.5) #sleeps one second between each steps

def discard(pos):
    #call the DC motor to go to card in the database position (np.where in saved list)
    DC_motor.go_to_pos(pos)

def clean_up_DC(DC_motor):
    DC_motor.cleanup()

if __name__ == "__main__":
    DC_motor = DCMotor(13, 19, 26)
    call_stepper()
    shuffle(DC_motor)
    clean_up_DC(DC_motor)
