package com.pizzaparlour.backend.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDto {

    private String type;

    private MultipartFile image;

    private String name;

}
