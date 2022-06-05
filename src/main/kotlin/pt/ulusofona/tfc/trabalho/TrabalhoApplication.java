package pt.ulusofona.tfc.trabalho;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import java.io.IOException;
import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.config.Configuration;
import io.imagekit.sdk.utils.Utils;
import javax.servlet.http.HttpServlet;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class TrabalhoApplication extends HttpServlet {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(TrabalhoApplication.class, args);

        ImageKit imageKit = ImageKit.getInstance();
        Configuration config = Utils.getSystemConfig(TrabalhoApplication.class);
        imageKit.setConfig(config);

        String rootPath = Paths.get("").toAbsolutePath().toString();

        if(!Files.exists(Path.of(rootPath + "/src/main/kotlin/pt/ulusofona/tfc/trabalho/tmp"))) {
            Files.createDirectory(Path.of(rootPath + "/src/main/kotlin/pt/ulusofona/tfc/trabalho/tmp"));
        }
    }
}
