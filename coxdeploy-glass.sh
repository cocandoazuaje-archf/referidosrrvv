#!/bin/bash

clear

GLASSFISH="/Users/carlosocando/Downloads/glassfish7/glassfish8/glassfish/bin/asadmin"
WAR="/Users/carlosocando/Documentos/referidos_elias/git/refderidos/git/rereferidosrrvv-git/target/referidosrrvv-1.0.war"

echo "***** Compilando proyecto *****"
mvn clean package -DskipTests

if [ "$1" = "1" ]; then
    echo "***** Redeploy *****"
    $GLASSFISH redeploy "$WAR" referidosrrvv-1.0

else
    echo "***** Iniciando GlassFish *****"
    $GLASSFISH start-domain

    echo "***** Deploy *****"
    $GLASSFISH deploy --force=true "$WAR"

fi