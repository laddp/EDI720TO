package com.bottinifuel.edi720to;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.pb.x12.Context;
import org.pb.x12.Parser;
import org.pb.x12.Segment;
import org.pb.x12.X12Simple;
import org.pb.x12.X12SimpleParser;

import com.bottinifuel.edi720to.util.ContextType;

public class Parse997Ack {
	
	Context context = ContextType.IRSCONTENT;
	File file;
	
	public Parse997Ack(File file) {
		this.file = file;
	
		X12Simple x12; // = new X12Simple(context);
	    Parser parser = new X12SimpleParser();
	    
	    String x12Data = "";
	    try {
	        BufferedReader in = new BufferedReader(new FileReader(file));
	        System.out.println(x12Data);
	        in.close();
	    }  catch (IOException e) { }
		
		// In order to get the parser to work, the element separator *
	    // and the Segment separator must be ~
	    // So replace these chars
	    x12Data = x12Data.replace(context.getElementSeparator(), '*');
	    x12Data = x12Data.replace(context.getSegmentSeparator(), '~');
	    System.out.println("x12Data="+x12Data);
	      
	    try {
	            x12 = (X12Simple) parser.parse(x12Data);
	            for (Segment s : x12) {
	            	    System.out.println("seg="+s);
	            }
	    } catch (Exception e1) {
	            e1.printStackTrace();
	    }
	}
}
