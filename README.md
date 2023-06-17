# FlixBus API

FlixBus API is a Java application that allows obtaining and transmitting information about buses, both owned and rented, as well as information about routes they operate on. It provides functionality to manage buses, routes, and related data.

## Functions
- Create, update, and delete buses and routes
- Read data from CSV files
- Write data to CSV files
- Retrieve information about buses, routes, and their details
- Calculate ticket prices between adjacent stops on a route
- Retrieve the total ticket price for an entire route
- Retrieve the distance between stops and the total distance of a route

## Technologies Used
- Java
- Spring Framework
- Maven

## Installation and Usage
To run this project locally, please follow these steps:

1. Clone the repository: `git clone https://github.com/your-username/flixbus-api`
2. Open the project in your preferred IDE.
3. Build the project using the command: `mvn clean install`.
4. Run the application with the command: `mvn spring-boot:run`.
5. The application will start running on `http://localhost:8080`.
6. Use the following endpoints to interact with the API:

   - Retrieve a list of all buses: `GET /buses`
   - Retrieve a bus by ID: `GET /buses/{id}`
   - Create a new bus: `POST /buses`
   - Update an existing bus: `PUT /buses/{id}`
   - Delete a bus: `DELETE /buses/{id}`

   - Retrieve a list of all routes: `GET /routes`
   - Retrieve a route by ID: `GET /routes/{id}`
   - Create a new route: `POST /routes`
   - Update an existing route: `PUT /routes/{id}`
   - Delete a route: `DELETE /routes/{id}`

   Additional endpoints for calculating prices and retrieving route details can be implemented as per your requirements.

## Data Storage
The application stores data in CSV files located in the `src/main/resources` directory. The relevant CSV files for buses and routes are expected to be present in this directory.

Please make sure to set up and configure the CSV files accordingly to ensure proper data storage and retrieval.

Feel free to reach out if you have any further questions or need assistance with the FlixBus API.
