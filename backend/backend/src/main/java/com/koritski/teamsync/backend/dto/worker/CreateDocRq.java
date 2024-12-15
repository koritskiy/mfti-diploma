package com.koritski.teamsync.backend.dto.worker;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateDocRq {
    private String title = "PDF";
    private Integer fontSize = 10;
    private String textColor = "#333333";
    private DocData data = new DocData();

    @Data
    @Accessors(chain = true)
    public static class DocData {
        private String castca8ad5f0c1d311ed902457fbdc3d46aa;
        private String castd0307410c1d311ed902457fbdc3d46aa;
        private String castd92f3030ce7411ed9ee57d33ad801090;
        private String cast0044fc90ce7511ed9ee57d33ad801090;
        private String cast0579fe90ce7511ed9ee57d33ad801090;
        private String cast0b9cbbf0ce7511ed9ee57d33ad801090;
        private String cast1e02aa20ce7511ed9ee57d33ad801090;
        private String cast246110f0ce7511ed9ee57d33ad801090;
        private String cast2e35b2c0ce7511ed9ee57d33ad801090;
        private String cast33b886a0ce7511ed9ee57d33ad801090;
        private String cast45e03350ce7511ed9ee57d33ad801090;
        private String cast4cbdb530ce7511ed9ee57d33ad801090;
        private String cast5338f2d0ce7511ed9ee57d33ad801090;
        private String cast70efe9a0ce7511ed9ee57d33ad801090;
        private String cast82769b60ce7511ed9ee57d33ad801090;
        private String cast87516f70ce7511ed9ee57d33ad801090;
        private String casta0667c80ce7511ed9ee57d33ad801090 = "01";
        private String castb74596c0ce7511ed9ee57d33ad801090 = "1";
        private String castcfa117d0ce7511ed9ee57d33ad801090 = "5";
        private String caste0ac7f10ce7511ed9ee57d33ad801090;
    }
}
