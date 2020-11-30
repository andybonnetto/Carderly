# Motor
Carderly uses 5 motors to work which are 2x 360servo motors for the casters, 1x angle servo motor for the flipping system, 1x stepper motor for the push push arm and 1x DC motor for the main wheel. 
## Motor launch
All motors can be launch by using the functions python file "call_motor.py" (workin on it), it calls subfunctions with classes for each type of motors, parameters and pins must be entered according to the scheme in the bottom.
At the end of every motor call, a *cleanup* function set in every class is necessary to disconnect motor from pins in order to let the energy for other motors. PINS refer to GPIO pins shown on scheme ([pin/GPIO relation](https://github.com/andybonnetto/Carderly/blob/main/Carderly_Motor/pins.PNG))
In the launch file : you can use
```
from call_motor import *

call_servo_angle()
call_stepper()
call_servo_360(status) #status is either 'input' or 'output'
```
The DC motor will be called in the *shuffle.py* and *discard.py* that you can call this way
```
from call motor import *

shuffle()
discard(pos) #pos of the card to discard (quick np.where in the card saved list)
```
And don't forget to cleanup the port for the DC motor which needs to keep on the whole process
```
from call motor import *

clean_up_DC()
```

### Servo motor -angle
Angle servo motors work from 0° to 180° and uses only 1 command and 2 power supply cables with a PWM input. Demo file found in [Raspberry Pi tutorials](https://tutorials-raspberrypi.com/raspberry-pi-servo-motor-control/)
and final class namely *Motor_servo_angle* should work for the flipping system by adjusting the flipping time in between 2 flips. Speed used seems to be the maximum speed.

in call_motor :
```
from Motor_servo_angle import ServoMotor

servo_motor = ServoMotor(PIN,STARTING_DC) #default value for duty cycle is 2.5
servo_motor.activate(STARTING_DC)
servo_motor.cleanup()
```

### Servo motor -360
No function has been written yet, though must look like the angle one

### DC motor
For the wheel to operate correctly, certain positions must be reached. The positions are integers going from 1 to 32 and current position is initialized to 0 when first declaration of the motor class.
Sub-functions exist in order to turn the motor in the right direction toward the targetted position according to the current position. Convert position shift to period of time and send to pwm with a certain fixed frequency.
Class function are in *Motor_DC.py* and demo file comes from [Maker Pro](https://maker.pro/raspberry-pi/projects/raspberry-pi-dc-motor-control-with-custom-board). The motor chosen
must be supplied by an external 12V battery.

in call motor:
```
from Motor_DC import DCMotor

dc_motor = DCMotor(PIN_enable, PIN_input1, PIN_input_2)
dc_motor.go_to_position(target_position) #enter integer between 1 and 32
dc_motor.cleanup
```
(!!NOT TESTED!!)
*TODO: find speed dependance on number of turn before testing and define speed ratio with PWM

### Stepper motor
A sequence for the arm with 2 position is created in *Motor_step.py* (should do 180° but can be arranged). Parameters are nb/turn per revolution : find in datasheet, and a string
for the sequence name which must be "arm" but other demos are created for "full" speed turn and "half" speed turn. Something must be done for the direction of rotation (still dunno how this work)
Demo file comes from [aranacorp](https://www.aranacorp.com/fr/pilotez-un-moteur-pas-a-pas-avec-raspberrypi/)

in call motor:
```
from Motor_step import StepMotor

step_motor = StepMotor(StepPins) #list of used pins [pin1,pin2,pin3,pin4] (TODO find the pins)
sequence = step_motor.define_sequence("arm")
step_motor.steps(Nb_per_turn, sequence)
step_motor.cleanup
```
(!!NOT TESTED!!)
*TODO : find datasheet speed and pins

## Motors circuit

![Scheme motor](https://github.com/andybonnetto/Carderly/blob/main/Carderly_Motor/Schema%20branchements%20moteur.png?raw=false)


