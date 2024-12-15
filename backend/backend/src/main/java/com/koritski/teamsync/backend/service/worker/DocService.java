package com.koritski.teamsync.backend.service.worker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.icu.text.RuleBasedNumberFormat;
import com.koritski.teamsync.backend.dto.rs.PaymentDocNotifyRs;
import com.koritski.teamsync.backend.dto.worker.CreateDocRq;
import com.koritski.teamsync.backend.dto.worker.GetPaymentByWorkerRq;
import com.koritski.teamsync.backend.entity.organization.Organization;
import com.koritski.teamsync.backend.entity.organization.Worker;
import com.koritski.teamsync.backend.entity.worker.WorkTime;
import com.koritski.teamsync.backend.repository.organization.OrganizationRepository;
import com.koritski.teamsync.backend.repository.organization.WorkerRepository;
import com.koritski.teamsync.backend.repository.worker.WorkTimeRepository;
import javassist.NotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Data
@Slf4j
@RequiredArgsConstructor
@Service
public class DocService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;
    private final OrganizationRepository organizationRepository;
    private final WorkerRepository workerRepository;
    private final WorkTimeRepository workTimeRepository;
    private final static String URL = "https://app.useanvil.com/api/v1/fill/DxTOW1V86IVByTj8L48X.pdf";
    private final static String AUTH_TOKEN_ANVIL = "Basic WTNpZHJiWmg1TnRsb0R3aXluU1FCclRSQWtJUFlBdWs6";
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Метод получения квитанции по сотруднику об отработанном времени
     *
     * @param rq запрос с данными по сотруднику
     * @return массив байт (pdf-файл квитанции)
     * @throws JsonProcessingException при обращении к anvil может вернуться некорректный json
     * @throws NotFoundException       вместо ответа 404 выбрасывается exception для предотвращения ложного запроса в anvil
     */
    public byte[] getDocForPayment(GetPaymentByWorkerRq rq, String token) throws JsonProcessingException, NotFoundException {
        Optional<Organization> organization = organizationRepository.findById(rq.getOrganizationId());
        if (organization.isEmpty()) {
            throw new NotFoundException(
                    String.format("Organization with id = %s not found.", rq.getOrganizationId())
            );
        }

        Optional<Worker> worker = workerRepository.findById(rq.getWorkerId());
        if (worker.isEmpty()) {
            throw new NotFoundException(
                    String.format("Worker with id = %s not found.", rq.getWorkerId())
            );
        }

        List<WorkTime> allWorkTimeForWorker
                = workTimeRepository.findAllByWorkerAndIsPaymentLoaded(worker.get(), false);

        long totalCash = 0;
        for (WorkTime eachWorkTime : allWorkTimeForWorker) {
            totalCash += eachWorkTime.getTotal();

            eachWorkTime.setIsPaymentLoaded(true);
            workTimeRepository.save(eachWorkTime);
        }

        if (totalCash == 0) {
            throw new NotFoundException("Total cash for this worker is null.");
        }

        CreateDocRq.DocData rqDocData = new CreateDocRq.DocData()
                .setCastca8ad5f0c1d311ed902457fbdc3d46aa(LocalDate.now().toString())
                .setCastd0307410c1d311ed902457fbdc3d46aa(String.valueOf(1 + (int) (Math.random() * 1000)))
                .setCast0044fc90ce7511ed9ee57d33ad801090(organization.get().getInn())
                .setCast0579fe90ce7511ed9ee57d33ad801090(organization.get().getKpp())
                .setCast1e02aa20ce7511ed9ee57d33ad801090(organization.get().getFullOrganizationTitle())
                .setCast246110f0ce7511ed9ee57d33ad801090(organization.get().getAccountNumber())
                .setCast2e35b2c0ce7511ed9ee57d33ad801090(organization.get().getOrganizationBank())
                .setCast33b886a0ce7511ed9ee57d33ad801090(organization.get().getBankBik())
                .setCast45e03350ce7511ed9ee57d33ad801090(organization.get().getBankAccountNumber())
                .setCast4cbdb530ce7511ed9ee57d33ad801090(worker.get().getWorkerBank())
                .setCast5338f2d0ce7511ed9ee57d33ad801090(worker.get().getBankBik())
                .setCast70efe9a0ce7511ed9ee57d33ad801090(worker.get().getBankAccountNumber())
                .setCast82769b60ce7511ed9ee57d33ad801090(worker.get().getInn())
                .setCast87516f70ce7511ed9ee57d33ad801090(
                        String.format("%s %s %s",
                                worker.get().getLastName(), worker.get().getFirstName(), worker.get().getMiddleName()
                        )
                )
                .setCaste0ac7f10ce7511ed9ee57d33ad801090(rq.getReason())
                .setCast0b9cbbf0ce7511ed9ee57d33ad801090(
                        String.format("%s - 00", totalCash)
                );

        // Сумма прописью
        RuleBasedNumberFormat nf = new RuleBasedNumberFormat(Locale.forLanguageTag("ru"),
                RuleBasedNumberFormat.SPELLOUT);
        rqDocData
                .setCastd92f3030ce7411ed9ee57d33ad801090(nf.format(totalCash) + " рублей 00 копеек");

        CreateDocRq dtoForRequestForAnvil = new CreateDocRq()
                .setData(rqDocData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", AUTH_TOKEN_ANVIL);

        HttpEntity<String> requestForAnvil =
                new HttpEntity<String>(objectMapper.writeValueAsString(dtoForRequestForAnvil), headers);

        ResponseEntity<byte[]> array = restTemplate.postForEntity(URL, requestForAnvil, byte[].class);

        try {
            // В АУФЕ нельзя получить данные пользователя не от самого пользователя !!!
            HttpHeaders headersForAuth = new HttpHeaders();
            headersForAuth.add(HttpHeaders.AUTHORIZATION, token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headersForAuth);

            ResponseEntity<String> response = restTemplate.exchange(
                    "http://localhost:8082/api/auth/v1/user/get/tg/" + worker.get().getUserId(),
                    HttpMethod.GET,
                    requestEntity,
                    String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());
            String tgChatId = root.path("payload").path("tgChatId").asText();

            if (!tgChatId.isEmpty()) {
                PaymentDocNotifyRs rs = new PaymentDocNotifyRs()
                        .setTotalCash(String.valueOf(totalCash))
                        .setUserTgChatId(tgChatId);
                kafkaTemplate.send("TEAMSYNC.PAYMENT_DOC.V1", objectMapper.writeValueAsString(rs));
            }
        } catch (Exception e) {
            log.error("Message to kafka not send.");
            log.error(e.getMessage());
        }

        return array.getBody();
    }
}
