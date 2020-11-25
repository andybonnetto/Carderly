import RPi.GPIO as GPIO
import Math
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
FREQ = 100
FULL_SPEED =  #RANDOM VALUE

class DCMotor:

    def __init__(self, EN, input1, input2):

        self.PIN  = {'EN': EN, 'input1': input1 , 'input2': input2}
        self.EN1 = GPIO.PWM(self.PIN['EN'], FREQ) #GPIO EN for PWM with FREQ Hz
        self.current_pos = 1

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

    def go_to_position(self,pos):
        mov_step = self.find_dir(pos)
        period = self.step_to_period(mov_step)
        if mov_step > 0:
            self.clockwise(period=period)  #TODO define speed
        elif mov_step < 0:
            self.counter_clockwise(period=period)  #TODO define speed
        self.current_pos = (self.current_pos + mov_step)%32

    def find_dir(self,pos):
        mov_step = self.current_pos - pos
        if Math.abs(mov_step) < 16:
            return(mov_step)
        else:
            return(-mov_step)
    def step_to_period(self,mov_step):
        half_turn_time = 2 # for pi/2 per second speed #TODO find period for half turn using full speed
        period = math.abs(mov_step)*half_turn_time/16
        return period


    def cleanup(self):
        GPIO.cleaup()