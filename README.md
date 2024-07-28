# World1-6TrafficLights

## Commands

### Create Commands
- **/trafficlight create system \<Name\> \<Type\>**  
  Creates a new traffic light system.  
  Example: `/trafficlight create system mainroad TWO_LANE_ROAD`

- **/trafficlight create junction \<Name\> \<Int\> \<isTurningJunction\>**  
  Creates a new junction in an existing traffic light system.  
  Example: `/trafficlight create junction mainroad 1 true`

- **/trafficlight create light \<Name\> \<Junction\> \<Int\> \<isLeft\>**  
  Creates a new traffic light in a junction.  
  Example: `/trafficlight create light mainroad 1 1 true`

### Delete Commands
- **/trafficlight delete system \<Name\>**  
  Deletes an existing traffic light system.  
  Example: `/trafficlight delete system mainroad`

- **/trafficlight delete junction \<Name\> \<Junction\>**  
  Deletes a junction from a traffic light system.  
  Example: `/trafficlight delete junction mainroad 1`

- **/trafficlight delete light \<Name\> \<Junction\> \<Int\>**  
  Deletes a traffic light from a junction.  
  Example: `/trafficlight delete light mainroad 1 1`

### Other Commands
- **/trafficlight tick \<Name\>**  
  Manually ticks a traffic light system.  
  Example: `/trafficlight tick mainroad`

- **/trafficlight list systems**  
  Lists all existing traffic light systems.  
  Example: `/trafficlight list systems`

- **/trafficlight list junctions \<Name\>**  
  Lists all junctions in a specified traffic light system.  
  Example: `/trafficlight list junctions mainroad`

- **/trafficlight list lights \<Name\> \<Junction\>**  
  Lists all traffic lights in a specified junction.  
  Example: `/trafficlight list lights mainroad 1`

## Permissions
- **world16.trafficlight**  
  Required to use the `/trafficlight` commands.