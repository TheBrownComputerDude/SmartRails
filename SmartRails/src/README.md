CS 351
Project 3: SmartRails
Date: 4/5/2018

Description:
SmartRails is a basic train simulator program. Allows users to input
maps from TextFiles and allows them to move trains across the maps.
The Smart Rails game allows multiple trains to run at the same time
and coordinates trains so no collisions or over lap occur. A tool bar
is used to interact with program.

Tools:

    LoadMap:
        Loads in a selection of maps
    Send Train:
        Allows the user to select the departure and destination of the train.
        Note:
        If the route is not possible the train will not be made and start its
        route.

Map Making:

Max supported size is an 8x8 map
The map must be placed in the "Maps" folder

S or s: Makes a station

R or r: Makes a rail

L or l: makes a light

Everything else will be interpreted as a switch.
Note: lights must be added
Valid Switchs Examples:

    Ex1:rr1r
        r1rr

    Ex2:r23r
        2rr3
Invalid Switches Examples:

    Ex1:rr11r

    Ex2:r1rr
        r1rr

Work Break Down:

    Steven Chavez:
        Back end threading and SmartRails backend
    Manfred Hayes:
        Front End GUI and file reading/management

Bugs:
If you spam too many trains the image will freeze

Main File: SmartRailsGUI.java