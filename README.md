## Sample project creating PersonREST HTTPS API with Spring Boot and Angular JS.

### To run this application use:

* mvn spring-boot:run
 

### Rest Api hosted and urls:

Go to `https://localhost:8443` 

* POST request to `/api/v1/persons/` with a "object" object as JSON creates a new Person;
* GET request to `/api/v1/persons/` returns a list of persons;
* GET request to `/api/v1/persons/1` returns the persons with ID 1;
* PUT request to `/api/v1/persons/3` with a "object" object as JSON updates the person with ID 3;
* DELETE request to `/api/v1/persons/4` deletes the person with ID 4;
* DELETE request to `/api/v1/persons/` deletes all the persons.

