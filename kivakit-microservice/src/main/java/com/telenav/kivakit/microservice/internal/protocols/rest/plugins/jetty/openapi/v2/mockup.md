
## Mockup of DrivingSafetyIncidentsInCircle

 - external schemas are placed in `api/schemas` relative to the microservice class
 - type: object is assumed

### Location.schema

```
description: "A location on the surface of the Earth"  
properties:  
  latitude:  
    type: double  
    description "Latitude in degrees"  
  longitude:  
    type: double  
    description "Longitude in degrees"  
```
    
--------

### Distance.schema

```
description: "A distance in any unit"  
properties:  
  distance:  
    type: string  
    description: "Distance in any unit, like 6 feet, 4.5 miles or 15 meters"
```

--------

### DrivingSafetyIncidentsInCircleRequest

 - the name of each property corresponds with the field
 - the type of the property is used to look up the schema
   - the schema for `Location` is external to the project, so its schema is defined above
   - the schema for `DrivingSafetyIncident` is defined in the corresponding class
 - types do not inherit properties from their supertype so everything is visible in one place

```
@OpenApiType
(
    """
    type: object
    description: "A request for driving safety incidents within a given circle"
    properties:
      center:
        required: true
        description: "The center of the requested circle"
      radius:
        required: true
        decription: "The radius of the requested circle"
    example:
      center:
        latitude: 45.0
        longitude: -105.0
      radius:
        distance: "400 meters"
    """,
)
public class DrivingSafetyIncidentsInCircleRequest extends BaseDrivingSafetyIncidentsRequest
{
    private Location center;

    private Distance radius;
```

--------

### DrivingSafetyIncidentsResponse

 - the type DrivingSafetyIncident is defined in that class

```
@OpenApiType
(
    """
    description: "The response to a DrivingSafetyIncidentRequest"
    properties: 
      incidents:
        type: array
        required: true
        description: "List of driving safety incidents"
        items:
          type: DrivingSafetyIncident
    """
)
public static class DrivingSafetyIncidentsResponse extends BaseMicroservletResponse
{
    private List<DrivingSafetyIncident> incidents;

    
```


--------
 
```
@OpenApiType
(
    """
    description: "
    properties: 
      utcDayOfWeek:
        required: true
        description: "The day of the week (monday = 1) in UTC when the accident happened"
      description:
        required: true
        description: "Description of the incident (for debugging and visualization)"
      utcHourOfDay:
        required: true
        description: "The hour of day in UTC when the accident happened"
      location:
        required: true
        description: "The location of the incident"
      
    """
)
public class DrivingSafetyIncident
{
    private int utcDayOfWeek;

    private String description;

    private int utcHourOfDay;

    private Location location;
```
