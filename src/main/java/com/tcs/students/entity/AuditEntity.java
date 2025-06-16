package com.tcs.students.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "audits")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private Integer auditId;

    @Column(name = "feature_name")
    private String featureName;

    @Column(name = "audit_type")//CREATED
    private String auditType;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "audit_date")
    private Date auditDate;

    @Column(name = "details_info")
    private String detailsInfo;

}
