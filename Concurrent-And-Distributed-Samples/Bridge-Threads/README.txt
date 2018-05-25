Bridge Problem
- Vehicles/Drivers want to cross the bridge going east to west or west to east
- The bridge is observed having only one lane of traffic
- ie. cars only travel one way while on the bridge, forcing cars in the other direction to wait

Main functions in the vehicles/Bridge problem

CheckBridge  - Helper function to check whether the bridge is safe to enter
ArriveBridge - Cars arrive at the bridge (cross the bridge or wait)
CrossBridge  - Print statement showing current car and crossing direction with sleep timer
ExitBridge   - Show car has exited bridge with print statement and signal appropriate car to follow

bridge.c
    - Policy is suitable when traffic volume is relatively low
    - When a vehicle exits the bridge, a waiting vehicle in the same direction is allowed to enter/travel
    across the bridge
    - Waiting vehicles travelling in the opposite direction may only travel on the bridge if there are no waiting
    vehicles traveling in same direction as the exiting vehicle
    - ie. 5 vehicles currently traveling east, 2 vehicles waiting to travel east, 3 vehicles waiting to travel west.
    The 2 vehicles traveling east would go next, and once all vehicles traveling east are finished exiting the bridge,
    then the 3 vehicles traveling west would cross the bridge.

bridge-rush.c
    - This file handles rush hour conditions: Implementing a fair traffic control policy during a theoretical rush hour
    - Given high traffic load, impose a limit of cars that can cross the bridge from one direction (6)
    - After given number of cars (6) have crossed the bridge, or number of cars waiting in current direction has
    reached zero, then change directions for passing/waiting cars
    - ie. 6 cars traveling east, 5 cars waiting to travel east, 7 cars waiting to travel west,
    After 6 cars are done traveling east, 6 cars (out of 7) will be allowed to travel on the bridge going west. The 5
    cars traveling east will be allowed to cross, and after they have all crossed, the 1 car left traveling west
    will be allowed to cross the bridge