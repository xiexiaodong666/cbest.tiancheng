package com.welfare.serviceaccount.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.welfare.serviceaccount.BaseTest;
import java.util.Map;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

public abstract class AbstractBaseControllerTest extends BaseTest {

    protected MockMvc mvc;

    @Autowired
    public WebApplicationContext webApplicationConnect;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationConnect).build();
    }

    protected <T> void post(String url, T obj) {
        try {
            postWithJson(url, JSON.toJSONString(obj));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected <T> void postWithJson(String url, String json) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
            .post(url);
        setHttpHeaders(builder);
        mvc.perform(builder
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk())
            .andDo(print());
    }

    protected <T> void put(String url, T obj) {
        try {
            putWithJson(url, JSON.toJSONString(obj));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected <T> void putWithJson(String url, String json) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
            .put(url);
        setHttpHeaders(builder);
        mvc.perform(builder
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk())
            .andDo(print());
    }

    protected void get(String url) {
        try {
            get(url, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void get(String url, MultiValueMap<String, String> params) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url);
        if (params != null) {
            builder.params(params);
        }
        setHttpHeaders(builder);
        mvc.perform(builder)
            .andExpect(status().isOk())
            .andDo(print());
    }

    private void setHttpHeaders(MockHttpServletRequestBuilder builder) {
        Map<String, String> headers = getHeaders();
        if (CollUtil.isNotEmpty(headers)) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAll(headers);
            builder.headers(httpHeaders);
        }
    }

    protected abstract Map<String, String> getHeaders();
}
