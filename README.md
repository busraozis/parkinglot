# parkinglot
Parking Lot API

Parking Lot is a simple application for calculating parking fee for parked car. Parking Lot product supports the following functions.

* Managing the parking areas
* Manage Vehicles
* Check in/out (park)

You can find Swagger Documentation of API under the path http://localhost:8080/swagger-ui/.
Also source code of Swagger UI Html is added to this repository.

Sample requests:
1) POST
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

2) DELETE
localhost:8080/parkingLot/deleteParkingArea/1

3) GET
localhost:8080/parkingLot/getParkingAreaByName/BALPARK

4) POST
localhost:8080/parkingLot/createVehicle
{
    "plate" : "34M01",
    "type"  : "MINIVAN"
}

5) POST 
localhost:8080/parkingLot/checkin
 {
        "checkIn" : "2020-11-20T08:25:07.524",
        "parkingAreaId" : 2,
        "vehicle" : {
            "plate" : "34KL00",
            "type" : "SUV"
        }
}

6) POST
localhost:8080/parkingLot/checkout
 {
        "id" : 1,
        "vehicleId" : 2,
        "checkOut" : "2020-11-20T15:10:07.524"
}

7) GET
localhost:8080/parkingLot/getDailyIncome/parkingArea/1/date/2020-11-20

8) POST
localhost:8080/parkingLot/updateParkingArea
{
    "id"   : 1,
    "name" : "BALPARK",
    "capacity" : 3,
    "city" : "Balikesir",
    "vehicleCount" : 0,
    "prices" : [
        {
            "startHour" : 0,
            "endHour"   : 20,
            "value"     : 25
        },
        {
            "startHour" : 20,
            "endHour"   : 24,
            "value"     : 29
        }
    ]
}




