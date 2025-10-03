package com.example.Qolzy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private UserEntityDTO userEntityDTO;
    private String title;
    private String content;
    private List<PostMediaDTO> mediaDTOS;
    private int likes;
    private int comments;
}
