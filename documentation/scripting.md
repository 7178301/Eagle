# Scripting Interface

This API comes with some basic scripting functionality. The available commands are listed here as a reference. All commands are case insensitive and largely match the SDKAdaptor functionality.

|Command|Example|Description|
|GETPOSITION|GETPOSITION|prints out the drones current position|
|DELAY|DELAY _time_| Delays for _time_ milliseconds|
|GOHOME|GOHOME|Flys the drone to its home position|
|FLYTORELATIVE|FLYTORELATIVE _longitude_ _latitude_ _altitude_ _roll_ _pitch_ _bearing_ _[speed]_|Fly the drone to a given relative position|
|FLYTOABSOLUTE|FLYTOABSOLUTE _longitude_ _latitude_ _altitude_ _roll_ _pitch_ _bearing_ _[speed]_|Fly the drone to a given gps position|

More commands will be added over time.
