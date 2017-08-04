/*Copyright (c) 2017-2018 vanenburgsoftware.com All Rights Reserved.
 This software is the confidential and proprietary information of vanenburgsoftware.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with vanenburgsoftware.com*/
package com.testloginapp.hoursaccont1.service;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wavemaker.runtime.data.dao.WMGenericDao;
import com.wavemaker.runtime.data.exception.EntityNotFoundException;
import com.wavemaker.runtime.data.export.ExportType;
import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.model.AggregationInfo;
import com.wavemaker.runtime.file.model.Downloadable;

import com.testloginapp.hoursaccont1.Login;


/**
 * ServiceImpl object for domain model class Login.
 *
 * @see Login
 */
@Service("HoursAccOnt1.LoginService")
public class LoginServiceImpl implements LoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);


    @Autowired
    @Qualifier("HoursAccOnt1.LoginDao")
    private WMGenericDao<Login, Integer> wmGenericDao;

    public void setWMGenericDao(WMGenericDao<Login, Integer> wmGenericDao) {
        this.wmGenericDao = wmGenericDao;
    }

    @Transactional(value = "HoursAccOnt1TransactionManager")
    @Override
	public Login create(Login login) {
        LOGGER.debug("Creating a new Login with information: {}", login);
        Login loginCreated = this.wmGenericDao.create(login);
        return loginCreated;
    }

	@Transactional(readOnly = true, value = "HoursAccOnt1TransactionManager")
	@Override
	public Login getById(Integer loginId) throws EntityNotFoundException {
        LOGGER.debug("Finding Login by id: {}", loginId);
        Login login = this.wmGenericDao.findById(loginId);
        if (login == null){
            LOGGER.debug("No Login found with id: {}", loginId);
            throw new EntityNotFoundException(String.valueOf(loginId));
        }
        return login;
    }

    @Transactional(readOnly = true, value = "HoursAccOnt1TransactionManager")
	@Override
	public Login findById(Integer loginId) {
        LOGGER.debug("Finding Login by id: {}", loginId);
        return this.wmGenericDao.findById(loginId);
    }


	@Transactional(rollbackFor = EntityNotFoundException.class, value = "HoursAccOnt1TransactionManager")
	@Override
	public Login update(Login login) throws EntityNotFoundException {
        LOGGER.debug("Updating Login with information: {}", login);
        this.wmGenericDao.update(login);

        Integer loginId = login.getId();

        return this.wmGenericDao.findById(loginId);
    }

    @Transactional(value = "HoursAccOnt1TransactionManager")
	@Override
	public Login delete(Integer loginId) throws EntityNotFoundException {
        LOGGER.debug("Deleting Login with id: {}", loginId);
        Login deleted = this.wmGenericDao.findById(loginId);
        if (deleted == null) {
            LOGGER.debug("No Login found with id: {}", loginId);
            throw new EntityNotFoundException(String.valueOf(loginId));
        }
        this.wmGenericDao.delete(deleted);
        return deleted;
    }

	@Transactional(readOnly = true, value = "HoursAccOnt1TransactionManager")
	@Override
	public Page<Login> findAll(QueryFilter[] queryFilters, Pageable pageable) {
        LOGGER.debug("Finding all Logins");
        return this.wmGenericDao.search(queryFilters, pageable);
    }

    @Transactional(readOnly = true, value = "HoursAccOnt1TransactionManager")
    @Override
    public Page<Login> findAll(String query, Pageable pageable) {
        LOGGER.debug("Finding all Logins");
        return this.wmGenericDao.searchByQuery(query, pageable);
    }

    @Transactional(readOnly = true, value = "HoursAccOnt1TransactionManager")
    @Override
    public Downloadable export(ExportType exportType, String query, Pageable pageable) {
        LOGGER.debug("exporting data in the service HoursAccOnt1 for table Login to {} format", exportType);
        return this.wmGenericDao.export(exportType, query, pageable);
    }

	@Transactional(readOnly = true, value = "HoursAccOnt1TransactionManager")
	@Override
	public long count(String query) {
        return this.wmGenericDao.count(query);
    }

    @Transactional(readOnly = true, value = "HoursAccOnt1TransactionManager")
	@Override
    public Page<Map<String, Object>> getAggregatedValues(AggregationInfo aggregationInfo, Pageable pageable) {
        return this.wmGenericDao.getAggregatedValues(aggregationInfo, pageable);
    }



}

