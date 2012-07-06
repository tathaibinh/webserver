package net.guzun.webserver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import net.guzun.webserver.http.HttpRequest;
import net.guzun.webserver.http.HttpResponse;
import net.guzun.webserver.processors.HeaderProcessor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;


public class HeaderProcessorTest {
	private static final String ROOT_PATH = "/";
	private static final String LINE_SEPARATOR = "\n";
	private static final String ATTRIBUTES_SEPARATOR = ": ";
	private static final String PROTOCOL = "HTTP/1.1";
	private static final String PATH = "/pathto.folder/new.html";
	private static final String METHOD_GET = "GET";
	private final static String HEADER_KEY1 = "Accept-Charset";
	private final static String HEADER_VALUE1 = "utf-8";
	private final static String HEADER_KEY2 = "Accept-Encoding";
	private final static String HEADER_VALUE2 = "gzip, deflate";
	private HeaderProcessor headerProcessor;
	
	@Mock private HttpResponse response;
	private InputStream simpleGetInputStream;
	@Mock private OutputStream outputStream;
	
    @Before
    public void setUp() throws UnsupportedEncodingException
    {
    	MockitoAnnotations.initMocks(this);
    	headerProcessor = new HeaderProcessor(null);
    	simpleGetInputStream = new ByteArrayInputStream(getSimpleRequestString().getBytes("UTF-8"));
    	
    }
    
    private String getSimpleRequestString() {
	    StringBuilder stringBuilder = new StringBuilder();
	    stringBuilder.append(METHOD_GET)
		    .append(" ")
		    .append(PATH)
		    .append(" ")
		    .append(PROTOCOL)
		    .append(LINE_SEPARATOR)
		    .append(HEADER_KEY1)
		    .append(ATTRIBUTES_SEPARATOR)
		    .append(HEADER_VALUE1)
		    .append(LINE_SEPARATOR)
		    .append(HEADER_KEY2)
		    .append(ATTRIBUTES_SEPARATOR)
		    .append(HEADER_VALUE2)
		    .append(LINE_SEPARATOR);
	    return stringBuilder.toString();
    }

	@Test
    public void ProcessHttpGetRequest() {
		HttpRequest request = new HttpRequest(ROOT_PATH, simpleGetInputStream);
    	when(response.getOutputStream()).thenReturn(outputStream);
    	headerProcessor.process(request, response);
    	assertEquals(request.getMethod(), METHOD_GET);
    	assertEquals(request.getPath(), PATH);
    	assertEquals(request.getProtocol(), PROTOCOL);
    	assertEquals(HEADER_VALUE1, request.getAttribute(HEADER_KEY1));
    	assertEquals(HEADER_VALUE2, request.getAttribute(HEADER_KEY2));
    }
}