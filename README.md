# HouseControl
Small project to control a raspberry pi connected to an arduino over wifi (using an android app and a python server)

In my adventures in home automation, I have been using a raspberry pi to interface with an arduino. The arduino output controls a USB hub to turn on/off power to it. The USB hub is connected to several USB powered lights around my room.

This project contains the code for the python script running as a server on the Raspberry Pi, which can be commanded to send signals to the arduino. The android app in this project is able to command the python server over wifi (provided the IP is set in the java file)

## Todo
* Authentication (currently none, apart from WPA2 to connect to the router)
* Decentralisation (to send a command to the appropriate raspberry pi if there are many in the house, and so the IP need not be hard-coded)
