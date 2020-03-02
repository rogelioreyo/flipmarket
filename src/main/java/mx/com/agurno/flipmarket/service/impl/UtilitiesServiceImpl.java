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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.ui.RectangleInsets;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import mx.com.agurno.flipmarket.service.UtilitiesService;


/**
 * UtilitiesServiceImpl - UtilitiesServiceImpl.java
 *
 * @author Rogelio Reyo Cachu
 * @version 1.0.0
 * @since 9/06/2018
 */
@Service("utilitiesService")
public class UtilitiesServiceImpl implements UtilitiesService {

	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(UtilitiesServiceImpl.class);

	/** The file list. */
	private List<String> fileList;
	
	/** The runtime. */
	Runtime runtime;
	
	/** The date formatter file. */
	private static SimpleDateFormat dateFormatterFile = new SimpleDateFormat("MM-dd-yyyy");

	/**
	 * Instantiates a new utilities service impl.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public UtilitiesServiceImpl() throws IOException, InterruptedException {
		fileList = new ArrayList<String>();
		runtime = Runtime.getRuntime();
		LOG.info("Initilializing Runtime Environment Utilities...");
	}


	/**
	 * Convert to json.
	 *
	 * @param o
	 *            the o
	 * @return the object
	 * @throws JSONException
	 *             the JSON exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object _convertToJson(Object o) throws JSONException {
		if (o instanceof Map) {
			Map<Object, Object> map = (Map<Object, Object>) o;
			JSONObject result = new JSONObject();
			for (Map.Entry<Object, Object> stringObjectEntry : map.entrySet()) {
				String key = stringObjectEntry.getKey().toString();
				result.put(key, _convertToJson(stringObjectEntry.getValue()));
			}
			return result;
		} else if (o instanceof ArrayList) {
			ArrayList arrayList = (ArrayList) o;
			JSONArray result = new JSONArray();
			for (Object arrayObject : arrayList) {
				result.put(_convertToJson(arrayObject));
			}
			return result;
		} else if (o instanceof String) {
			return o;
		} else if (o instanceof Boolean) {
			return o;
		} else {
			return o;
		}
	}

	/**
	 * Gets the encoded authorization.
	 *
	 * @param credentials
	 *            the credentials
	 * @return the encoded authorization
	 */
	public String getEncodedAuthorization(String credentials) {
		return "Basic " + java.util.Base64.getEncoder().encodeToString((credentials).getBytes(StandardCharsets.UTF_8));
	}

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
	public String getXMLString(String cUrl, String authorization) throws MalformedURLException, IOException, InterruptedException {
		HttpsURLConnection connection = getConnection(new URL(cUrl), authorization);
		try {
			int responseCode = connection.getResponseCode();
	        switch (responseCode) {
	            case 404:
	            	return "";
	            case 204:
	            	return "";
	        }
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine).append("\n");
			}
			in.close();
			return response.toString().substring(response.toString().indexOf("<"), response.toString().length());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}
		return "";
	}
	
	/**
	 * Gets the connection.
	 *
	 * @param url
	 *            the url
	 * @param authorization
	 *            the authorization
	 * @return the connection
	 * @throws InterruptedException
	 *             the interrupted exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static final HttpsURLConnection getConnection(URL url, String authorization) throws InterruptedException, IOException{
	    int retry = 0;
	    boolean delay = false;
	    long RETRY_DELAY_MS = 20L;
	    long RETRIES = 100;
	    do {
	        if (delay) {
	            Thread.sleep(RETRY_DELAY_MS);
	        }
	        HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
			if (authorization != null) {
				connection.setRequestProperty("Authorization", authorization);
			}
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Connection", "keep-alive");
	        switch (connection.getResponseCode()) {
	            case HttpsURLConnection.HTTP_OK:
	                LOG.info(url + " **OK**");
	                return connection;
	            case HttpsURLConnection.HTTP_NOT_FOUND:
	                LOG.info(url + " **OK**");
	                return connection;
	            case HttpsURLConnection.HTTP_NO_CONTENT:
	                LOG.info(url + " **OK**");
	                return connection;
	            case HttpsURLConnection.HTTP_GATEWAY_TIMEOUT:
	            	LOG.warn(url + " **gateway timeout**");
	                break;
	            case HttpsURLConnection.HTTP_UNAVAILABLE:
	            	LOG.warn(url + "**unavailable**");
	                break;
	            default:
	            	LOG.fatal(url + " **unknown response code**." + connection.getResponseCode());
	                break;
	        }
	        connection.disconnect();
	        retry++;
	        LOG.warn("Failed retry " + retry + "/" + RETRIES);
	        delay = true;
	    } while (retry < RETRIES);
	    LOG.fatal("Aborting download of policy.");
	    return null;
	}
	
	public Date addDays(Date date, int days) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
				
		return cal.getTime();
	}

	public Date subtractDays(Date date, int days) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, -days);
				
		return cal.getTime();
	}
	
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
	public String prettyPrint(String xml) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
		if(xml != null && !xml.trim().isEmpty()) {
			try {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				documentBuilderFactory.setValidating(false);
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = documentBuilder.parse(new InputSource(new StringReader(xml)));
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				Writer writer = new StringWriter();
				transformer.transform(new DOMSource(document), new StreamResult(writer));
				xml = writer.toString();
			} catch (SAXParseException spe) {
				LOG.error(xml);
				return xml;
			}
		}
		return xml;
	}

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
	public JSONObject getJSONObject(String cUrl, String authorization, Map<String, String> customerHeadersMap)
			throws java.text.ParseException, MalformedURLException, IOException, ParseException {
		HttpsURLConnection connection = (HttpsURLConnection) new URL(cUrl).openConnection();
		try {
			for (Map.Entry<String, String> entry : customerHeadersMap.entrySet()) {
				connection.setRequestProperty(entry.getKey(), entry.getValue());
			}
			if (authorization != null) {
				connection.setRequestProperty("Authorization", authorization);
			}
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Connection", "keep-alive");
			int responseCode = connection.getResponseCode();
			LOG.info("Response Code : " + responseCode);
	        switch (responseCode) {
            case 404:
            	return null;
	        }
			org.json.simple.parser.JSONParser jsonParser = new org.json.simple.parser.JSONParser();
			org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) jsonParser
					.parse(new InputStreamReader((InputStream) connection.getContent(), "UTF-8"));
			return new org.json.JSONObject(jsonObject.toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}
		return null;
	}
	
	

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
	public JSONObject getJSONObject(String cUrl, String authorization)
			throws java.text.ParseException, MalformedURLException, IOException, ParseException {
		HttpsURLConnection connection = (HttpsURLConnection) new URL(cUrl).openConnection();
		try {
			if (authorization != null) {
				connection.setRequestProperty("Authorization", authorization);
			}
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Connection", "keep-alive");
			int responseCode = connection.getResponseCode();
			LOG.info("Response Code : " + responseCode);
	        switch (responseCode) {
            case 404:
            	return null;
	        }
			org.json.simple.parser.JSONParser jsonParser = new org.json.simple.parser.JSONParser();
			org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) jsonParser
					.parse(new InputStreamReader((InputStream) connection.getContent(), StandardCharsets.ISO_8859_1));
			return new org.json.JSONObject(jsonObject.toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}
		return null;
	}

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
	public JSONArray getJSONArray(String cUrl, String authorization)
			throws java.text.ParseException, MalformedURLException, IOException, ParseException {
		HttpURLConnection connection = (HttpURLConnection) new URL(cUrl).openConnection();
		try {
			if (authorization != null) {
				connection.setRequestProperty("Authorization", authorization);
			}
			org.json.simple.parser.JSONParser jsonParser = new org.json.simple.parser.JSONParser();
			org.json.simple.JSONArray jsonArray = (org.json.simple.JSONArray) jsonParser
					.parse(new InputStreamReader((InputStream) connection.getContent(), "UTF-8"));
			return new org.json.JSONArray(jsonArray.toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}
		return null;
	}


	/**
	 * Clean yaml directory files.
	 */
	public void cleanYamlDirectoryFiles() {
		for (String file : this.fileList) {
			File yamlFile = new File(file);
			yamlFile.delete();
		}
		fileList = new ArrayList<String>();
	}

	/**
	 * Zip it.
	 *
	 * @param zipFile
	 *            the zip file
	 */
	public void zipIt(String zipFile) {
		LOG.info("Generating a local backup on " + this.fileList.size() + " YAML Files");
		byte[] buffer = new byte[1024];
		FileOutputStream fileOutputStreamos = null;
		ZipOutputStream zipOutputStreamos = null;
		try {
			fileOutputStreamos = new FileOutputStream(zipFile);
			zipOutputStreamos = new ZipOutputStream(fileOutputStreamos);
			FileInputStream inputStreamn = null;
			for (String file : this.fileList) {
				// LOG.info("File Added : " + file);
				ZipEntry zipEntrye = new ZipEntry(file);
				zipOutputStreamos.putNextEntry(zipEntrye);
				try {
					inputStreamn = new FileInputStream(new File(file));
					int len;
					while ((len = inputStreamn.read(buffer)) > 0) {
						zipOutputStreamos.write(buffer, 0, len);
					}
				} finally {
					inputStreamn.close();
				}
			}
			zipOutputStreamos.closeEntry();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				zipOutputStreamos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * Generate file list.
	 *
	 * @param node
	 *            the node
	 */
	public void generateFileList(File node) {
		if (node.isFile()) {
			this.fileList.add(node.toString());
		}
		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(new File(node, filename));
			}
		}
	}

	/** The ohlc series. */
	private OHLCSeries ohlcSeries;
	
	/** The volume series. */
	private TimeSeries volumeSeries;
	
	/** The bytes sent. */
	private TimeSeries bytesSent;

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
	public File createAndStoreSplittedCandlestickChart(String serieName, String fileName, List<Object[]> results,
			String operationIdentifier) throws java.text.ParseException {
		LOG.info("Rendering new Candle Stick Chart for: " + operationIdentifier + " with " + results.size()
				+ " database results.");
		File outputfile = null;

		/**
		 * Creating candlestick subplot
		 */
		// Create OHLCSeriesCollection as a price dataset for candlestick chart
		OHLCSeriesCollection candlestickDataset = new OHLCSeriesCollection();
		/**
		 * Creating volume subplot
		 */
		// creates TimeSeriesCollection as a volume dataset for volume chart
		TimeSeriesCollection volumeDataset = new TimeSeriesCollection();
		/**
		 * Creating volume subplot
		 */
		// creates TimeSeriesCollection as a volume dataset for volume chart
		TimeSeriesCollection bytesSentDataset = new TimeSeriesCollection();

		produceDataset(serieName, results);

		bytesSentDataset.addSeries(bytesSent);
		volumeDataset.addSeries(volumeSeries);
		candlestickDataset.addSeries(ohlcSeries);
		// Create candlestick chart priceAxis
		NumberAxis responseAxis = new NumberAxis(serieName);
		responseAxis.setAutoRangeIncludesZero(true);
		// Create candlestick chart renderer
		CandlestickRenderer candlestickRenderer = new CandlestickRenderer(CandlestickRenderer.WIDTHMETHOD_AVERAGE);
		candlestickRenderer.setDrawVolume(true);
		candlestickRenderer.setSeriesPaint(0, new Color(29, 143, 147));
		candlestickRenderer.setUpPaint(new Color(109, 185, 95));
		candlestickRenderer.setDownPaint(new Color(238, 94, 77));
		candlestickRenderer.setVolumePaint(new Color(29, 143, 147));

		// Create candlestickSubplot
		XYPlot candlestickSubplot = new XYPlot(candlestickDataset, null, responseAxis, candlestickRenderer);
		candlestickSubplot.setBackgroundPaint(Color.white);
		candlestickSubplot.setDomainPannable(true);
		candlestickSubplot.setRangePannable(true);
		candlestickSubplot.setForegroundAlpha(0.85f);
		candlestickSubplot.setDomainGridlinePaint(Color.white);
		candlestickSubplot.setRangeGridlinePaint(Color.white);
		candlestickSubplot.setOutlineVisible(false);
		candlestickSubplot.getRenderer().setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
		candlestickSubplot.getRenderer().setBaseItemLabelsVisible(true);

		NumberAxis yAxis = (NumberAxis) candlestickSubplot.getRangeAxis();
		yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		/**
		 * Creating volume subplot
		 */
		// creates TimeSeriesCollection as a volume dataset for volume chart
		// Create volume chart volumeAxis
		XYBarRenderer timeRenderer = new XYBarRenderer();
		timeRenderer.setShadowVisible(false);
		XYBarRenderer bytesRenderer = new XYBarRenderer();
		bytesRenderer.setShadowVisible(false);

		NumberAxis volumeAxis = new NumberAxis("Total API Calls");
		volumeAxis.setAutoRangeIncludesZero(false);
		volumeAxis.setNumberFormatOverride(new DecimalFormat("0"));
		XYPlot volumeSubplot = new XYPlot(volumeDataset, null, volumeAxis, timeRenderer);
		volumeSubplot.setBackgroundPaint(Color.white);

		NumberAxis bytesSentAxis = new NumberAxis("Total Bytes Sent");
		bytesSentAxis.setAutoRangeIncludesZero(false);
		bytesSentAxis.setNumberFormatOverride(new DecimalFormat("0"));
		XYPlot bytesSentSubplot = new XYPlot(bytesSentDataset, null, bytesSentAxis, bytesRenderer);
		bytesSentSubplot.setBackgroundPaint(Color.white);

		/**
		 * Create chart main plot with two subplots (candlestickSubplot, volumeSubplot)
		 * and one common dateAxis
		 */
		// Creating charts common dateAxis
		DateAxis dateAxis = new DateAxis("Time");
		dateAxis.setDateFormatOverride(new SimpleDateFormat("ddMMyy"));
		// reduce the default left/right margin from 0.05 to 0.02
		dateAxis.setLowerMargin(0.02);
		dateAxis.setUpperMargin(0.02);
		// Create mainPlot
		CombinedDomainXYPlot mainPlot = new CombinedDomainXYPlot(dateAxis);
		mainPlot.setGap(10.0);
		mainPlot.add(candlestickSubplot, 3);
		mainPlot.add(volumeSubplot, 1);
		mainPlot.add(bytesSentSubplot, 1);
		mainPlot.setOrientation(PlotOrientation.VERTICAL);

		JFreeChart chart = new JFreeChart(operationIdentifier, JFreeChart.DEFAULT_TITLE_FONT, mainPlot, true);
		// chart.removeLegend();
		chart.getLegend().setFrame(BlockBorder.NONE);
		chart.getLegend().setItemLabelPadding(new RectangleInsets(5.0, 2.0, 10.0, 400));
		chart.getLegend().setPadding(new RectangleInsets(20.0, 20.0, 0.0, 0.0));

		BufferedImage objBufferedImage = chart.createBufferedImage(1200, 700);
		ByteArrayOutputStream bas = new ByteArrayOutputStream();
		try {
			ImageIO.write(objBufferedImage, "png", bas);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] byteArray = bas.toByteArray();
		InputStream in = new ByteArrayInputStream(byteArray);
		try {
			BufferedImage image = ImageIO.read(in);
			outputfile = new File("resources/operationcharts/" + fileName + ".png");
			ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputfile;
	}

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
	public File createAndStoreCandlestickChart(String serieName, String fileName, List<Object[]> results,
			String operationIdentifier) throws java.text.ParseException {
		LOG.info("Rendering new Candle Stick Chart for: " + operationIdentifier + " with " + results.size()
				+ " database results.");
		File outputfile = null;
		final DefaultHighLowDataset dataset = produceDataset(serieName, results);
		final JFreeChart chart = ChartFactory.createCandlestickChart(operationIdentifier, "Time", "API Calls", dataset,
				true);

		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setDomainPannable(true);
		plot.setRangePannable(true);
		plot.setForegroundAlpha(0.85f);

		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setOutlineVisible(false);

		NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
		yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		CandlestickRenderer renderer = (CandlestickRenderer) plot.getRenderer();
		renderer.setDrawVolume(true);
		renderer.setSeriesPaint(0, new Color(29, 143, 147));
		renderer.setUpPaint(new Color(109, 185, 95));
		renderer.setDownPaint(new Color(238, 94, 77));
		renderer.setVolumePaint(new Color(29, 143, 147));
		plot.setRenderer(renderer);

		chart.getLegend().setFrame(BlockBorder.NONE);
		chart.getLegend().setItemLabelPadding(new RectangleInsets(5.0, 2.0, 10.0, 400));
		chart.getLegend().setPadding(new RectangleInsets(20.0, 20.0, 0.0, 0.0));

		plot.getRenderer().setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
		plot.getRenderer().setBaseItemLabelsVisible(true);

		ValueAxis valueAxis = plot.getDomainAxis();
		valueAxis.setTickLabelPaint(new Color(160, 163, 165));
		valueAxis.setLowerMargin(0);
		valueAxis.setUpperMargin(0);

		BufferedImage objBufferedImage = chart.createBufferedImage(800, 600);
		ByteArrayOutputStream bas = new ByteArrayOutputStream();
		try {
			ImageIO.write(objBufferedImage, "png", bas);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] byteArray = bas.toByteArray();
		InputStream in = new ByteArrayInputStream(byteArray);
		try {
			BufferedImage image = ImageIO.read(in);
			outputfile = new File("resources/operationcharts/" + fileName + ".png");
			ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputfile;
	}

	/**
	 * Produce dataset.
	 *
	 * @param serieName
	 *            the serie name
	 * @param results
	 *            the results
	 * @return the default high low dataset
	 * @throws ParseException
	 *             the parse exception
	 */
	private DefaultHighLowDataset produceDataset(String serieName, List<Object[]> results)
			throws java.text.ParseException {

		double[] orderedMax = new double[results.size()];
		double[] orderedMedian = new double[results.size()];
		double[] orderedMin = new double[results.size()];
		double[] orderedAverage = new double[results.size()];
		int sumCount = 0;
		int bytesSent = 0;
		// COUNT(*), MEDIAN(TIME_TO_SERVE_REQUEST), MAX(TIME_TO_SERVE_REQUEST),
		// MIN(TIME_TO_SERVE_REQUEST), AVG(TIME_TO_SERVE_REQUEST)
		for (int j = 0, sizeJ = results.size(); j < sizeJ; j++) {
			Object[] result = results.get(j);
			sumCount += ((Number) result[1]).doubleValue();
			orderedMedian[j] = ((Number) result[2]).doubleValue();
			orderedMax[j] = ((Number) result[3]).doubleValue();
			orderedMin[j] = ((Number) result[4]).doubleValue();
			orderedAverage[j] = ((Number) result[5]).doubleValue();
			bytesSent += ((Number) result[6]).doubleValue();
		}
		double[] standardDeviation = ArrayUtils.addAll(orderedMin, orderedMax);
		standardDeviation = ArrayUtils.addAll(standardDeviation, orderedMedian);
		standardDeviation = ArrayUtils.addAll(standardDeviation, orderedAverage);

		Arrays.sort(orderedMax);
		Arrays.sort(orderedMedian);
		Arrays.sort(orderedMin);
		Arrays.sort(orderedAverage);

		this.volumeSeries = new TimeSeries("Total API Calls: " + sumCount);
		this.bytesSent = new TimeSeries("Total Bytes Sent: " + bytesSent);
		this.ohlcSeries = new OHLCSeries(serieName + "  HIGH: " + (orderedMax[orderedMax.length - 1]) + ", LOW: "
				+ (orderedMin[0]) + ", MEDIAN: " + calculareArrayAverage(orderedMedian) + ", AVERAGE: "
				+ calculareArrayAverage(orderedAverage));
		Date[] dates = new Date[results.size()];
		double[] count = new double[results.size()];
		double[] median = new double[results.size()];
		double[] max = new double[results.size()];
		double[] min = new double[results.size()];
		double[] average = new double[results.size()];

		for (int j = 0, sizeJ = results.size(); j < sizeJ; j++) {
			Object[] result = results.get(j);
			dates[j] = dateFormatterFile.parse((String) result[0]);
			count[j] = ((Number) result[1]).doubleValue();
			median[j] = ((Number) result[2]).doubleValue();
			max[j] = ((Number) result[3]).doubleValue();
			min[j] = ((Number) result[4]).doubleValue();
			average[j] = ((Number) result[5]).doubleValue();
			FixedMillisecond time = new FixedMillisecond(dateFormatterFile.parse((String) result[0]));

			// Remove max clandles above global standard deviation
			// double maxValue = ((Number)result[3]).doubleValue();
			// if(maxValue > serviceStandardDeviation)
			// maxValue = serviceStandardDeviation;

			this.ohlcSeries.add(time, ((Number) result[2]).doubleValue(), ((Number) result[3]).doubleValue(),
					((Number) result[4]).doubleValue(), ((Number) result[5]).doubleValue());
			this.volumeSeries.add(time, ((Number) result[1]).doubleValue());
			this.bytesSent.add(time, ((Number) result[6]).doubleValue());
		}

		return new DefaultHighLowDataset(serieName + "  HIGH: " + (orderedMax[orderedMax.length - 1]) + ", LOW: "
				+ (orderedMin[0]) + ", MEDIAN: " + calculareArrayAverage(orderedMedian) + ", AVERAGE: "
				+ calculareArrayAverage(orderedAverage), dates, max, min, median, average, count);
	}

	/**
	 * Calculare array average.
	 *
	 * @param doubleArray
	 *            the double array
	 * @return the double
	 */
	private double calculareArrayAverage(double[] doubleArray) {
		double median;
		if (doubleArray.length % 2 == 0)
			median = ((double) doubleArray[doubleArray.length / 2] + (double) doubleArray[doubleArray.length / 2 - 1])
					/ 2;
		else
			median = (double) doubleArray[doubleArray.length / 2];
		return median;
	}

	public Runtime getRuntime() {
		return runtime;
	}

	public void setRuntime(Runtime runtime) {
		this.runtime = runtime;
	}

	/**
	 * Standard deviation.
	 *
	 * @param values
	 *            the values
	 * @return the double
	 */
	public double standardDeviation(double[] values) {
		double sum = 0;
		double finalsum = 0;
		double average = 0;
		for (double i : values) {
			finalsum = (sum += i);
		}
		average = finalsum / (values.length);
		double sumX = 0;
		double finalsumX = 0;
		double[] x1_average = new double[2000];
		for (int i = 0; i < values.length; i++) {
			double fvalue = (Math.pow((values[i] - average), 2));
			x1_average[i] = fvalue;
		}
		for (double i : x1_average) {
			finalsumX = (sumX += i);
		}
		Double AverageX = finalsumX / (values.length);
		double SquareRoot = Math.sqrt(AverageX);
		return SquareRoot;
	}

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
	public void executeCommand(String command, File workingDirectory) throws IOException, InterruptedException {
		LOG.info("Executing the following command: " + command);
		this.runtime = Runtime.getRuntime();
		Process process = this.runtime.exec(command, null, workingDirectory);
		String line;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		while ((line = bufferedReader.readLine()) != null) {
			LOG.info(line);
		}
		process.waitFor();
		process.destroyForcibly();
	}

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
	public void executeCommandError(String command, File workingDirectory) throws IOException, InterruptedException {
		LOG.info("Executing the following command: " + command);
		this.runtime = Runtime.getRuntime();
		Process process = this.runtime.exec("git config core.autocrlf true", null, workingDirectory);
		String line;
		BufferedReader bufferedReaderError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		while ((line = bufferedReaderError.readLine()) != null) {
			LOG.info(line);
		}
		process = this.runtime.exec(command, null, workingDirectory);
		bufferedReaderError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		while ((line = bufferedReaderError.readLine()) != null) {
			LOG.info(line);
		}
		process.waitFor();
		process.destroyForcibly();
	}


	@Override
	public Long getConfluencePageVersionNumber(String cUrl)
			throws java.text.ParseException, MalformedURLException, IOException, ParseException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Long maintainConfluencePage(String method, Long ancestorId, String pageTitle, String storageValue,
			boolean isUpdate, String spaceKey)
			throws MalformedURLException, IOException, ParseException, java.text.ParseException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getConfluenceAttachmentId(String fileName, String cUrl)
			throws java.text.ParseException, MalformedURLException, IOException, ParseException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setupBluemixApicRuntime(String regionHostAcronym) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void logoutBluemixApicRuntime(String regionHostAcronym) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
	}

}
