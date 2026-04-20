cd /Users/carlosocando/Documentos/referidos_elias/git/refderidos/git/1.0-war

  read "REPLY?**** Presione ENTER para package ... \n" < /dev/tty

mvn clean package -X
cd /Users/carlosocando/Documentos/referidos_elias/git/refderidos/git/1.0-war

  read "REPLY?**** Presione ENTER para List App ... \n" < /dev/tty

sh /Users/carlosocando/Downloads/glassfish7/glassfish8/glassfish/bin/asadmin list-applications

  read "REPLY?**** Presione ENTER para undeploy ... \n" < /dev/tty

/Users/carlosocando/Downloads/glassfish7/glassfish8/glassfish/bin/asadmin undeploy referidosrrvv

  read "REPLY?**** Presione ENTER para redeploy ... \n" < /dev/tty

/Users/carlosocando/Downloads/glassfish7/glassfish8/glassfish/bin/asadmin redeploy \
  --force=true \
  /Users/carlosocando/Documentos/referidos_elias/git/refderidos/git/1.0-war/target/referidosrrvv-1.0.war
