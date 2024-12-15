package com.koritski.teamsync.hiring.service;

import com.koritski.teamsync.hiring.util.Response;
import com.koritski.teamsync.hiring.dto.CreateInterviewResultRq;
import com.koritski.teamsync.hiring.dto.CreateInterviewRq;
import com.koritski.teamsync.hiring.dto.CreateInterviewTaskStackRq;
import com.koritski.teamsync.hiring.entity.Candidate;
import com.koritski.teamsync.hiring.entity.Interview;
import com.koritski.teamsync.hiring.entity.InterviewTask;
import com.koritski.teamsync.hiring.entity.InterviewTaskStack;
import com.koritski.teamsync.hiring.repository.CandidateRepository;
import com.koritski.teamsync.hiring.repository.InterviewRepository;
import com.koritski.teamsync.hiring.repository.InterviewTaskRepository;
import com.koritski.teamsync.hiring.repository.InterviewTaskStackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final CandidateRepository candidateRepository;
    private final InterviewTaskRepository interviewTaskRepository;
    private final InterviewTaskStackRepository interviewTaskStackRepository;

    /**
     * Метод создания собеседования
     * @param rq данные нового собеседования
     * @return http-ответ
     */
    public ResponseEntity<?> createInterview(CreateInterviewRq rq) {
        Optional<Candidate> candidate = candidateRepository.findById(rq.getCandidateId());

        if (candidate.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Candidate with id = %s not found.", rq.getCandidateId())
            );
        }

        Interview interview = new Interview()
                .setCandidate(candidate.get())
                .setRecruiter(rq.getRecruiter());

        interviewRepository.save(interview);

        return Response.send(
                HttpStatus.OK,
                interview
        );
    }

    /**
     * Метод редактирования существующего собеседования
     * @param rq данные обновленного собеседования
     * @param id id-собеседования
     * @return http-ответ
     */
    public ResponseEntity<?> updateInterview(CreateInterviewRq rq, Long id) {
        Optional<Interview> interview = interviewRepository.findById(id);

        if (interview.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Interview with id = %s not found.", id)
            );
        }

        Optional<Candidate> candidate = candidateRepository.findById(rq.getCandidateId());

        if (candidate.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Candidate with id = %s not found.", rq.getCandidateId())
            );
        }

        interview.get()
                .setCandidate(candidate.get())
                .setRecruiter(rq.getRecruiter());

        Interview updatedInterview = interviewRepository.save(interview.get());

        return Response.send(
                HttpStatus.OK,
                updatedInterview
        );
    }

    /**
     * Метод сохранения результатов собеседования
     * @param rq тело запрос с данными о собеседовании
     * @param id id-собеседования
     * @return http-ответ
     */
    public ResponseEntity<?> resultForInterview(CreateInterviewResultRq rq, Long id) {
        Optional<Interview> interview = interviewRepository.findById(id);

        if (interview.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Interview with id = %s not found.", id)
            );
        }


        interview.get()
                .setInterviewFeedback(rq.getInterviewFeedback())
                .setInterviewDecision(rq.getInterviewDecision());

        Interview resultedInterview = interviewRepository.save(interview.get());

        return Response.send(
                HttpStatus.OK,
                resultedInterview
        );
    }

    /**
     * Метод привязывания задачи к собеседованию
     * @param rq данные задачи и собеседованию
     * @return http-ответ
     */
    public ResponseEntity<?> addTaskToInterview(CreateInterviewTaskStackRq rq) {
        Optional<Interview> interview = interviewRepository.findById(rq.getInterviewId());

        if (interview.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Interview with id = %s not found.", rq.getInterviewId())
            );
        }

        Optional<InterviewTask> interviewTask = interviewTaskRepository.findById(rq.getTaskId());

        if (interviewTask.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Interview task with id = %s not found.", rq.getTaskId())
            );
        }

        InterviewTaskStack interviewTaskStack = new InterviewTaskStack()
                .setInterview(interview.get())
                .setInterviewTask(interviewTask.get());

        interviewTaskStack = interviewTaskStackRepository.save(interviewTaskStack);

        return Response.send(
                HttpStatus.OK,
                interviewTaskStack.getInterview()
        );
    }
}