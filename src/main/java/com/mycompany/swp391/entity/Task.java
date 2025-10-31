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
public class Task {
    private Integer id;
    private String taskName;
    private Integer eventId;
    private String taskDescription;
    private Integer accountId;
    private Date createDate;
    private Date updateDate;

}
