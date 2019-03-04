# TowerOfHanoi
This is graphics solution of the [Tower of Hanoi](https://en.wikipedia.org/wiki/Tower_of_Hanoi).<br>
You graphics card must support OpenGL 3.3<br>
I used [LWJGL 3](lwjgl.org) in this project.
Sorry for my English.
# Using
## Command line options
First (optional) command line argument is an amount of disks. Default value is 4.<br>
Second (optional) command line argument is an amount of ticks for every movement.  Default value is 100.<br>
## Keys
You also can move in simulation with WSAD - horisontal movement and SPACE and LSHIFT - vertical movement.<br>
ESC close window. KEY_C restore default position. KEY_I changes glPoligonMode GL_FRONT_AND_BACK.
## Examples
<code>java -jar lwjgl3.jar 5 30</code><br>
will create solving for 5 disks and every movement will take 30 ticks.<br>
<code>java -jar lwjgl3.jar 5</code><br>
will create solving for 5 disks and every movement will take 100 ticks.<br>
<code>java -jar lwjgl3.jar</code><br>
will create solving for 4 disks and every movement will take 100 ticks.<br>
