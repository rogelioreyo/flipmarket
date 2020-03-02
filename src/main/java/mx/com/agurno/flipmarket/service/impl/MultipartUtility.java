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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;

import org.apache.log4j.Logger;

import mx.com.agurno.flipmarket.IFlipMarketConstants;



/**
 * MultipartUtility - MultipartUtility.java
 *
 * @author Rogelio Reyo Cachu
 * @version 1.0.0
 * @since 9/06/2018
 */
public class MultipartUtility {
	
	/** The Constant LOG. */
	private static final Logger LOG  = Logger.getLogger(MultipartUtility.class);
	
	/** The http conn. */
	private HttpURLConnection httpConn;
	
	/** The request. */
	private DataOutputStream request;

	/**
	 * Adds the form field.
	 *
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void addFormField(String name, String value) throws IOException {
		request.writeBytes(IFlipMarketConstants.CRLF + IFlipMarketConstants.CRLF + IFlipMarketConstants.TWO_HYPHENS + IFlipMarketConstants.BOUNDARY + IFlipMarketConstants.CRLF);
		request.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + IFlipMarketConstants.CRLF);
		request.writeBytes(IFlipMarketConstants.CRLF);
		request.writeBytes(value + IFlipMarketConstants.CRLF);
		request.flush();
	}

	/**
	 * Adds the file part.
	 *
	 * @param fieldName
	 *            the field name
	 * @param uploadFile
	 *            the upload file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void addFilePart(String fieldName, File uploadFile) throws IOException {
		String fileName = uploadFile.getName();
		request.writeBytes(IFlipMarketConstants.TWO_HYPHENS + IFlipMarketConstants.BOUNDARY + IFlipMarketConstants.CRLF);
		request.writeBytes("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"" + IFlipMarketConstants.CRLF);
		request.writeBytes("Content-Type: " + Files.probeContentType(uploadFile.toPath()) + IFlipMarketConstants.CRLF);
		request.writeBytes(IFlipMarketConstants.CRLF);
		byte[] bytes = Files.readAllBytes(uploadFile.toPath());
		request.write(bytes);
	}

	/**
	 * Adds the string as bytes part.
	 *
	 * @param filePath
	 *            the file path
	 * @param str
	 *            the str
	 * @param fileName
	 *            the file name
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void addStringAsBytesPart(String filePath, String str, String fileName) throws IOException {
		request.writeBytes(IFlipMarketConstants.TWO_HYPHENS + IFlipMarketConstants.BOUNDARY + IFlipMarketConstants.CRLF);
		request.writeBytes("Content-Disposition: form-data; name=\"" + filePath + "\"; filename=\"" + fileName + "\"" + IFlipMarketConstants.CRLF);
		request.writeBytes("Content-Type: text/plain" + IFlipMarketConstants.CRLF);
		request.writeBytes(IFlipMarketConstants.CRLF);
		byte[] bytes = str.getBytes(Charset.forName("UTF-8"));
		request.write(bytes);
	}

	/**
	 * Finish.
	 *
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public String finish() throws IOException {
		String response = "";
		request.writeBytes(IFlipMarketConstants.TWO_HYPHENS + IFlipMarketConstants.BOUNDARY + IFlipMarketConstants.TWO_HYPHENS + IFlipMarketConstants.CRLF);
		request.flush();
		request.close();
		int status = httpConn.getResponseCode();
		if (status == HttpURLConnection.HTTP_OK || status == HttpURLConnection.HTTP_CREATED) {
			InputStream responseStream = new BufferedInputStream(httpConn.getInputStream());
			BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
			String line = "";
			StringBuilder stringBuilder = new StringBuilder();
			while ((line = responseStreamReader.readLine()) != null) {
				stringBuilder.append(line).append("\n");
			}
			responseStreamReader.close();
			response = stringBuilder.toString();
			httpConn.disconnect();
		} else {
			LOG.info("Server returned non-OK status: " + status);
			throw new IOException("Server returned non-OK status: " + status);
		}
		return response;
	}

}
