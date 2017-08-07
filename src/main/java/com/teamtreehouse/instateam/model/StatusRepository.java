package com.teamtreehouse.instateam.model;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Rumy on 7/24/2017.
 */
@Component
public class StatusRepository {
       private static final List<String> PROJECT_STATUS= Arrays.asList("Active","On hold","Archived","Recruiting");

       public List<String> getStatus(){
            return PROJECT_STATUS;
        }

}
