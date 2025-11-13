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

public class RequestJoinEvent {
    private Integer id;
    private Integer accountId;
    private Integer eventId;
    private Date created_at;
    private String status;
}
