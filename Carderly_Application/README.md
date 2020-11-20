Important files for the Android Studio project are in the app/source/main directory :
- Manifest.xml : where the activities (files) are declared
- the folder Res : all the ressources needed (images, strings, numbers, colors...)
- the activities, under the following path : CarderlyGame/CardDisplayTest/app/src/main/java/com/example/carderly/

Run the "app" in android studio to run the whole project.

## Login Activity
Get a name and password in input, checks if already exists in Database under the Profiles section using Firebase connection. 
Send the user to the activity Register when pressing the "register" button, or to the room activity when pressing the "login" button.

Once done, passes the username to the Room Activity.

## Rooms Activity
Allows the user to choose his/her player ID (player 1,2,3,4).  (player1 is the old person)
Then, creates a room using the username given from the login activity. If one already exists, he/she can join it. The room instance is created in the Database, with the name of the room.
Once the room is created and the player is in, the main activity is launched when 4 players are in the room, giving the player ID in argument. In the database, each user is put inside their room.

## Main Activity
Creates the game layout when clicking on the start button. Handles each player's hand through the database, with their 8 cards and the last card played.
A variable is also created for the 1st player's turn and for the player currently playing.
Enable player to choose a card only when it's his/her turn.
