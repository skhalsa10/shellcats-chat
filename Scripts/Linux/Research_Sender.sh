#!/bin/bash

echo 'How many total clients(must be a factor of 2) will this research test have? (this sender script will divide the total clients and create client1 through clientTotal/2 and pair them with a client in the receiver half)'
##Ask the user running script to privide research numbers
read clients

echo "Enter path to java"
read path

echo "Enter the number of messages a client will send"
read messages

##build a variable to use that point to the client to run
printf -v ChatCLI "$(pwd)/../../jars/ChatCLI.jar"
## loop on input number of clients

for (( i=1; i<=$((clients/2)); i++ ))
    do
        ##73.42.106.175
        ##73.26.145.62 lawrence
        ##/usr/bin/java -jar $ChatCLI client${i} localhost 8558 research client$((clients -(2-1)))&
        ${path} -jar $ChatCLI client${i} 73.26.145.62 8558 research client$((clients -(${i}-1))) ${messages}&
done
