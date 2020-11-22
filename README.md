# parkinglot
Parking Lot API

Parking Lot is a simple application for calculating parking fee for parked car. Parking Lot product supports the following functions.

* Managing the parking areas
* Manage Vehicles
* Check in/out (park)

You can find Swagger Documentation of API under the path http://localhost:8080/swagger-ui/.
Also source code of Swagger UI Html is added to this repository.

Sample requests:
POST
localhost:8080/parkingLot/createParkingArea

{
    "name" : "BALPARK",
    "capacity" : 3,
    "city" : "Balikesir",
    "vehicleCount" : 0,
    "prices" : [
        {
            "startHour" : 0,
            "endHour"   : 10,
            "value"     : 12
        },
        {
            "startHour" : 10,
            "endHour"   : 20,
            "value"     : 18
        },
        {
            "startHour" : 20,
            "endHour"   : 24,
            "value"     : 21
        }
    ]
}
