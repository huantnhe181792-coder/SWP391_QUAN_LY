package com.mycompany.swp391.entity;

import lombok.*;

import java.sql.Date;

@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class RequestJoinClub {

    private Integer id;
    private Integer accountId;
    private Integer clubId;
    private String self;
    private String reason;
    private String status;
    private Date created_at;
}
