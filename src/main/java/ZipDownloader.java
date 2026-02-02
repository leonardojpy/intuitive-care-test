import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ZipDownloader {

    private final HttpClient httpClient;
    private final Path downloadDirectory;

    public ZipDownloader(String downloadDir) {
        this.httpClient = HttpClient.newHttpClient();
        this.downloadDirectory = Paths.get(downloadDir);

        try {
            Files.createDirectories(downloadDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao criar diretório de download", e);
        }
    }

    public boolean downloadIfExists(String fileUrl) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fileUrl))
                    .GET()
                    .build();

            HttpResponse<byte[]> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofByteArray()
            );

            int statusCode = response.statusCode();

            if (statusCode == 200) {
                String fileName = extractFileName(fileUrl);
                Path filePath = downloadDirectory.resolve(fileName);

                Files.write(filePath, response.body());

                System.out.println("Arquivo baixado: " + fileName);
                System.out.println("Tamanho (bytes): " + response.body().length);
                return true;

            } else if (statusCode == 404) {
                System.out.println("Arquivo não encontrado (404): " + fileUrl);
                return false;

            } else {
                throw new RuntimeException(
                        "Erro inesperado ao acessar " + fileUrl +
                                " - Status: " + statusCode
                );
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erro na requisição HTTP", e);
        }
    }

    private String extractFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }
}
