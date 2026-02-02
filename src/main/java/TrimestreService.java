import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class TrimestreService {

    private static final String BASE_URL =
            "https://dadosabertos.ans.gov.br/FTP/PDA/demonstracoes_contabeis/";

    //busca no max. 10 anos antes da data atual, caso tenha longos períodos sem dados, interrompe a busca.
    private static final int LIMITE_ANOS = 10;

    private final HttpClient httpClient;

    public TrimestreService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public List<String> descobrirUltimosTresTrimestres() {
        List<String> urlsEncontradas = new ArrayList<>();

        int anoAtual = Year.now().getValue();
        int anoLimite = anoAtual - LIMITE_ANOS;

        for (int ano = anoAtual; ano >= anoLimite; ano--) {

            for (int trimestre = 4; trimestre >= 1; trimestre--) {

                String nomeArquivo = trimestre + "T" + ano + ".zip";
                String url = BASE_URL + ano + "/" + nomeArquivo;

                if (arquivoExiste(url)) {
                    urlsEncontradas.add(url);
                }

                if (urlsEncontradas.size() == 3) {
                    return urlsEncontradas;
                }
            }
        }

        throw new RuntimeException(
                "Não foram encontrados 3 trimestres disponíveis nos últimos "
                        + LIMITE_ANOS + " anos."
        );
    }

    private boolean arquivoExiste(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .method("HEAD", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<Void> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.discarding());

            return response.statusCode() == 200;

        } catch (Exception e) {
            return false;
        }
    }
}
