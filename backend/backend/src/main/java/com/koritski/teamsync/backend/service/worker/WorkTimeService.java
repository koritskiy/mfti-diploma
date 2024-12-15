package com.koritski.teamsync.backend.service.worker;


import com.koritski.teamsync.backend.dto.worker.CreateWorkTimeRq;
import com.koritski.teamsync.backend.entity.organization.Function;
import com.koritski.teamsync.backend.entity.organization.Worker;
import com.koritski.teamsync.backend.entity.organization.WorkerFunction;
import com.koritski.teamsync.backend.entity.worker.WorkTime;
import com.koritski.teamsync.backend.repository.organization.FunctionRepository;
import com.koritski.teamsync.backend.repository.organization.WorkerFunctionRepository;
import com.koritski.teamsync.backend.repository.organization.WorkerRepository;
import com.koritski.teamsync.backend.repository.worker.WorkTimeRepository;
import com.koritski.teamsync.backend.util.JwtUtil;
import com.koritski.teamsync.backend.util.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkTimeService {
    private final WorkTimeRepository workTimeRepository;

    private final WorkerRepository workerRepository;
    private final WorkerFunctionRepository workerFunctionRepository;
    private final FunctionRepository functionRepository;
    private final JwtUtil jwtUtil;

    /**
     * Метод начала рабочего времени
     * @param rq данные о новом рабочем времени
     * @return http-ответ
     */
    public ResponseEntity<?> startWorkTime(CreateWorkTimeRq rq) {
        Optional<Worker> worker = workerRepository.findByUserId(rq.getUserId());
        if (worker.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Worker with user-id = %s not found.", rq.getUserId())
            );
        }

        Optional<WorkerFunction> workerFunction = workerFunctionRepository.findById(rq.getWorkerFunctionId());
        if (workerFunction.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Worker function with id = %s not found.", rq.getWorkerFunctionId())
            );
        }

        WorkTime workTime = new WorkTime()
                .setStartTime(OffsetDateTime.now())
                .setTariffRate(workerFunction.get().getTariffRate())
                .setWorker(worker.get())
                .setFunction(workerFunction.get().getFunction());

        workTimeRepository.save(workTime);

        return Response.send(
                HttpStatus.OK,
                workTime
        );
    }

    /**
     * Метод окончания рабочего времени
     * @param workTimeId id-рабочего времени в БД
     * @return http-ответ
     */
    public ResponseEntity<?> endWorkTime(Long workTimeId) {
        Optional<WorkTime> workTime = workTimeRepository.findById(workTimeId);
        if (workTime.isEmpty()) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    String.format("Work time with id = %s not found.", workTimeId)
            );
        }

        OffsetDateTime endTime = OffsetDateTime.now();
        OffsetDateTime startTime = workTime.get().getStartTime();
        Duration difference = Duration.between(startTime, endTime);

        long hours = difference.toHours(),
                rate = workTime.get().getTariffRate(),
                total = 0;

        if (hours < 1L) {
            total = countCashByMinutes(difference.toMinutes(), rate);
        } else {
            total = hours * rate;
            difference = difference.minusHours(hours);
            total += countCashByMinutes(difference.toMinutes(), rate);
        }

        workTime.get()
                .setEndTime(endTime)
                .setTotal(total);
        WorkTime finalTime = workTimeRepository.save(workTime.get());

        return Response.send(
                HttpStatus.OK,
                finalTime
        );
    }

    /**
     * Метод проверки существования активного рабочего времени
     * (один сотрудник не может работать по двум направлениям одновременно)
     * @param token токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> getCurrentWorkTime(String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        Optional<Worker> worker = workerRepository.findByUserId(userId);
        if (worker.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Worker with user-id = %s not found.", userId)
            );
        }

        Optional<WorkTime> workTime = workTimeRepository.findByWorkerAndEndTimeIsNull(worker.get());
        if (workTime.isEmpty()) {
            return Response.send(
                    HttpStatus.OK,
                    "Current work time not found."
            );
        }

        return Response.send(
                HttpStatus.OK,
                workTime.get()
        );
    }

    /**
     * Метод подсчета заработанных денег за минуты
     * @param minutes кол-во минут
     * @param rate тариф
     * @return зарплата за неполный час
     */
    private long countCashByMinutes(long minutes, long rate) {
        long percent = (minutes * 100) / 60;
        return (rate / 100) * percent;
    }
}
