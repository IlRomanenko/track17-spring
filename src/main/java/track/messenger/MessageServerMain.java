package track.messenger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import track.messenger.net.MessengerServer;

/**
 *
 */
public class MessageServerMain {

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");

        MessengerServer server = (MessengerServer)context.getBean("messengerServer");
        server.start();
    }
}
