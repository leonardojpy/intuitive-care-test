import org.junit.Test;

import java.util.List;

public class ZipDownloaderTest {
    @Test
    public void DeveBaixarArquivoCorretamente(){
        String url = "https://dadosabertos.ans.gov.br/FTP/PDA/demonstracoes_contabeis/2025/1T2025.zip";

        ZipDownloader downloader = new ZipDownloader("downloads");
        boolean success = downloader.downloadIfExists(url);

        System.out.println("Download realizado? " + success);
    }
}
