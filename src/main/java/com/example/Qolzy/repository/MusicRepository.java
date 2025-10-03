package com.example.Qolzy.repository;

import com.example.Qolzy.model.music.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music, Long> {

    boolean existsByIdM(Long idM);
    Music findMusicById(String id);
}
