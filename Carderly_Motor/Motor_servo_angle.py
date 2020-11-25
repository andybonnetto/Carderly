import RPi.GPIO as GPIO
import time
FREQ = 50
DUTY_THRESHOLD = 50
class = ServoMotor:
    def __init__(self, pin, duty_cycle):
        self.PIN = pin
        GPIO.setmode(GPIO.BCM)
        GPIO.setup(self.PIN, GPIO.OUT)
        self.pwm = GPIO.PWM(self.PIN, FREQ) # GPIO 17 for PWM with 50Hz
        self.pwm.start(duty_cycle) # Initialization where dc is the duty cycle (0.0 <= duty_cycle <= 100.0)

    def forward(self,duty_cycle,period):
        if duty_cycle <= DUTY_THRESHOLD:
            self.pwm.ChangeDutyCycle(duty_cycle)
            time.sleep(period)
            return True
        else:
            return False

    def backward(self,duty_cycle,period):
        if duty_cycle >= DUTY_THRESHOLD:
            self.pwm.ChangeDutyCycle(duty_cycle)
            time.sleep(period)
            return True
        else:
            return False

    def cleanup(self):
        self.pwm.stop()
        GPIO.cleanup()