mvn clean package -X

if [ "$1" == "1" ]
then
    echo "***** se hara Re-deployment ...."
    echo "***** se hara Re-deployment ...."
    echo "***** se hara Re-deployment ...."
    echo "***** se hara Re-deployment ...."
    //Users/carlosocando/Downloads/glassfish4/payara7/bin/asadmin redeploy --name referidosrrvv /Users/carlosocando/Dropbox/Proyectos/JavaProjects/referidoseje/referidosrrvv/target/referidosrrvv.war
else
    echo "***** se hara deployment ...."
    echo "***** se hara deployment ...."
    echo "***** se hara deployment ...."
    echo "***** se hara deployment ...."
    /Users/carlosocando/Downloads/glassfish4/payara7/bin/asadmin deploy /Users/carlosocando/Dropbox/Proyectos/JavaProjects/referidoseje/referidosrrvv/target/referidosrrvv.war
fi




