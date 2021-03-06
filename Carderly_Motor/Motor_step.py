import RPi.GPIO as GPIO
import time

WAIT_TIME = 0.02 #0.002

class StepMotor:

    def __init__(self, StepPins):    # Use BCM GPIO references instead of physical pin numbers, 18,22,24,26 GPIO24,GPIO25,GPIO8,GPIO7
        GPIO.setmode(GPIO.BCM)
        # Set all pins as output
        self.StepPins = StepPins
        for pin in StepPins:
            print("Setup pins")
            GPIO.setup(pin, GPIO.OUT)
            GPIO.output(pin, False)
        self.current_pos = 0


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
        nb = sign * nb  # times 2 because half-step
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

    def calculate_step_pos(self, pos):
        val = -int(pos/4) + pos * 19
        if pos % 4 > 3:
            val -= 1
        return val

    def find_dir(self, pos):
        pos_diff = self.current_pos - pos
        if abs(pos_diff) < 15:
            return -pos_diff
        else:
            return pos_diff

    def go_to_pos(self, target_pos):
        pos_diff = self.find_dir(target_pos)
        target_steps = self.calculate_step_pos(target_pos)
        current_steps = self.calculate_step_pos(self.current_pos)

        if pos_diff == 0:
            return self.current_pos
        if pos_diff > 0:
            if pos_diff > 15:
                self.steps(600 - current_steps)
                steps = target_steps
                self.steps(steps)
            else:
                steps = target_steps - current_steps
                self.steps(steps)

        elif pos_diff < 0:
            if pos_diff < -15:
                self.steps(-current_steps)
                steps = 600 - target_steps
                self.steps(-steps)
            else:
                steps = target_steps - current_steps
                self.steps(steps)
        self.current_pos = target_pos
