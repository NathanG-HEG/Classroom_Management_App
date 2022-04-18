# Classroom Management App

## Goals and features
Classroom Management App is an app allowing signed in teachers to book a classroom for a specific time period of maximum 24 hours. Teachers can also add and edit classrooms in the system. 
Users who don't verify their e-mail address can only consult reservations and available classrooms.

## Usage
Start the application using a physical or virtual Android 10.0+ (API 30) device connected to the internet. 

Available settings:
- Toggle night mode: Toggle the application theme to a more comfortable one for low light use.
- Change password: Providing your actual password, receive a reset password link by e-mail.
- Use U.S. date and time format: Change date format to "MM/DD/YYY" and time format to "hh:mm am/pm"
- Delete my account: Erase all your data in the application (irreversible operation).
               
The information is stored on a Firebase Realtime Database.

You can log out from the classroom lists screen.

Sample users:
- email: nathan@mail.ch password: 12345678 (unverified)
- email: biollazgaillard@gmail.com password: 12345678 (verified)

## Technical features
- Firebase Realtime Database with three full CRUD tables
- Time dimension reservation management
- E-mail verification mechanism
- Time dynamic greetings

## Troubleshooting
- I can't create a classroom, create or edit a reservation, even though I clicked the verification link.
  
    Log out and log in to solve this issue. This is due to a Firebase Authentication limitation.
  
- I don't receive a verification link.
  
    Most mail servers mark Firebase e-mail as spam. Check your spam inbox regularly, wait for your e-mail server to release the e-mail. Gmail usually does not mark Firebase e-mail as spam, try using a gmail.com address.

## Authors and contributors
Authors: B. Biollaz, N. Gaillard

Contributors: Y. Pannatier
## Language
Java 11

## Licence
Developed in academic context at HES-SO for educational purpose only
