package com.sic.bb.hudson.plugins.xcodeplugin.io;

import hudson.console.LineTransformationOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class XcodeFilteredOutputStream extends LineTransformationOutputStream {
	private static final String MASK = "******";
	private static final String[] REGEXSPECIALCHARS = new String[] {"(",")","{","}","[","]","^","$","*",".","?","|"};
	
	private final OutputStream logger;
	private Pattern toSuppressPattern;
	
	public XcodeFilteredOutputStream(OutputStream logger, String toSuppress) {
		this.logger = logger;
		
		List<String> toSuppressList = new ArrayList<String>();
		toSuppressList.add(toSuppress);
		createSuppressPattern(toSuppressList);
	}
	
	public XcodeFilteredOutputStream(OutputStream logger, List<String> toSuppressList) {
		this.logger = logger;
		
		createSuppressPattern(toSuppressList);
	}
	
	private void createSuppressPattern(List<String> toSuppressList) {
	    StringBuilder regex = new StringBuilder();
	    
	    if(toSuppressList != null) {
	      regex.append('(');
	      
	      for(String item: toSuppressList) {
	        if(StringUtils.isBlank(item))
	          continue;

	        for(String regexChar: REGEXSPECIALCHARS)
	        	item = item.replace(regexChar, "\\" + regexChar);
	        
	        regex.append(item);
	        regex.append('|');
	      }
	      
	      regex.deleteCharAt(regex.length() - 1);
	      regex.append(')');
	    }
	    
	    this.toSuppressPattern = Pattern.compile(regex.toString());
	}

	@Override
	protected void eol(byte[] bytes, int len) throws IOException {
	    String line = new String(bytes, 0, len);
	    line = this.toSuppressPattern.matcher(line).replaceAll(MASK);
	    this.logger.write(line.getBytes());
	}
}