package com.tms.microservice.repository;


import com.tms.microservice.entity.Candidate;
import com.tms.microservice.entity.CandidateArchive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface CandidateArchiveRepository extends JpaRepository<CandidateArchive,Integer> {


}