/*Copyright (c) 2017-2018 vanenburgsoftware.com All Rights Reserved.
 This software is the confidential and proprietary information of vanenburgsoftware.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with vanenburgsoftware.com*/

package com.testloginapp.hoursaccont1.service;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wavemaker.runtime.data.dao.query.WMQueryExecutor;

@Service
public class HoursAccOnt1QueryExecutorServiceImpl implements HoursAccOnt1QueryExecutorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HoursAccOnt1QueryExecutorServiceImpl.class);

    @Autowired
    @Qualifier("HoursAccOnt1WMQueryExecutor")
    private WMQueryExecutor queryExecutor;


}


