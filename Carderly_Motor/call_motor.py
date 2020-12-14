import time
from Carderly_Motor.Motor_servo_angle import ServoAngle
from Carderly_Motor.Motor_servo_360 import Servo360
from Carderly_Motor.Motor_step import StepMotor
from Carderly_Motor.Motor_DC import DCMotor


def call_servo_angle():
    servo_motor = ServoAngle(5,4)
    servo_motor.activate(4)

def call_servo_360(status = 'input'):
    servo_motor360_in = Servo360(18)
    servo_motor360_out = Servo360(27)
    if status == 'input':
        servo_motor360_in.activate(5,dc=1) #period and dc
    elif status == 'output':
        servo_motor360_out.activate(5,dc=100)


def shuffle(step_motor):
    #call the DC motor to go to next step separated by an interval of ? sec
    step_motor.go_to_pos(step_motor.current_pos+1)
    time.sleep(0.5) #naps between each steps

def discard(step_motor, pos):
    #call the DC motor to go to card in the database position (np.where in saved list)
    if pos >= 16:
        opp_pos = pos - 16
    else:
        opp_pos = pos + 16
    step_motor.go_to_pos(opp_pos)

def call_dc():
    DC_motor = DCMotor(13, 19, 26)
    DC_motor.run_arm()

def define_step_motor():
    StepPins = [17, 4, 23, 24]
    step_motor = StepMotor(StepPins)
    step_motor.define_sequence('half')
    return step_motor

if __name__ == "__main__":
    #call_dc()
    #call_servo_angle()
    call_servo_360()
    #step_motor = define_step_motor()
    #shuffle(step_motor)
    time.sleep(1)
    #discard(step_motor,6)
