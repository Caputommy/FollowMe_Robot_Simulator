# FollowMe Robot Simulator

This is a simple 2D simulation environment for programmable moving robots,
developed as a project for the Programming Methodologies exam at [UNICAM](http://www.unicam.it/).

This application can run a simulation based on: 
- An environment, made of two-dimensional labeled areas; 
- A program made of instructions followed by all robots in the simulation.

## Building and running

To build the program, first open a terminal window in the main folder of the project,
then run the `build` command using the correct version of the Gradle wrapper
based on your OS:

    gradlew.bat build   (on Windows)    
    gradlew build       (on macOS/Linux)

After building, execute the Gradle wrapper `run` command to launch the application.

    gradlew.bat run   (on Windows)    
    gradlew run       (on macOS/Linux)

## Application usage

Once the application has been successfully launched, the main window of the
app will be shown, containing the following main sections:

- The simulation view, composed of the simulation time stopwatch and the 
two-dimensional cartesian plane.
- The program source code text box on the left.
- The control panel on the bottom.

Here's a brief explanation of the commands available in the control panel:

### Settings section

| Command                                                       | Usage                                                              |
|---------------------------------------------------------------|--------------------------------------------------------------------|
| ![Load environment button](/readmeImages/LoadEnvironment.png) | Loads a .txt file from file system as simulation environment.     | 
| ![Load program button](/readmeImages/LoadProgram.png)         | Loads a .txt file from file system as robots' program.             |
| ![Instruction interval setter](/readmeImages/SetPaceTime.png) | Sets the time interval between the execution of each instruction.  |

Note that modifying any of these settings will cause the current 
simulation to reset, along with the stopwatch timer (all robots 
added to the current simulation will be moved to the new one).

### Robots section

| Command                                                       | Usage                                                                |
|---------------------------------------------------------------|----------------------------------------------------------------------|
| ![Add robots button](/readmeImages/AddRobots.png)             | Opens a menu window for adding new robots to the current simulation. | 
| ![Clear robots button](/readmeImages/ClearRobots.png)         | Removes all robots currently added into the simulation.              |

The Add Robots menu window is composed of two sections with the
following functionalities:

- Adding single robots with a given movement configuration in a 
specific position on the plane.

- Adding a given amount of robots in positions randomly chosen
among coordinate ranges, all of them having a given movement 
configuration.

Note that is possible to add robot in both ways at the same time
using the "Add robots" button according to the given information.
<br/> All fields are manually editable.

### Simulation section

| Command                                                | Usage                                                                                                                                                              |
|--------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| ![Play seconds button](/readmeImages/PlaySeconds.png)  | Runs the current simulation for the given amount of seconds. (with or without real-time animation)                                                                 | 
| ![Play steps button](/readmeImages/PlaySteps.png)      | Runs the current simulation for the given amount of steps. (with or without real-time animation)                                                                   |
| ![Play one step button](/readmeImages/PlayOneStep.png) | Runs the current simulation until the next instruction is executed. Note that this is equivalent to the previous command used with "1" step and no animation. |

All fields are manually editable.

### View section

| Command                                          | Usage                                               |
|--------------------------------------------------|-----------------------------------------------------|
| ![Move view buttons](/readmeImages/MoveView.png) | Scrolls the simulation view in the four directions. | 
| ![Zoom view slider](/readmeImages/ZoomView.png)  | Increases or reduces the zoom factor.               |

## File formats

### Environment

To describe the environment of the simulation, a .txt file containing lines 
of the following format is used:

| Syntax                     | Described area                                                                                                                     |
|----------------------------|------------------------------------------------------------------------------------------------------------------------------------|
| _label_ CIRCLE _x_ _y_ _r_          | A circular area labeled with _label_, having its center sitting in position (_x_, _y_), with radious _r_ (meters).                 | 
| _label_ RECTANGLE _x_ _y_ _w_ _h_ | A rectangular area labeled with _label_, having its center sitting in position (_x_, _y_), with width _w_ and height _h_ (meters). |

### Program

The robots' program is composed of a sequence of instructions and iteration constructs.
Such programming language includes the following commands:

- Instructions

| Syntax                     | Semantics                                                                                                                                                                                                                                                                                                                                                                                                                                  |
|----------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| MOVE _x_ _y_ _s_           | Sets the robot's movement configuration in the direction associated to the relative position (_x_, _y_) and setting its velocity to _s_ (m/s).                                                                                                                                                                                                                                                                                             | 
| MOVE RANDOM _x1_ _x2_ _y1_ _y2_ _s_ | Sets the robot's movement configuration in the direction associated to a random absolute positon having its coordinates in ranges  [_x1_, _x2_] and [_y1_, _y2_] and setting its velocity to _s_ (m/s).                                                                                                                                                                                                                                    |
| CONTINUE _s_               | The robot continues moving according to its configuration for _s_ simulation steps.                                                                                                                                                                                                                                                                                                                                                        |
| STOP                       | The robot stops its motion, setting its velocity to 0 (m/s).                                                                                                                                                                                                                                                                                                                                                                               |
| SIGNAL _label_             | The robot start signaling a condition described by _label_.                                                                                                                                                                                                                                                                                                                                                                                |
| UNSIGNAL _label_           | The robot stop signaling a condition described by _label_.                                                                                                                                                                                                                                                                                                                                                                                 |
| FOLLOW _label_ _dist_ _s_  | Sets the robot's movement configuration in the direction associated to the absolute position obtained as an average of positions of robots that are currently signaling a _label_ condition and are not further than _dist meters from the robot. If no such robot exists, a direction obtained from a random relative position in ranges [-_dist_, _dist_] and [-_dist_, _dist_] is set. In any case, sets the robot velocity to _s_ (m/s). |

- Iteration constructs

| Syntax                                                  | Semantics                                                                                                       |
|---------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------|
| REPEAT _n_<br/> cmd1 <br/> ... <br/> cmdn <br/> DONE    | Repeats the sequence of instructions cmd1, ..., cdmn **for _n_ times**.                                         | 
| UNTIL _label_<br/> cmd1 <br/> ... <br/> cmdn <br/> DONE | Repeats the sequence of instructions cmd1, ..., cdmn **until the robots reaches an area labeled with _label_**. |
| DO FOREVER <br/> cmd1 <br/> ... <br/> cmdn <br/> DONE   | Repeats the sequence of instructions cmd1, ..., cdmn **forever**.                                               |


Note that every area label and robot's condition label must be composed 
only of alphanumerical characters and "_" character.

Sample files for both environment and program are respectively available 
in the following paths internal to the project:

    app/src/test/samples/environmentSamples
    app/src/test/samples/programSamples







