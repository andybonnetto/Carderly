import RPi.GPIO as GPIO
import time

WAIT_TIME = 0.002

class StepMotor:

    def __init__(self, StepPins):    # Use BCM GPIO references instead of physical pin numbers, 18,22,24,26 GPIO24,GPIO25,GPIO8,GPIO7
        GPIO.setmode(GPIO.BCM)
        # Set all pins as output
        self.StepPins = StepPins
        for pin in StepPins:
            print("Setup pins")
            GPIO.setup(pin, GPIO.OUT)
            GPIO.output(pin, False)


    def define_sequence(self,sequence):
        self.Seq = []
        if sequence == "full":
            # Define simple sequence
            self.StepCount = 4
            self.Seq = list(range(0, self.StepCount))
            self.Seq[0] = [1, 0, 0, 0]
            self.Seq[1] = [0, 1, 0, 0]
            self.Seq[2] = [0, 0, 1, 0]
            self.Seq[3] = [0, 0, 0, 1]
            return self.Seq
        elif sequence == "half":
            # Define advanced half-step sequence
            self.StepCount = 8
            self.Seq = list(range(0, self.StepCount))
            self.Seq[0] = [1, 0, 0, 0]
            self.Seq[1] = [1, 1, 0, 0]
            self.Seq[2] = [0, 1, 0, 0]
            self.Seq[3] = [0, 1, 1, 0]
            self.Seq[4] = [0, 0, 1, 0]
            self.Seq[5] = [0, 0, 1, 1]
            self.Seq[6] = [0, 0, 0, 1]
            self.Seq[7] = [1, 0, 0, 1]
            return self.Seq
        else:
            print("sequence undefined")
            return False

    def steps(self,nb): #nb = nbtour/revolution, depends on motor datasheet, can be negative
        stepcounter = 0
        if nb < 0:
            sign = -1
        else:
            sign = 1
        nb = sign * nb * 2  # times 2 because half-step
        print("nbsteps {} and sign {}".format(nb, sign))
        for i in range(nb):
            for pin in range(len(self.StepPins)):
                xpin = self.StepPins[pin]
                if self.Seq[stepcounter][pin] != 0:
                    GPIO.output(xpin, True)
                else:
                    GPIO.output(xpin, False)
            stepcounter += sign
            # If we reach the end of the sequence
            # start again
            if stepcounter == self.StepCount:
                stepcounter = 0
            if stepcounter < 0:
                stepcounter = self.StepCount - 1
                # Wait before moving on
            time.sleep(WAIT_TIME)

    def run_arm(self,nb):
        hasRun = False
        while not hasRun:
            self.steps(nb)  # parcourt un tour dans le sens horaire
            time.sleep(0.1)
            self.steps(-nb)  # parcourt un tour dans le sens anti-horaire
            time.sleep(1)
            hasRun = True
            print("Stop motor")
            for pin in self.StepPins:
                GPIO.output(pin, False)