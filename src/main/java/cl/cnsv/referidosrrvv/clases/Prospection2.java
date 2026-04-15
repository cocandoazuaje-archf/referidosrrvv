// /*
//  * To change this license header, choose License Headers in Project Properties.
//  * To change this template file, choose Tools | Templates
//  * and open the template in the editor.
//  */
// package cl.cnsv.referidosrrvv.clases;

// import jakarta.ws.rs.client.Client;
// import jakarta.ws.rs.core.MediaType;

// /**
//  *
//  * @author cox
//  */
// public class Prospection2 {

//     private static String salida = "";
//     private static final ;
//     import org.apache.logging.log4j.LogManager;  
//     import org.apache.logging.log4j.LogManager.getLogger(Prospection.class);

//     public static String consultar(String rut, int timeout) {
//         String url2 = Resources.URL_PROSPECTION + "?param=" + rut.trim();

//         Client c = Resources.client;

//         OuPprospeccion op = c
//                 .target(url2)
//                 .request(MediaType.APPLICATION_JSON)
//                 .header("x-api-key", Resources.KEY_PROSPECTION)
//                 .get(OuPprospeccion.class);

//         salida = op.getIndicador();

//         if (salida.contains("consultado no tiene ")) {
//             salida = "";
//         }

//         return salida;
//     }
// }
