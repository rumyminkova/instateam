package com.teamtreehouse.instateam.converter;

import com.teamtreehouse.instateam.model.Collaborator;
import com.teamtreehouse.instateam.service.CollaboratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Rumy on 7/30/2017.
 */
@Component
public class CollaboratorConverter implements Converter<String,Collaborator>{
        @Autowired
        private CollaboratorService collaboratorService;

        @Override
        public Collaborator convert(String source) {
            if (source == null || source.isEmpty()) {
                return null;
            }

            return collaboratorService.findById(new Long(source));
        }


        @Bean
        public ConversionService getConversionService() {
            ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
            Set<Converter> converters = new HashSet<Converter>();
            converters.add(new com.teamtreehouse.instateam.converter.CollaboratorConverter());
            bean.setConverters(converters);
            return bean.getObject();
        }

}

