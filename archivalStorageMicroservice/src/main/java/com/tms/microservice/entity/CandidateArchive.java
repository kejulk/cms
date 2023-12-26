package com.tms.microservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="candidate_archive")
public class CandidateArchive {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer candidateId;
    private String name;

    private String address;

    @Temporal(TemporalType.DATE)
    private Date createdOn;

    @Temporal(TemporalType.DATE)
    private Date updateOn;
}