
## API Testing Commands

### Vehicle API Endpoints

**1. Create a Vehicle (POST)**  
Creates a new Tesla Model Y in the database.
```bash
curl -X POST http://localhost:8080/vehicle \
  -H "Content-Type: application/json" \
  -d '{
    "vin": "TEST_VIN_001",
    "manufacturerName": "Tesla",
    "description": "Electric SUV",
    "horsePower": 384,
    "modelName": "Model Y",
    "purchasePrice": 54990.00,
    "fuelType": "ELECTRIC"
  }'
```
Creates a new Toyota in the database.
```bash
curl -X POST http://localhost:8080/vehicle \
     -H "Content-Type: application/json" \
     -d '{
           "vin": "TEST_VIN_002",
           "manufacturerName": "Toyota",
           "description": "Reliable Sedan",
           "horsePower": 150,
           "modelName": "Camry",
           "purchasePrice": 25000.00,
           "fuelType": "GASOLINE"
         }'
```

**2. Get All Vehicles (GET)**  
Retrieves a list of all vehicles.
```bash
curl -v http://localhost:8080/vehicle
```

**3. Get Specific Vehicle (GET)**  
Fetch car using VIN.
```bash
curl -v http://localhost:8080/vehicle/TEST_VIN_001
```

**4. Update Vehicle (PUT)**  
Update price and description for vehicle.
```bash
curl -v -X PUT http://localhost:8080/vehicle/TEST_VIN_001 \
     -H "Content-Type: application/json" \
     -d '{
           "vin": "TEST_VIN_001",
           "manufacturerName": "Tesla",
           "description": "Updated Description: Long Range AWD",
           "horsePower": 384,
           "modelName": "Model S",
           "purchasePrice": 49990.00,
           "fuelType": "ELECTRIC"
         }'
```

**5. Delete Vehicle (DELETE)**  
Remove vehicle record from database.
```bash
curl -v -X DELETE http://localhost:8080/vehicle/TEST_VIN_001
```

### Exception Handling Tests

**1. Test Validation Failure (422 Unprocessable Entity)**  
Sending a vehicle with invalid data to trigger MethodArgumentNotValidException.  
Expect 422 Unprocessable Entity with a map of field errors.
```bash
curl -v -X POST http://localhost:8080/vehicle \
-H "Content-Type: application/json" \
-d '{
"vin": "INVALID-DATA-VIN",
"manufacturerName": "Unknown",
"modelName": "FailMobile",
"description": "This should fail validation",
"horsePower": -100,
"purchasePrice": -5000.00,
"fuelType": "Gasoline"
}'
```
**2. Test Malformed JSON (400 Bad Request)**  
Sending a JSON body with a syntax error (missing closing brace) to trigger HttpMessageNotReadableException.  
Expect 400 Bad Request with message "Malformed JSON request".
```bash
curl -v -X POST http://localhost:8080/vehicle \
     -H "Content-Type: application/json" \
     -d '{
           "vin": "BROKEN-JSON-VIN",
           "manufacturerName": "Toyota",
           "description": "Whoops, I forgot the closing brace...
```

**3. Test Business Conflict (409 Conflict)**  
Trying to create a Vehicle that already exists to trigger IllegalArgumentException.  
Expect 409 Conflict with the error message.

```bash
curl -X POST http://localhost:8080/vehicle \
-H "Content-Type: application/json" \
-d '{"vin": "DUPLICATE-VIN", "manufacturerName": "Honda", "modelName": "Civic", "horsePower": 150, "purchasePrice": 20000.00, "fuelType": "Gasoline"}'
curl -v -X POST http://localhost:8080/vehicle \
-H "Content-Type: application/json" \
-d '{"vin": "DUPLICATE-VIN", "manufacturerName": "Honda", "modelName": "Civic", "horsePower": 150, "purchasePrice": 20000.00, "fuelType": "Gasoline"}'
```

