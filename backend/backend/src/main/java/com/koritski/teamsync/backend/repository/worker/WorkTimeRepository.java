package com.koritski.teamsync.backend.repository.worker;

import com.koritski.teamsync.backend.entity.worker.WorkTime;
import com.koritski.teamsync.backend.entity.organization.Worker;
import org.hibernate.jdbc.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkTimeRepository extends JpaRepository<WorkTime, Long> {
    List<WorkTime> findAllByWorkerAndIsPaymentLoaded(Worker worker, Boolean isPaymentLoaded);

//    @Query("select wt from WorkTime wt where wt.worker.userId = :userId and wt.endTime is null")
//    Optional<WorkTime> findCurrentWorkTime(@Param("userId") Long userId);
    Optional<WorkTime> findByWorkerAndEndTimeIsNull(Worker worker);
    List<WorkTime> findAllByWorker(Worker worker);
}
