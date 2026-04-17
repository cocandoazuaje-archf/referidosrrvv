mvn clean package -X

if [ "$1" == "1" ]
then
    echo "***** se hara Re-deployment ...."
    echo "***** se hara Re-deployment ...."
    echo "***** se hara Re-deployment ...."
    echo "***** se hara Re-deployment ...."

    /Users/carlosocando/Downloads/glassfish7/glassfish8/glassfish/bin/asadmin redeploy /Users/carlosocando/.m2/repository/cl/cns/referidosrrvv/1.0/referidosrrvv-1.0.war
else
    echo "***** se levantando glassfish7  ...."
    echo "***** se levantando glassfish7  ...."
    echo "***** se levantando glassfish7  ...."
    echo "***** se levantando glassfish7  ...."

 /Users/carlosocando/Downloads/glassfish7/glassfish8/glassfish/bin/asadmin start-domain

     echo "***** haciendo deploy en glassfish7  ...."
    echo "***** haciendo deploy en glassfish7  ...."
    echo "***** haciendo deploy en glassfish7  ...."
    echo "***** haciendo deploy en glassfish7  ...."

    /Users/carlosocando/Downloads/glassfish4/payara7/bin/asadmin deploy /Users/carlosocando/Documentos/referidos_elias/git/refderidos/git/rereferidosrrvv-git/target/referidosrrvv-1.0.war

fi




