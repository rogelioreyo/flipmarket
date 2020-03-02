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

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.agurno.flipmarket.entity.MarketLog;
import mx.com.agurno.flipmarket.service.RestService;
import mx.com.agurno.flipmarket.service.UtilitiesService;


/**
 * RestServiceImpl - RestServiceImpl.java
 *
 * @author Rogelio Reyo Cachu
 * @version 1.0.0
 * @since 9/06/2018
 */
@Service("restService")
public class RestServiceImpl implements RestService {
	
	/** The Constant LOG. */
	@SuppressWarnings("unused")
	private static final Logger LOG  = Logger.getLogger(RestServiceImpl.class);
	
	/** The utilities service. */
	@Autowired
	private UtilitiesService utilitiesService;
	
	public List<MarketLog> getStatPrices(String itemIds) throws MalformedURLException, ParseException, IOException, org.json.simple.parser.ParseException, InvalidKeyException, NoSuchAlgorithmException, JSONException  {
		String url = "https://" + "www.albion-online-data.com/api/v1/stats/prices/"  + itemIds;
		System.out.println(url);
		JSONArray jsonArray = utilitiesService.getJSONArray(url, null);
		return fillMarketLogList(jsonArray);
	}

	private List<MarketLog> fillMarketLogList(JSONArray jsonArray) throws ParseException, JSONException {
		List<MarketLog> marketLogs = new ArrayList<MarketLog>();
        for(int i=0;i<jsonArray.length();i++)
        {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
	    	marketLogs.add(fillMarketLog(jsonObject));
        }
		return marketLogs;
	}
	
	private MarketLog fillMarketLog(JSONObject jsonObject) {
		MarketLog marketLog = new MarketLog();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		marketLog.setItemId(jsonObject.optString("itemTypeId"));
		marketLog.setCityName(jsonObject.optString("city"));
		marketLog.setQuality(jsonObject.optInt("qualityLevel"));
		marketLog.setSellPriceMin(jsonObject.optInt("sellPriceMin"));
		marketLog.setSellPriceMax(jsonObject.optInt("sellPriceMax"));
		marketLog.setBuyPriceMin(jsonObject.optInt("buyPriceMin"));
		marketLog.setBuyPriceMax(jsonObject.optInt("buyPriceMax"));
		try {
			marketLog.setSellPriceMinDate(simpleDateFormat.parse(jsonObject.optString("sellPriceMinDate")));
			marketLog.setSellPriceMaxDate(simpleDateFormat.parse(jsonObject.optString("sellPriceMaxDate")));
			marketLog.setBuyPriceMinDate(simpleDateFormat.parse(jsonObject.optString("buyPriceMinDate")));
			marketLog.setBuyPriceMaxDate(simpleDateFormat.parse(jsonObject.optString("buyPriceMaxDate")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return marketLog;
	}
	

}
