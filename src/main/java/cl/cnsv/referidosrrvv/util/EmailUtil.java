/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import  org.apache.logging.log4j.LogManager;

public class EmailUtil {

    private static final Logger LOGGER = Logger.getLogger(EmailUtil.class);

    public static void sendEmail(
            Session session,
            String toEmail,
            String subject,
            String body) throws UnsupportedEncodingException, MessagingException {
        try {
            MimeMessage msg = new MimeMessage(session);
            // set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(
                    new InternetAddress("referidosrrvv@consorcio.cl", "ReferidosRRVV"));
            msg.setSubject(subject, "UTF-8");
            msg.setContent(body, "text/HTML; charset=UTF-8");
            msg.setSentDate(new Date());

            msg.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail, false));
            Transport.send(msg);
        } catch (UnsupportedEncodingException | MessagingException e) {
            LOGGER.info(e);
        }
    }
}
