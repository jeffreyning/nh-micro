package com.project.util;



import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringBufferInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.pdf.BaseFont;

public class Html2Pdf {  

	public static String fontsRootPath=new File(Html2Pdf.class.getResource("/").getFile()).getAbsolutePath()+File.separator+"font";
	
	
    public static String getFontsRootPath() {
		return fontsRootPath;
	}


	public void setFontsRootPath(String fontsRootPath) {
		Html2Pdf.fontsRootPath = fontsRootPath;
	}


	public static boolean convertHtmlToPdf(String htmlText, OutputStream os)  
            throws Exception {  

        ITextRenderer renderer = new ITextRenderer();  
        renderer.setDocumentFromString(htmlText);
       // BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        ITextFontResolver fontResolver = renderer.getFontResolver();  
        System.out.println(fontsRootPath);
        fontResolver.addFontDirectory(fontsRootPath, BaseFont.NOT_EMBEDDED);
        //fontResolver.addFont("C:/Windows/Fonts/simsunb.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);  
        //fontResolver.addFont("C:/Windows/Fonts/arialuni.ttf", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);  
        //fontResolver.addFont(fontsRootPath+"/simsun.ttc",BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);// 宋体字        
        //fontResolver.addFont("C:/Windows/Fonts/BELLB.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        //fontResolver.addFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED); 
       // renderer.getSharedContext().setBaseURL("file:/D:/test");  
        renderer.layout();  
        renderer.createPDF(os);  
        return true;  
    }  
 	
	
    public static boolean convertHtmlToPdf(String inputFile, String outputFile)  
            throws Exception {  
  
        OutputStream os = new FileOutputStream(outputFile);  
        ITextRenderer renderer = new ITextRenderer();  
        String url = new File(inputFile).toURI().toURL().toString();  
        renderer.setDocument(url);  

        ITextFontResolver fontResolver = renderer.getFontResolver();  
        //fontResolver.addFont("C:/Windows/Fonts/simsunb.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);  
        
        //fontResolver.addFont("C:/Windows/Fonts/arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);  
        
        fontResolver.addFont("C:/WINDOWS/Fonts/simsun.ttc",BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);// 宋体字        
        //fontResolver.addFont("C:/Windows/Fonts/BELLB.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        //���ͼƬ�����·������  
       // renderer.getSharedContext().setBaseURL("file:/D:/test");  
        renderer.layout();  
        renderer.createPDF(os);  
        os.flush();  
        os.close();  
        return true;  
    }  
  
  
     public   static  void  main(String [] args){  
         Html2Pdf html2Pdf =new Html2Pdf();  
         try {  
        	 //String sourceFile=args[0];
        	 //String targetFile=args[1];
             //html2Pdf.convertHtmlToPdf(sourceFile,targetFile); 
             html2Pdf.convertHtmlToPdf("d://2.html","d://index2.pdf");
         } catch (Exception e) {  
             e.printStackTrace();  
         }  
     }  
}  
