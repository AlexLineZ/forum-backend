package com.example.fileservice.service.implementation;

import com.example.fileservice.service.FilenameService;
import com.ibm.icu.text.Transliterator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilenameServiceImpl implements FilenameService {

    private final Transliterator transliterator;

    @Override
    public String convertFilename(String name) {
        String newName = name.replace(" ", "_");
        return transliterator.transliterate(newName);
    }

}
