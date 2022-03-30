import java.io.File;

public class Bot {
    public static void main(final String[] args) {
        String pathFile = "/home/dam1/IdeaProjects/ExamenCOD/token.txt";
        File file = new File(pathFile);
        if (file.exists()) {
            Metodos.bot(Metodos.leerFichero(file));
        } else {
            Metodos.escribirToken(pathFile);
            Metodos.bot(Metodos.leerFichero(file));
        }
    }
}
