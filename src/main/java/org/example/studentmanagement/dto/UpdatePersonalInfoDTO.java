package org.example.studentmanagement.dto;

import lombok.Data;

@Data
public class UpdatePersonalInfoDTO {
    private String fullName; // Họ tên
    private String email;    // Email
    private String address;  // Địa chỉ  // Optional if role needs updating (ensure security!)

}
