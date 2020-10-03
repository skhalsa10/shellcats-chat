# Lab1: Client/Server Model
The purpose of this lab is to create a chat application with a multi-threaded server that can handle multiple clients 
who wish to chat to one another. Our group decided to use Java for our programming language.

## Students
Siri Khalsa, Lawrence Leung, Michelle Louie, Soheila Jafari Khouzani

### Usage
**JRE 10.0.2 MUST BE USED**
Zip files for this can be found in the "jre_downloads" folder -- Windows and Linux 
versions are included.

The link to download jre 10.0.2 is
- https://www.oracle.com/java/technologies/java-archive-javase10-downloads.html
- **Note: **  An account must be created before you can download 

There are 3 jar files in the repo under the folder "jars":
1. ChatCLI.jar -- used for research purposes
2. ChatGUI.jar
3. ChatServer.jar

### To Run Server
`java -jar ChatServer.jar <hostname> <port number>`

- `<hostname>` is the server's hostname
- `<port number>` is the server's port

### To Run Client GUI Version
`java -jar ChatGUI.jar`

1. In the GUI, type in a username, the server hostname, and the server port. 
Press `Enter` or click on the `Log In` button.
- **Note:** If the client cannot connect to the specified server, the client will close down.
- **Note:** If the input username already exists, the client will also exit.
2. Specify the recipient at the top by typing the recipient's username.
3. Type your message at the bottom and click the `send` button or press `Enter` to send the message
- **Note:** If no recipient is specified or if the recipient is currently not connected, you will see the following:
`The recipient client is unavailable.`
4. You are now ready to chat!!
5. To exit the GUI, simply click exit on the top right when you're done!

## Running in Research Mode
For the research portion of this project, we added a research mode to the Server and a research mode to the Chat CLI. 

**See "Research" Page for a more detailed explanation of our approach.**

### To Run Server in Research Mode
`java -jar ChatServer.jar <hostname> <port number> research <total clients needed> <opt log1> <opt log2>`
- `<total clients needed>` is the total number of clients we want to connect to the server as described in our research page.
- `<opt log1>` refers to a **csv** file that writes the times needed for messages to arrive to the recipients from the senders. 
- `<opt log2>` refers to a **csv** file that writes the times needed between the sender and the server.  

### To Run ChatCLI in Research Mode
`java -jar ChatCLI.jar <username> <server hostname> <server port> research <recipient> <num_msgs>`

- `<recipient>` sets the recipient for this client
- `<num_msgs>` the total number of messages sent from one client to another

**Note:** Linux Bash scripts and Windows Batch scripts were used to automate the process of creating these chat clients.
These scripts are located in the "Scripts" folder.

## Known Issues and Future Features
- [ ] **BUG** - Can alter text within chat window (received messages area, "Your username" area)
- [ ] **FEATURE** - format output in ChatCLI to include user name and time next to the message
- [ ] **Feature:** GUI Window does not resize dynamically per resolution - 