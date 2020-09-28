#!/bin/bash

echo 'How many total clients(must be a factor of 2) will this research test have?(these will be the receiver PART)'
##Ask the user running script to privide research numbers
read clients

echo "Enter path to java"
read path

##build a variable to use that point to the client to run
printf -v ChatCLI "$(pwd)/../../jars/ChatCLI.jar"
## loop on input number of clients

for (( i=1; i<=$((clients/2)); i++ ))
    do
        ##/usr/bin/java -jar $ChatCLI client${i} localhost 8558 research client$((clients -(2-1)))&
        ${path} -jar $ChatCLI client$((clients -(${i}-1))) localhost 8558 research client${i}&
done