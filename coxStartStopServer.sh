#!/bin/bash

if [ "$1" == "0" ]
then
    echo "***** se hara Stop server ...."
    echo "***** se hara Stop server ...."
    echo "***** se hara Stop server ...."
    echo "***** se hara Stop server ...."
    /Users/carlosocando/GlassFish_Server/glassfish/bin/asadmin stop-domain $2
fi
if [ "$1" == "1" ] 
then
    echo "***** se hara Start server ...."
    echo "***** se hara Start server ...."
    echo "***** se hara Start server ...."
    echo "***** se hara Start server ...."
    /Users/carlosocando/GlassFish_Server/glassfish/bin/asadmin start-domain $2
fi


