mvn clean package -X

if [ "$1" == "1" ]
then
    echo "***** se hara Re-deployment ...."
    echo "***** se hara Re-deployment ...."
    echo "***** se hara Re-deployment ...."
    echo "***** se hara Re-deployment ...."
    /Users/carlosocando/Downloads/glassfish4/payara7/bin/asadmin redeploy /Users/carlosocando/.m2/repository/cl/cns/referidosrrvv/1.0/referidosrrvv-1.0.war
else
    echo "***** se levantando payara7  ...."
    echo "***** se levantando payara7  ...."
    echo "***** se levantando payara7  ...."
    echo "***** se levantando payara7  ...."
    # /Users/carlosocando/Downloads/glassfish4/payara7/bin/asadmin create-jvm-options "--add-opens=java.base/java.lang=ALL-UNNAMED"
 /Users/carlosocando/Downloads/glassfish4/payara7/bin/asadmin restart-domain

    #  echo "***** haciendo deploy en payara7  ...."
    # echo "***** haciendo deploy en payara7  ...."
    # echo "***** haciendo deploy en payara7  ...."
    # echo "***** haciendo deploy en payara7  ...."
    # /Users/carlosocando/Downloads/glassfish4/payara7/bin/asadmin deploy /Users/carlosocando/.m2/repository/cl/cns/referidosrrvv/1.0/referidosrrvv-1.0.war

fi




