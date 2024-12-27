# PROFIT_API

The API Application has two sets of endpoints: `ExternalApiController` and `EmployeeController`.
The first set is designed for fetching and posting data to/from an external API (vendor).
A fake API with a made-up URL and credentials is implemented to simulate the external API.
The code demonstrates a workflow for obtaining and pushing data from/to the vendor API,
and storing to the internal database.
The second set of endpoints is used for manipulating the internal database.
All connections require authentication using basic auth.
The credentials are: username: `user`, password: `password`.

ExternalApiController:
GET http://localhost:8080/vendorData/{id}   get records for certain employee from vendor
POST http://localhost:8080/vendorData       push data to vendor

EmployeeController:
GET http://localhost:8080/employee      get all the employees
POST http://localhost:8080/employee     push a new employee into internal DB
PUT  http://localhost:8080/employee     update employee
DELETE  http://localhost:8080/employee/{id}
