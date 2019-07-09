package com.bihaoran.o2o.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bihaoran.o2o.cache.JedisUtil;
import com.bihaoran.o2o.cache.JedisUtil.Keys;
import com.bihaoran.o2o.dao.AreaDao;
import com.bihaoran.o2o.entity.Area;
import com.bihaoran.o2o.service.AreaService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AreaServceImpl implements AreaService {
	@Autowired AreaDao areaDao;
	@Autowired 
	private JedisUtil.Keys jedisKeys;
	@Autowired 
	private JedisUtil.Strings jedisStrings;
	
	private static String AREALISTKEY="arealist";
	private static Logger logger=LoggerFactory.getLogger(AreaServceImpl.class);
	

	@Override
	@Transactional
	public List<Area> getAreaList() {
		String key=AREALISTKEY;
		List<Area> areaList=null;
		ObjectMapper mapper=new ObjectMapper();
		if(!jedisKeys.exists(key))
		{
			areaList=areaDao.queryArea();
			String jsonString;
			try {
				jsonString=mapper.writeValueAsString(areaList);
			}catch (JsonProcessingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new RuntimeException(e.getMessage());
			}
			jedisStrings.set(key,jsonString);
		}else
		{
			String jsonString=jedisStrings.get(key);
			JavaType javaType=mapper.getTypeFactory().constructParametricType(ArrayList.class,Area.class);
			try {
				areaList=mapper.readValue(jsonString, javaType);
			} catch (JsonParseException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new RuntimeException(e.getMessage());
			} catch (JsonMappingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new RuntimeException(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new RuntimeException(e.getMessage());
			}
		}
		return areaList;
		
	}
}
