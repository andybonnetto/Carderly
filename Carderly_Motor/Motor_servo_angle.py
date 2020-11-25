import RPi.GPIO as GPIO
import time
FREQ = 50
DUTY_THRESHOLD = 50
WAITING_TIME = 1.5 #in second
MAX_DUTY_CYCLE = 12
class ServoMotor:
    def __init__(self, pin, start_duty_cycle=2.5):
        self.PIN = pin
        GPIO.setmode(GPIO.BCM)
        GPIO.setup(self.PIN, GPIO.OUT)
        self.pwm = GPIO.PWM(self.PIN, FREQ) # GPIO 17 for PWM with 50Hz
        self.pwm.start(start_duty_cycle) # Initialization where dc is the duty cycle (0.0 <= duty_cycle <= 100.0)

    def activate(self,start_duty_cycle=2.5):
        self.pwm.ChangeDutyCycle(MAX_DUTY_CYCLE)
        time.sleep(WAITING_TIME)
        self.pwm.ChangeDutyCycle(start_duty_cycle)
        time.sleep(WAITING_TIME)


    def cleanup(self):
        self.pwm.stop()
        GPIO.cleanup()

