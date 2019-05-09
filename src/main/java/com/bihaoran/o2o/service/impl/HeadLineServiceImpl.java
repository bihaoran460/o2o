package com.bihaoran.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bihaoran.o2o.dao.HeadLineDao;
import com.bihaoran.o2o.entity.HeadLine;
import com.bihaoran.o2o.service.HeadLineService;

@Service
public class HeadLineServiceImpl implements HeadLineService {
	@Autowired
	private HeadLineDao headLineDao;

	@Override
	public List<HeadLine> getHeadLineList(HeadLine headLineCondition) {
		// TODO Auto-generated method stub
		return headLineDao.queryHeadLine(headLineCondition);
	}

}
