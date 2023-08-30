package com.example.shelterjsontocsv;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShelterJsonToCsvApplication {

    static String[] regionCode = {"11110", "11140", "11170", "11200", "11215", "11230", "11260", "11290",
            "11305", "11320", "11350", "11380", "11410", "11440", "11470", "11500", "11530",
            "11545", "11560", "11590", "11620", "11650", "11680", "11710", "11740", "26110",
            "26140", "26170", "26200", "26230", "26260", "26290", "26320", "26350", "26380",
            "26410", "26440", "26470", "26500", "26530", "26710", "27110", "27140", "27170",
            "27200", "27230", "27260", "27290", "27710", "27720", "28110", "28140", "28177",
            "28185", "28200", "28237", "28245", "28260", "28710", "28720", "29110", "29140",
            "29155", "29170", "29200", "30110", "30140", "30170", "30200", "30230", "31110",
            "31140", "31170", "31200", "31710", "41111", "41113", "41115", "41117", "41131",
            "41133", "41135", "41150", "41171", "41173", "41190", "41210", "41220", "41250",
            "41271", "41273", "41281", "41285", "41287", "41290", "41310", "41360", "41370",
            "41390", "41410", "41430", "41450", "41461", "41463", "41465", "41480", "41500",
            "41550", "41570", "41590", "41610", "41630", "41650", "41670", "41800", "41820",
            "41830", "43111", "43112", "43113", "43114", "43130", "43150", "43720", "43730",
            "43740", "43745", "43750", "43760", "43770", "43800", "44131", "44133", "44150",
            "44180", "44200", "44210", "44230", "44250", "44270", "44710", "44760", "44770",
            "44790", "44800", "44810", "44825", "45111", "45113", "45130", "45140", "45180",
            "45190", "45210", "45710", "45720", "45730", "45740", "45750", "45770", "45790",
            "45800", "46110", "46130", "46150", "46170", "46230", "46710", "46720", "46730",
            "46770", "46780", "46790", "46800", "46810", "46820", "46830", "46840", "46860",
            "46870", "46880", "46890", "46900", "46910", "47111", "47113", "47130", "47150",
            "47170", "47190", "47210", "47230", "47250", "47280", "47290", "47730", "47750",
            "47760", "47770", "47820", "47830", "47840", "47850", "47900", "47920", "47930",
            "47940", "48121", "48123", "48125", "48127", "48129", "48170", "48220", "48240",
            "48250", "48270", "48310", "48330", "48720", "48730", "48740", "48820", "48840",
            "48850", "48860", "48870", "48880", "48890", "50110", "50130", "51110", "51130",
            "51150", "51170", "51190", "51210", "51230", "51720", "51730", "51750", "51760",
            "51770", "51780", "51790", "51800", "51810", "51820", "51830"};

    // "https://www.safekorea.go.kr/idsiSFK/neo/ext/json/outhouseList/outhouseList_51830.json"
    // "https://www.safekorea.go.kr/idsiSFK/neo/ext/json/acmdfcltyList/acmdfcltyList_51830.json"

    static String path = "C:\\Users\\enble\\Downloads";
    static String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36";

    public static void main(String[] args) throws IOException {

        List<String> earthquakeList = new ArrayList<>();
        List<String> sufferList = new ArrayList<>();
        for (String url : regionCode) {
            Response response = sendRequest(
                    "https://www.safekorea.go.kr/idsiSFK/neo/ext/json/outhouseList/outhouseList_" + url + ".json");
            String body = response.body();
            earthquakeList.add(body);
        }
        for (String url : regionCode) {
            Response response = sendRequest(
                    "https://www.safekorea.go.kr/idsiSFK/neo/ext/json/acmdfcltyList/acmdfcltyList_" + url + ".json");
            String body = response.body();
            sufferList.add(body);
        }

        List<ShelterDto> shelterDtoList = new ArrayList<>();
        int count = 1;
        for (String s : earthquakeList) {
            JSONArray array = new JSONArray(s);

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String name = obj.getString("VT_ACMD_FCLTY_NM");
                String latitudeStr = obj.getString("LA");
                String longitudeStr = obj.getString("LO");
                String tel = obj.getString("TEL_NO");
                String detailAddress = obj.getString("DTL_ADRES");
                String legalDongCode = obj.getString("ARCD");

                double latitude;
                if (latitudeStr.isEmpty()) {
                    latitude = 0.0;
                } else {
                    latitude = Double.parseDouble(latitudeStr);
                }

                double longitude;
                if (longitudeStr.isEmpty()) {
                    longitude = 0.0;
                } else {
                    longitude = Double.parseDouble(longitudeStr);
                }

                ShelterDto dto = new ShelterDto(count, "EARTHQUAKE", name, latitude, longitude, tel,
                        detailAddress, legalDongCode);
                shelterDtoList.add(dto);
                count++;
            }
        }

        for (String s : sufferList) {
            JSONArray array = new JSONArray(s);

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String name = obj.getString("VT_ACMD_FCLTY_NM");
                String latitudeStr = obj.getString("LA");
                String longitudeStr = obj.getString("LO");
                String tel = obj.getString("TEL_NO");
                String detailAddress = obj.getString("DTL_ADRES");
                String legalDongCode = obj.getString("ARCD");

                double latitude;
                if (latitudeStr.isEmpty()) {
                    latitude = 0.0;
                } else {
                    latitude = Double.parseDouble(latitudeStr);
                }

                double longitude;
                if (longitudeStr.isEmpty()) {
                    longitude = 0.0;
                } else {
                    longitude = Double.parseDouble(longitudeStr);
                }

                ShelterDto dto = new ShelterDto(count, "SUFFER", name, latitude, longitude, tel,
                        detailAddress, legalDongCode);
                shelterDtoList.add(dto);
                count++;
            }
        }

        File file = new File(path, "shelter.csv");
        try (FileOutputStream fos = new FileOutputStream(
                file); OutputStreamWriter osw = new OutputStreamWriter(fos,
                StandardCharsets.UTF_8);) {
            StatefulBeanToCsv<ShelterDto> csvWriter = new StatefulBeanToCsvBuilder<ShelterDto>(osw)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
                    .withEscapechar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                    .withLineEnd(CSVWriter.DEFAULT_LINE_END)
                    .withOrderedResults(false)
                    .build();

            csvWriter.write(shelterDtoList);
        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            throw new RuntimeException(e);
        }

    }

    static Connection.Response sendRequest(String url) throws IOException {
        return Jsoup.connect(url).timeout(1000)
                .userAgent(USER_AGENT)
                .header("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7,ja;q=0.6")
                .header("Connection", "keep-alive")
                .header("Host", "www.safekorea.go.kr")
                .header("Cache-Control", "max-age=0")
                .header("Sec-Ch-Ua",
                        "\"Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"115\", \"Chromium\";v=\"115\"")
                .header("Sec-Ch-Ua-Mobile", "?0")
                .header("Sec-Ch-Ua-Platform", "\"Windows\"")
                .header("Sec-Fetch-Dest", "document")
                .header("Sec-Fetch-Mode", "navigate")
                .header("Sec-Fetch-Site", "none")
                .header("Sec-Fetch-User", "?1")
                .header("Content-Type", "application/json; charset=UTF-8")
                .header("Upgrade-Insecure-Requests", "1")
                .ignoreContentType(true)
                .method(Method.GET)
                .execute();
    }

}
