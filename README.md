# Contact-Management-System
This is a public repo for a management software for phone contacts

## Description
This service is based on a REST API implemented with Java Spring Boot and a FE built with Angular 15+. Authentication follows OAuth2 protocol and it is handled by a Keycloak external instance. All the service has been containerized through Docker Compose.

## Setup
Start the service with the following command:
```
docker compose up -d
```
Once all services are up, Keycloak instance must be configured properly creating realms, clients and users used by BE for authenticating calls. They can be served as env variables in Docker Compose file and they have to match in application.yml file (in *backend* folder).

## Design decisions

### Backend
Each contact is represented through a DTO having name, surname and phone number. At data access layer, the information about the creator of the contact is placed inside the *user_id* column that matches the Keycloak's JWT subject attribute. In order to have a better hanndling of HTTP errors a GlobalExceptionHandler class has been implemented to map custom exceptions to real HTTP statuses. All CRUD APIs  have been secured using the *User* role (a custom one defined on Keycloak), except for one, which is dedicated to admin users (e.g. users with *Admin* role on Keycloak), that returns contact pagination along with creator's user id.

### Frontend
The service presents an initial login form under */login* path and any other route redirects to /login in case the caller is not logged in yet. All forms are built using **FormGroups** that provide built-in validation upon definition of specific validators (login form has the one for email format and password length).
After logging in, the user will see its username in the top-right corner right next to **Logout** button; under that there is a toolbar to create, edit or delete a contact (edit and delete buttons won't be enabled unless a specific contact has been selected by clicking on it).
When creating a new contact a form will appear. The same component is used when editing a contact; in that case, the form fields will be pre-filled with information available for that contact. 