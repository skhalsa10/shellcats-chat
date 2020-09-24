#!/bin/bash

echo 'How many total clients(must be a factor of 2) will this research test have? (this sender script will divide the total clients and create client1 through clientTotal/2 and pair them with a client in the receiver half)'
##Ask the user running script to privide research numbers
read clients
##build a variable to use that point to the client to run
printf -v ChatCLI "$(pwd)/../../jars/ChatCLI.jar"
## loop on input number of clients

for (( i=1; i<=$((clients/2)); i++ ))
    do
        /usr/bin/java -jar $ChatCLI client${i} localhost 8558 research client$((clients -(2-1)))
done