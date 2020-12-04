# Carderly Motor
Carderly uses 5 motors to work which are 2x 360servo motors for the casters, 1x angle servo motor for the flipping system, 1x stepper motor for the push push arm and 1x DC motor for the main wheel. 

## TODO Motors
* <b>Discard all cards function</b> </br>
When the system breaks just discard all cards
## Motor launch
All motors can be launch by using the functions python file "call_motor.py" (workin on it), it calls subfunctions with classes for each type of motors, parameters and pins must be entered according to the scheme in the bottom.
At the end of every motor call, a *cleanup* function set in every class is necessary to disconnect motor from pins in order to let the energy for other motors. PINS refer to GPIO pins shown on scheme ([pin/GPIO relation](https://github.com/andybonnetto/Carderly/blob/main/Carderly_Motor/pins.PNG))
In the launch file : you can use
```
from call_motor import *

call_servo_angle()
call_servo_360(status) #status is either 'input' or 'output'
define_step_motor()
```
The Stepper motor will be called in the *shuffle.py* and *discard.py* that you can call this way
```
from call motor import *

shuffle(step_motor)
discard(step_motor,pos) #pos of the card to discard (quick np.where in the card saved list)
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

(!!NOT TESTED!!)
*TODO: find speed dependance on number of turn before testing and define speed ratio with PWM

### Stepper motor

## Motors circuit

![Scheme motor](https://github.com/andybonnetto/Carderly/blob/main/Carderly_Motor/Schema%20branchements%20moteur.png?raw=false)


