import RPi.GPIO as GPIO
import time

WAIT_TIME = 0.005

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
            self.Seq = range(0, self.StepCount)
            self.Seq[0] = [1, 0, 0, 0]
            self.Seq[1] = [0, 1, 0, 0]
            self.Seq[2] = [0, 0, 1, 0]
            self.Seq[3] = [0, 0, 0, 1]
            return self.Seq
        elif sequence == "half":
            # Define advanced half-step sequence
            self.StepCount = 8
            self.Seq = range(0, self.StepCount)
            self.Seq[0] = [1, 0, 0, 0]
            self.Seq[1] = [1, 1, 0, 0]
            self.Seq[2] = [0, 1, 0, 0]
            self.Seq[3] = [0, 1, 1, 0]
            self.Seq[4] = [0, 0, 1, 0]
            self.Seq[5] = [0, 0, 1, 1]
            self.Seq[6] = [0, 0, 0, 1]
            self.Seq[7] = [1, 0, 0, 1]
            return self.Seq
        elif sequence == "arm":
            self.StepCount = 2
            self.Seq = range(0,self.StepCount)
            self.Seq[0] = [1, 0, 0, 0]
            self.Seq[1] = [0, 0, 1, 0]
            return self.Seq
        else:
            print("sequence undefined")
            return False

    def steps(self,nb,seq = self.Seq): #nb = nbtour/revolution, depends on motor datasheet, can be negative
        StepCounter = 0
        if nb < 0:
            sign = -1
        else:
            sign = 1
        nb = sign * nb * 2  # times 2 because half-step
        print("nbsteps {} and sign {}".format(nb, sign))
        for i in range(nb):
            for pin in range(len(self.StepPins)):
                xpin = self.StepPins[pin]
                if seq[StepCounter][pin] != 0:
                    GPIO.output(xpin, True)
                else:
                    GPIO.output(xpin, False)
            StepCounter += sign
            # If we reach the end of the sequence
            # start again
            if StepCounter == self.StepCount:
                StepCounter = 0
            if StepCounter < 0:
                StepCounter = self.StepCount - 1
                # Wait before moving on
            time.sleep(WAIT_TIME)