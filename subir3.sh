#!/bin/bash

go_project() {
  read "REPLY?**** Presione ENTER para hacer cd ... \n" < /dev/tty
  cd /Users/carlosocando/Documentos/referidos_elias/git/refderidos/git/1.0-war || return 1
}

build_project() {
  read "REPLY?**** Presione ENTER para package ... \n" < /dev/tty
  mvn clean package -X
}

deploy_project() {
  read "REPLY?**** Presione ENTER para deploy ... \n" < /dev/tty
  /Users/carlosocando/Downloads/glassfish7/glassfish8/glassfish/bin/asadmin deploy \
    --force=true \
    /Users/carlosocando/Documentos/referidos_elias/git/refderidos/git/1.0-war/target/referidosrrvv-1.0.war
}

  xglasoff
  xglason
  go_project
  build_project
  deploy_project
