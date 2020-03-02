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
package mx.com.agurno.flipmarket.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.List;

import org.json.JSONException;

import mx.com.agurno.flipmarket.entity.MarketLog;



/**
 * The Interface RestService.
 */
public interface RestService {
	
	List<MarketLog> getStatPrices(String itemIds) throws MalformedURLException, ParseException, IOException, org.json.simple.parser.ParseException, InvalidKeyException, NoSuchAlgorithmException, JSONException;
}
