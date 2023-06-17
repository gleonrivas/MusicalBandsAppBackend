package com.example.solfamidasback.model.converter;

import com.example.solfamidasback.model.DTO.MusicSheetDTO;
import com.example.solfamidasback.model.MusicSheet;
import com.example.solfamidasback.repository.UserFormationRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MusicSheetConverter {

    @Autowired
    private UserFormationRoleRepository userFormationRoleRepository;


    public MusicSheetDTO toDTO(MusicSheet musicSheet) {

        MusicSheetDTO musicSheetDTO = new MusicSheetDTO();
        musicSheetDTO.setMusicSheetPdf(musicSheet.getMusicSheetPdf());

        return musicSheetDTO;
    }

    public List<MusicSheetDTO> toDTO(List<MusicSheet> musicSheets) {
         List<MusicSheetDTO> musicSheetDTOS =  new ArrayList<>();

        musicSheets.stream().forEach(musicSheet -> {
            MusicSheetDTO musicSheetDTO = new MusicSheetDTO();
            musicSheetDTO.setMusicSheetPdf(musicSheet.getMusicSheetPdf());
            musicSheetDTO.setId(musicSheet.getId());
            musicSheetDTOS.add(musicSheetDTO);
        });


        return musicSheetDTOS;
    }

    public MusicSheet toEntity(MusicSheetDTO musicSheetDTO) {

        MusicSheet musicSheet = new MusicSheet();
        musicSheet.setMusicSheetPdf(musicSheetDTO.getMusicSheetPdf());


        return musicSheet;
    }
}
