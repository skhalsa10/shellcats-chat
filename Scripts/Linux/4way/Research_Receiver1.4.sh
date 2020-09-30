#!/bin/bash

echo 'How many total clients(must be a factor of 2) will this research test have?(these will be the receiver PART)'
##Ask the user running script to privide research numbers
read clients

echo "Enter path to java"
read path

echo "Enter the number of messages a client will send"
read messages

##build a variable to use that point to the client to run
printf -v ChatCLI "$(pwd)/../../jars/ChatCLI.jar"
## loop on input number of clients

for (( i=1; i<=$((clients/4)); i++ ))
    do
        ## 73.42.106.175
        ##/usr/bin/java -jar $ChatCLI client${i} localhost 8558 research client$((clients -(2-1)))&
        echo client$((clients -(${i}-1)))
        ${path} -jar $ChatCLI client$((clients -(${i}-1))) 73.42.106.175 8558 research client${i} ${messages}&
done
