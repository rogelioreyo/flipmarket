/****************************************************************************
 * 
 * Copyright (C) CEMEX S.A.B de C.V 2018, Inc - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * 
 * Proprietary and confidential.
 * 
 * Written by Rogelio Reyo Cachu, 9/06/2018
 * 
 * We keep our License Statement under regular review and reserve the right 
 * to modify this License Statement from time to time.
 * 
 * Should you have any questions or comments about any of the above, 
 * please contact ethos@cemex.com for assistance or visit www.cemex.com 
 * if you need additional information or have any questions.
 * 
 ****************************************************************************/
package mx.com.agurno.flipmarket.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import mx.com.agurno.flipmarket.entity.MarketLog;
import mx.com.agurno.flipmarket.repository.MarketLogRepository;
import mx.com.agurno.flipmarket.service.MarketLogService;


/**
 * PropertyServiceImpl - PropertyServiceImpl.java
 *
 * @author Rogelio Reyo Cachu
 * @version 1.0.0
 * @since 9/06/2018
 */
@Named("marketLogService")
public class MarketLogServiceImpl extends CrudServiceImpl<MarketLog, Long, MarketLogRepository> implements MarketLogService {
	
	/** The Constant LOG. */
	@SuppressWarnings("unused")
	private static final Logger LOG  = Logger.getLogger(MarketLogServiceImpl.class);
    
    @Override
    @Inject
    public void setRepository(MarketLogRepository marketLogRepository) {
        super.setRepository(marketLogRepository);
    }
    
    public void truncateMarketLog() {
    	this.repository.truncateMarketLog();
    }
	
}