# Classroom Management App

## Goals and features
Classroom Management App is an app allowing signed in teachers to book a classroom for a specific time period of maximum 24 hours. Teachers can also add and edit classrooms in the system.

## Usage
Start the application using a physical or virtual Android 10.0+ (API 30) device. 

Available settings:
- Toggle night mode: Toggle the application theme to a more comfortable one for low light use.
- Change password: Providing your actual password, update your password to a new one.
- Use U.S. date and time format: Change date format to "MM/DD/YYY" and time format to "hh:mm am/pm"
- Delete my account: Erase all your data in the application (irreversible operation).
               
The information is only stored locally on your device.

You can log out from the classroom lists screen.

Sample users:
- email: nathan@mail.ch password: 123
- email: benjamin@mail.ch password: 123

## Technical features
- Room database with three full CRUD tables
- Time dimension reservation management
- SHA-512 hashed and salted password management system
- Time dynamic greetings

## Troubleshooting
- My app behaves strangely on the first use:
    The room database may be created on the first access, that's why the behavior can seem strange. This does not impact the use of the app.

## Authors and contributors
Authors: B. Biollaz, N. Gaillard

Contributors: Y. Pannatier
## Language
Java 11

## Licence
Developed in academic context at HES-SO for educational purpose only
