package ru.khananov.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailParamsDto {
    private String id;
    private String emailTo;
    private String tempPassword;
}
