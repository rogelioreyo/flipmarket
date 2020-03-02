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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;


/**
 * The Interface UtilitiesService.
 */
public interface UtilitiesService {
	
	/**
	 * Convert to json.
	 *
	 * @param o
	 *            the o
	 * @return the object
	 * @throws JSONException
	 *             the JSON exception
	 */
	Object _convertToJson(Object o) throws JSONException;
	
	/**
	 * Gets the JSON object.
	 *
	 * @param cUrl
	 *            the c url
	 * @param authorization
	 *            the authorization
	 * @return the JSON object
	 * @throws ParseException
	 *             the parse exception
	 * @throws ParseException
	 *             the parse exception
	 * @throws MalformedURLException
	 *             the malformed URL exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	JSONObject getJSONObject(String cUrl, String authorization) throws java.text.ParseException, MalformedURLException, IOException, ParseException;
	
	/**
	 * Gets the JSON object.
	 *
	 * @param cUrl
	 *            the c url
	 * @param authorization
	 *            the authorization
	 * @param customerHeadersMap
	 *            the customer headers map
	 * @return the JSON object
	 * @throws ParseException
	 *             the parse exception
	 * @throws ParseException
	 *             the parse exception
	 * @throws MalformedURLException
	 *             the malformed URL exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	JSONObject getJSONObject(String cUrl, String authorization, Map<String, String> customerHeadersMap) throws java.text.ParseException, MalformedURLException, IOException, ParseException;
	
	/**
	 * Gets the encoded authorization.
	 *
	 * @param credentials
	 *            the credentials
	 * @return the encoded authorization
	 */
	String getEncodedAuthorization(String credentials);
	
	/**
	 * Gets the JSON array.
	 *
	 * @param cUrl
	 *            the c url
	 * @param authorization
	 *            the authorization
	 * @return the JSON array
	 * @throws ParseException
	 *             the parse exception
	 * @throws ParseException
	 *             the parse exception
	 * @throws MalformedURLException
	 *             the malformed URL exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	JSONArray getJSONArray(String cUrl, String authorization) throws java.text.ParseException, MalformedURLException, IOException, ParseException;
	
	/**
	 * Gets the confluence page version number.
	 *
	 * @param cUrl
	 *            the c url
	 * @return the confluence page version number
	 * @throws ParseException
	 *             the parse exception
	 * @throws ParseException
	 *             the parse exception
	 * @throws MalformedURLException
	 *             the malformed URL exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	Long getConfluencePageVersionNumber(String cUrl) throws java.text.ParseException, MalformedURLException, IOException, ParseException;
	
	/**
	 * Maintain confluence page.
	 *
	 * @param method
	 *            the method
	 * @param ancestorId
	 *            the ancestor id
	 * @param pageTitle
	 *            the page title
	 * @param storageValue
	 *            the storage value
	 * @param isUpdate
	 *            the is update
	 * @param spaceKey
	 *            the space key
	 * @return the long
	 * @throws MalformedURLException
	 *             the malformed URL exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ParseException
	 *             the parse exception
	 * @throws ParseException
	 *             the parse exception
	 */
	Long maintainConfluencePage(String method, Long ancestorId, String pageTitle, String storageValue, boolean isUpdate, String spaceKey) throws MalformedURLException, IOException, org.json.simple.parser.ParseException, java.text.ParseException;
	
	/**
	 * Zip it.
	 *
	 * @param zipFile
	 *            the zip file
	 */
	void zipIt(String zipFile);
	
	/**
	 * Generate file list.
	 *
	 * @param node
	 *            the node
	 */
	void generateFileList(File node);
	
	/**
	 * Maintain local yaml files.
	 *
	 * @param environmentCatalog
	 *            the environment catalog
	 */
//	void maintainLocalYamlFiles(EnvironmentCatalog environmentCatalog);
	
	/**
	 * Clean yaml directory files.
	 */
	void cleanYamlDirectoryFiles();
	
	/**
	 * Creates the and store candlestick chart.
	 *
	 * @param serieName
	 *            the serie name
	 * @param fileName
	 *            the file name
	 * @param results
	 *            the results
	 * @param operationIdentifier
	 *            the operation identifier
	 * @return the file
	 * @throws ParseException
	 *             the parse exception
	 */
	File createAndStoreCandlestickChart(String serieName, String fileName, List<Object[]> results, String operationIdentifier) throws java.text.ParseException;
	
	/**
	 * Gets the confluence attachment id.
	 *
	 * @param fileName
	 *            the file name
	 * @param cUrl
	 *            the c url
	 * @return the confluence attachment id
	 * @throws ParseException
	 *             the parse exception
	 * @throws ParseException
	 *             the parse exception
	 * @throws MalformedURLException
	 *             the malformed URL exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	String getConfluenceAttachmentId(String fileName, String cUrl) throws java.text.ParseException, MalformedURLException, IOException, ParseException;
	
	/**
	 * Creates the and store splitted candlestick chart.
	 *
	 * @param serieName
	 *            the serie name
	 * @param fileName
	 *            the file name
	 * @param results
	 *            the results
	 * @param operationIdentifier
	 *            the operation identifier
	 * @return the file
	 * @throws ParseException
	 *             the parse exception
	 */
	File createAndStoreSplittedCandlestickChart(String serieName, String fileName, List<Object[]> results, String operationIdentifier) throws java.text.ParseException;
	Runtime getRuntime();
	void setRuntime(Runtime runtime);
	
	/**
	 * Maintain bitbucket repository.
	 *
	 * @param environmentCatalog
	 *            the environment catalog
	 */
//	void maintainBitbucketRepository(EnvironmentCatalog environmentCatalog);
	
	/**
	 * Execute command.
	 *
	 * @param command
	 *            the command
	 * @param workingDirectory
	 *            the working directory
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	void executeCommand(String command, File workingDirectory) throws IOException, InterruptedException;
	
	/**
	 * Execute command error.
	 *
	 * @param command
	 *            the command
	 * @param workingDirectory
	 *            the working directory
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	void executeCommandError(String command, File workingDirectory) throws IOException, InterruptedException;
	
	/**
	 * Sets the up bluemix apic runtime.
	 *
	 * @param regionHostAcronym
	 *            the new up bluemix apic runtime
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	void setupBluemixApicRuntime(String regionHostAcronym) throws IOException, InterruptedException;
	
	/**
	 * Logout bluemix apic runtime.
	 *
	 * @param regionHostAcronym
	 *            the region host acronym
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	void logoutBluemixApicRuntime(String regionHostAcronym) throws IOException, InterruptedException;
	
	/**
	 * Gets the XML string.
	 *
	 * @param cUrl
	 *            the c url
	 * @param authorization
	 *            the authorization
	 * @return the XML string
	 * @throws MalformedURLException
	 *             the malformed URL exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	String getXMLString(String cUrl, String authorization) throws MalformedURLException, IOException, InterruptedException;
	
	/**
	 * Pretty print.
	 *
	 * @param xml
	 *            the xml
	 * @return the string
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 * @throws SAXException
	 *             the SAX exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws TransformerFactoryConfigurationError
	 *             the transformer factory configuration error
	 * @throws TransformerException
	 *             the transformer exception
	 */
	String prettyPrint(String xml) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException;
	
	Date addDays(Date date, int days);

	Date subtractDays(Date date, int days);
}
