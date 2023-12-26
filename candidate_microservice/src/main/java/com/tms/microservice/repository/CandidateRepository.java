package com.tms.microservice.repository;


import com.tms.microservice.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate,Integer> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO candidate_archive (candidate_id, name, address, created_on, update_on) " +
            "SELECT candidate_id, name, address, created_on, update_on FROM candidate WHERE update_on < :update_on", nativeQuery = true)
    void copyRecordsOlderThan(@Param("update_on") Date updatedOn);

    @Transactional
    @Modifying
    @Query(value = "UPDATE candidate r SET r.marked_for_delete = true WHERE r.update_on < :update_on", nativeQuery = true)
    void markForDelete(@Param("update_on") Date updatedOn);

    @Transactional
    @Modifying
    @Query("DELETE FROM candidate e WHERE e.marked_for_delete = true")
    void deleteByMarkedForDeleteIsTrue();
}