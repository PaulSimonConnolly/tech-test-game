Techincal test
----------------

Start server:
CD into the server/target and run "java -jar tech-challenge-server-1.0-SNAPSHOT.jar"

Start client:
Open two terminals, and in each run the following command to start the clients:
CD into client/target and run "java -jar tech-challenge-client-1.0-SNAPSHOT.jar localhost"

Note: Currently spent over 12 hours on the tech task and therefore I will stop working on it.
Criteria which should be working:

• The server application holds the state and business logic of the game, receiving the movements from the players and deciding whether a player has won, or the game is over - DONE
• The state of the game, and who’s turn it is, will be returned to the client upon request - DONE
• The communication between the clients and the server should be over HTTP. - Using sockets
• The server, upon start, waits for the two players to connect. If one of the players disconnects, the game is over. - DONE
• The client prompts the player to enter her name upon start, and displays whether it’s waiting for a 2nd player, or the game can start. - DONE
• On each turn, the client displays the state of the board and prompts the corresponding player for input or displays that it’s waiting for the other player’s input - DONE
• The client receives the input from the player from the standard input (stdin) - DONE
• The client displays when the game is over, and the name of the winner. - PARTLY DONE (No diagonal checking of board)

Missing:
• Unit tests
• Diagonal checking logic for winner
