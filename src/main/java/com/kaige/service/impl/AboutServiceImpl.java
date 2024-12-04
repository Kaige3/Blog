package com.kaige.service.impl;

import com.kaige.repository.AboutRepository;
import com.kaige.service.AboutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AboutServiceImpl implements AboutService {

    @Autowired
    private AboutRepository aboutRepository;
    @Override
    public boolean getAboutCommentEnabled() {
        return aboutRepository.getAboutCommentEnabled();
    }
}
