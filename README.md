# logTail
Client can tail log file on remote server without using ssh

# How to run
Make sure firewalls are not blocking the socket connection.
This code connects localhost server at port 80. So, make sure server is up at localhost.

There are 2 separate modules- client and server, each having its own main method.
1. First start the main method in server file named ServerLogHandler.java using cli or any ide
  a. using cli-
    go to the directory server, then give command
    1. javac -cp ../../ ServerLogHandler.java
    2. java -cp ../../ ServerLogHandler.java absolute_path_log_file true
    In second command, first argument is the absolute path of the log file which has to be tailed. Pls ensure it exists.
    The second argument is a boolean true/false, which tells whether to auto update file with random data at frequent          interval.
    
 2. Now start the client 
   using cli-
   go to the directory client, then run 
   1. javac LogTailClient.java
   2. java LogTailClient.java


Post this, in the client tab you shall see logs are tailing.
