import RPi.GPIO as GPIO
import time
from Motor_servo_angle import ServoMotor


servo_motor = ServoMotor(5,2.5)
servo_motor.activate(2.5)
servo_motor.cleanup()