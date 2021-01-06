package com.welfare.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.MessageDigest;
import java.util.List;

@Controller
public class IndexController {
    private static  final  Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping("/home")
    public String home(){
        logger.info("access home");
        logger.error("error test");
        logger.warn("warn test");
        logger.trace("trace");
        logger.debug("debug");
        return "home";
    }


    @RequestMapping("/errortest")
    @ResponseBody
    public String errtest(PageRequest pageRequest) {
        ObjectMapper om = new ObjectMapper();
        try {
            logger.info("page:", om.writeValueAsString(pageRequest));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        int i = 1 / 0;

        return "success";
    }

}
