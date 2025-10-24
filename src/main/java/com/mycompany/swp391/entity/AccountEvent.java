/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.swp391.entity;

import lombok.*;

@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountEvent {
    private Integer id;
    private Integer account_id;
    private Integer event_id;
}
