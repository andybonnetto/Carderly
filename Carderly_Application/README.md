Important files for the Android Studio project are in the app/source/main directory :
- Manifest.xml : where the activities (files) are declared
- the folder Res : all the ressources needed (images, strings, numbers, colors...)
- the activities, under the following path : CarderlyGame/CardDisplayTest/app/src/main/java/com/example/carderly/

## Login Activity
Get a name and password in input, checks if already exists in Database using Firebase connection. 
Send to activity Register when pressing the "register" button, or to the room activity when pressing the "login" button.
Passes the username to the Room Activity.

## Rooms Activity
Allows the user to create a room or join an existing one. Once the room is created and the player is in, the main activity is launched when 4 players are in the room.

## Main Activity
Creates the game layout when clicking on the start button and handle each player's hand through the database. Enable player to choose a card only when it's his/her turn.
