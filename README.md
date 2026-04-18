# Weather Dashboard
A simple web application that displays current weather conditions for cities around the world. Authenticated users can browse a dashboard of weather cards or view detailed information for a specific city.

@ Features
-View current weather for 20 cities around the world
-View detailed weather information for a specific city
-User registration and login
-Protected routes — only authenticated users can access weather data

# Tech Stack
-Backend — Java, Spring Boot, Spring Security
-Database — PostgreSQL
-Frontend — HTML, CSS, JavaScript (no frameworks)
-Weather Data — WeatherAPI

# How It Works
Weather data is automatically fetched from the WeatherAPI every hour for all 20 cities and stored in the database. Data older than 30 days is automatically deleted every night at midnight. This means the app always serves fast, up-to-date data from the database rather than hitting the external API on every request.
