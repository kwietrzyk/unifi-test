import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class BaseTest {

    // Metoda, która uruchomi skrypt install.sh w WSL
    public static void runInstallScript() throws IOException, InterruptedException {
        // Ścieżka do WSL na Twoim komputerze
        String wslPath = "C:\\Windows\\System32\\wsl.exe";

        // Ścieżka do skryptu install.sh w katalogu src/test/resources w IntelliJ
        String scriptPath = "/mnt/c/Users/YourUserPath/IdeaProjects/YourProjectName/src/test/resources/install.sh";

        // Tworzymy proces, który uruchomi skrypt install.sh w WSL
        ProcessBuilder builder = new ProcessBuilder(wslPath, "bash", scriptPath);
        builder.redirectErrorStream(true);  // Łączy wyjście błędów z wyjściem standardowym
        Process process = builder.start();

        // Używamy OutputStream do symulowania wejść do skryptu
        OutputStream outputStream = process.getOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);

        // Symulujemy naciśnięcie ENTER
        writer.println();
        writer.flush();

        // Jeżeli skrypt wymaga podania ścieżki, to można ją podać tutaj:
        String pathToProvide = "/mnt/c/Users/YourUserPath/path";
        writer.println(pathToProvide);
        writer.flush();

        // Odczytujemy wyjście skryptu, aby zobaczyć, co się dzieje
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        // Oczekujemy na zakończenie procesu
        process.waitFor();

        // Zamykamy strumienie
        writer.close();
        reader.close();
    }

}
