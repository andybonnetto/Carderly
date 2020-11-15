import RPi.GPIO as GPIO
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
FREQ = 100

class DCMotor:

    def __init__(self, EN = 25, input1 = 24, input2 = 23):

        self.PIN  = {'EN': EN, 'input1': input1 , 'input2': input2}
        self.EN1 = GPIO.PWM(self.PIN['EN'], FREQ) #GPIO EN for PWM with FREQ Hz

    def clockwise(self,duty_cycle=40,period=0.1):
        for a in range(40, 100):
            print("clockwise motion")
            self.EN1.ChangeDutyCycle(duty_cycle)

            GPIO.output(self.PIN['input1'], GPIO.HIGH)
            GPIO.output(self.PIN['input2'], GPIO.LOW)

            sleep(period)

        print("STOP")
        self.EN1.ChangeDutyCycle(0)

        sleep(0.05)

    def counter_clockwise(self,duty_cycle=40,period=0.1):

        for a in range(40, 100):
            print("counter clockwise motion")
            self.EN1.ChangeDutyCycle(duty_cycle)

            GPIO.output(self.PIN['input1'], GPIO.LOW)
            GPIO.output(self.PIN['input2'], GPIO.HIGH)

            sleep(period)

        print("STOP")
        self.EN1.ChangeDutyCycle(0)

        sleep(0.05)

    def cleanup(self):
        GPIO.cleaup()