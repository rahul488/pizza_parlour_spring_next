package com.pizzaparlour.backend.Dto.Request;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BannerRequestDto {

    private String name;

    private String desc;

    private MultipartFile image;
}
