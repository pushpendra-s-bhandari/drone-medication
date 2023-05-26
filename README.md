
# Drones Medical Transportation System

This is a transportation system to manage medication transportation through Drones so that medical items can be urgently delivered in locations with difficult access.

## Table Reference
#### drone 
This table is used for drone storage.
#### dronebattery_level
This table is used for history/audit log of Drone battery levels.
#### medication
This table is used for medications storage
#### transportation
This table is used to record each transportation of medications done with drones.


## Dummy Data

This application loads Dummy Data for Drones and Medications during the startup of the application using data.sql file. If we want to insert any new record for drones and medications, respective rest end points are available as mentioned in the API Reference section below.

## API Reference

#### Register a Drone

```http
  POST /transportation/registerdrone                           
```
Input JSON:

{
   "serialNumber": "drone1",
   "model": "Middleweight",
   "weightLimit": 500,
   "batteryCapacity": 100,
   "state": "IDLE"


}


#### Register a Medication

```http
  POST /transportation/registermedication
```

Input JSON:

{
    "code" : "SINEX",
    "name": "Sinex Syrup",
    "weight": 45
}

#### Upload  a Image of the Medication

```http
  POST /transportation/uploadmedicationimage
```
Input Parameter (as Form Data)
:

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `code` | `String` | Code of the medication |
| `image` | `File` | Image of the medication |

The upload directory has been defined in application.properties file through following property

medication.image.path=D:/uploads/medications/

#### Get List of available drones for Loading

```http
  GET /transportation/getdronesforloading
```

No Any Input Parameter:
It will always check for Drones whose state is "IDLE"

#### Get List of available drones based on State.

```http
  GET /getdronesbystate/{state}
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `state` | `String` | State of the drone |

#### Get Battery level for a drone

```http
  GET /getdronebatterylevel/{serialNumber}
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `serialNumber` | `String` | serialNumber of the drone |

#### Load a drone with Medications

```http
  POST /transportation/loaddroneswithmedications
```
Input JSON:

{
   "serialNumber": "drone1",
   "description": "transportation 1",
   "loadingTimeInMinutes": 1,
   "medications": [ "SINEX", "NIKODS", "PARACET"]
}

Here, parameter "loadingTimeInMinutes" indicates the loading time of medications into the drone. During this time, the drone's state is changed to "LOADING" so that no other request to load medications to the same drone can be done. Drone with state "IDLE" can only be loaded with Medications and once medications has been loaded, the drone's state is moved to "LOADED". Medication are not loaded if battery level of the drone is below 25 % and the total weight of the medications is greater than the actual capacity of the drone that it can carry. This will create a new record on table 'transportation' with all the medications items. This table has been introduced to store all the transportation records so that we can record all the transportation happened and can track and retrieve in later phase based on drone, loaded date, delivered date etc.

#### Get all the medications loaded for a drone.

```http
  POST /getloadedmedications/{serialNumber}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `serialNumber` | `String` | serialNumber of the drone |

This api will only get only those medications from a drone which has been Loaded meaning whose state is "LOADED".

## Unit Testing Reference

Test cases for Service "DroneService.java" has been done whereas few rest end points have been unit tested from Controller Class.

## CRON JOB

There is a cron job scheduled which runs every 1 hour to check the battery level of all the drones and create history/audit event log for each of the drones. This log has been recorded in table 'drone_battery_level"
## Database

This system is currently using H2 Database which is a relational in-memory database. The console of the H2 database can be accessed by using following url

http://localhost:8080/h2-console/login.jsp
## Spring Boot and Java Version

Spring Boot Version and Java versions used were 3.0.6 and 17 respectively
## Dependencies

Following dependencies where used for this project

1. Spring Data JPA
2. H2 Database
3. Spring Web
4. Validation
5. Lombok


## Building and Running Project

Build : mvn package
This will build an executable jar file in the target directory.

Run: java -jar drone-medication-0.0.1-SNAPSHOT.jar