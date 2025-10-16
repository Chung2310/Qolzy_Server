package com.example.Qolzy.mapper;

import com.example.Qolzy.model.music.Music;
import com.example.Qolzy.model.music.MusicDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MusicMapper {
    MusicDTO toMusicDTO(Music music);
}
