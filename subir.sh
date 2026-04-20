#!/bin/bash

ASADMIN="/Users/carlosocando/Downloads/glassfish7/glassfish8/glassfish/bin"
BIN="/Users/carlosocando/Downloads/glassfish7/glassfish8/glassfish/bin"
FUENTE="/Users/carlosocando/Documentos/referidos_elias/git/refderidos/git/1.0-war/"
WAR="/Users/carlosocando/Documentos/referidos_elias/git/refderidos/git/1.0-war/target"

# compilar y generando war ...
function xwarcompile {
  clear
  echo "***** Preparando para compilar y generando WAR *****"

  cd "$FUENTE" || exit 1

  mvn clean package -X

  echo "***** Ya Compilo *****"
}

cd "$FUENTE"

if [ "$1" = "1" ]; then
  echo "***** Redeploy *****"
else
  echo "***** Iniciando start-domain en GlassFish *****"
  "$BIN"/asadmin start-domain

  echo "***** Haciendo Deploy *****"
  "$ASADMIN" deploy "$WAR"/referidosrrvv-1.0.war
fi

# pool/referidos
function xglaspool {
  sh asadmin create-jdbc-connection-pool \
    --datasourceclassname org.postgresql.ds.PGSimpleDataSource \
    --restype javax.sql.DataSource \
    --property user=postgres:password=1234:serverName=localhost:portNumber=5432:databaseName=referidos \
    --steadypoolsize 5 \
    --maxpoolsize 50 \
    --poolresize 5 \
    --idletimeout 300 \
    pool/referidos
}

# jdbc/referidos
function xglasjdbc {
  sh asadmin create-jdbc-resource \
    --connectionpoolid pool/referidos \
    jdbc/referidos
}

# ⚠️ El case va FUERA de la función
case "$1" in
  1)
    echo "***** Entro a la op 1 del case *****"
    xwarcompile
    ;;
  2)
    echo "***** Entro a la op 2 del case *****"
    xglaspool
    ;;
  3)
    echo "***** Entro a la op 3 del case *****"
    xglasjdbc
    ;;
  *)
    echo "Uso: de las opciones {1|2|3}"
    echo "1 -> Compilar y deploy (xwarcompile)"
    echo "2 -> Crear pool (xglaspool)"
    echo "3 -> Crear jdbc (xglasjdbc)"
    ;;
esac

pkill "brave browser"
