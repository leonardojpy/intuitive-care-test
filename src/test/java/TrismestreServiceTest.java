import org.junit.Test;

import java.util.List;

public class TrismestreServiceTest {
    @Test
    public void DeveBaixarUltimosTresTrimestres() {

        TrimestreService trimestreService = new TrimestreService();
        ZipDownloader downloader = new ZipDownloader("downloads");

        List<String> urls = trimestreService.descobrirUltimosTresTrimestres();

        urls.forEach(downloader::downloadIfExists);
    }
}
