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
call_servo_360(status) #status is either 'input' or 'output' to change the pin of the motor selected
call_dc() #do the back and forth movement of the arm
```
The Stepper motor will be called in the *shuffle* and *discard* functions that you can call this way
```
from call motor import *

step_motor = define_step_motor() #the class needs to be declared outside so we remember the current position of the wheel (other classes are declared inside)
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
360 servo motors are used to turn the small casters in the input and output of the device. You must specify input or output in order to choose the pin to activate. For now the run at full speed during a constant period (magic number in call_motor). 

in call_motor : 
```
from Motor_servo_360 import Servo360

 call_servo_360(status = 'input'):
    motor_1 = Servo360(PIN1)
    motor_2 = Servp360(PIN2)
    if status == 'input':
        servo_motor360.activate(period,dc=100)
    elif status == 'output':
        servo_motor360.activate(period,dc=100)
    servo_motor360.cleanup()
```
Max speed is already quite low (1 turn per second) so no need to regulate with PWM

### Stepper motor
Stepper motor controls the main Wheel, it is divided into 32 positions each separated by an irregular number of steps (6,7,6,6,6,7,6,6...). The calculation of steps needed to go to a certain position is already calculated in the *calculate_step_pos* function as well as the calculation for the quickest direction in *find_dir*. Anyway, the motor is a Nema17 which is a 100 steps per rotation motor but we are using the half-step sequence mode which allow us a precision of 1.8° per step. The maximum error of the motor for some of the positions is of 0.9°.

The stepper needs to be declared first, the variable will keep the current position of the wheel memory (integer between 0 and 31).
```
step_motor = define_step_motor()
```
Then there is 2 cases which are either the shuffling, which goes only to the next position, it should be called in a for loop with the 32 positions,
```
shuffle(step_motor)
```
either the discard which goes to a specified position.
```
discard(step_motor,pos) #pos is an int in range(31)
```

## Motors circuit

![Scheme motor](https://github.com/andybonnetto/Carderly/blob/main/Carderly_Motor/Schema%20branchements%20moteur.png?raw=false)


