import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import reactor.core.publisher.Mono;

import javax.swing.*;
import java.io.*;
import java.time.Instant;
import java.util.Scanner;

public class Metodos {
    private static String IMAGE_URL;
    private static String ANY_URL;
    static PrintWriter escribir;
    static FileWriter fich;

    /**
     * Metodo que sirve para escribir un texto corto en un fichero
     *
     * @param fichero Nombre del fichero donde escribir el texto
     */
    public static void escribirToken(String fichero) {
        try {
            fich = new FileWriter(fichero, false);
            escribir = new PrintWriter(fich);
            escribir.println(JOptionPane.showInputDialog("Introducir token"));
            System.out.println("Fichero creado con exito");

        } catch (IOException e) {
            System.out.println("Error escritura" + e.getMessage());
        } finally {
            escribir.close();
        }
    }

    /**
     * Lee un fichero y devuelve su contenido en forma de string
     *
     * @param file El fichero del qu se desean extrae los datos
     * @return Un string con el contenido del fichero
     */
    public static String leerFichero(File file) {

        Scanner sc = null;

        String message = "";

        try {
            sc = new Scanner(file);

            while (sc.hasNextLine()) {
                message += sc.nextLine();
            }

        } catch (FileNotFoundException ex) {

            System.out.println("Error:" + ex.getMessage());

        } finally {
            sc.close();
        }

        return message;
    }

    /**
     * @param token Método que recibe un string para introducir el token del bot.
     *              El bot cada vez que recibe un mensaje ping responde
     *              con un pong.
     */
    public static void crearBot(String token) {
        final DiscordClient client = DiscordClient.create(token);
        /*Este metodo nos permite saber que es lo que tiene que hacer el bot cuando se loggea*/
        Mono<Void> login = client.withGateway((GatewayDiscordClient gateway) -> {
            /*Se imprime la cuenta a la que está asociada el bot*/
            Mono<Void> printOnLogin = gateway.on(ReadyEvent.class, event ->
                            Mono.fromRunnable(() -> {
                                final User self = event.getSelf();
                                System.out.printf("Logged in as %s#%s%n", self.getUsername(), self.getDiscriminator());
                            }))
                    .then();

            // Creamos un evento de mensaje cada vez que se escribe en el chat
            //(en este caso al escribir "ping!")
            Mono<Void> handlePingCommand = gateway.on(MessageCreateEvent.class, event -> {
                Message message = event.getMessage();

                if (message.getContent().equalsIgnoreCase("ping")) {
                    return message.getChannel()
                            .flatMap(channel -> channel.createMessage("pong!"));
                }

                return Mono.empty();
            }).then();
            /*Haciendo esto combiamos los mensajes y se imprime la cuenta del bot asociado
             * y tambien el evento de respuesta al ping*/
            return printOnLogin.and(handlePingCommand);
        });
    }

    public static void bot(String token) {
        final DiscordClient client = DiscordClient.create(token);
        final GatewayDiscordClient gateway = client.login().block();

        gateway.on(MessageCreateEvent.class).subscribe(event -> {
            final Message message = event.getMessage();
            if ("sus".equals(message.getContent())) {
                IMAGE_URL = "https://c.tenor.com/bd5bGRCMdwwAAAAd/among-us-sussy-baka-sus-check.gif";
                ANY_URL = "https://www.youtube.com/watch?v=D32EHS748D4";
                final MessageChannel channel = message.getChannel().block();
                EmbedCreateSpec.Builder builder = EmbedCreateSpec.builder();
                builder.author("impostor", ANY_URL, IMAGE_URL);
                builder.image(IMAGE_URL);
                builder.title("Amongos");
                builder.url(ANY_URL);
                builder.description("sus");
                //  builder.addField("addField", "inline = true", true);
                // builder.addField("addFIeld", "inline = true", true);
                //builder.addField("addFile", "inline = false", false);
                builder.thumbnail(IMAGE_URL);
                //builder.footer("esto no se que es --> 2022", IMAGE_URL);
                builder.timestamp(Instant.now());
                channel.createMessage(builder.build()).block();
            }
        });

        gateway.onDisconnect().block();
    }

    public void fotos() {
        File fichero = new File("/home/dam1/bot");
        // returns an array of all files
        String[] fileList = fichero.list();

        for (String str : fileList) {
            System.out.println(str);
        }
    }
}

