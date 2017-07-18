package com.project.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class Html2Pdf {

	public static void expPdf(String htmlText, OutputStream os)
			throws Exception {
		PdfWriter pdfWriter = null;
		InputStream in_withcode = new ByteArrayInputStream(
				htmlText.getBytes("UTF-8"));
		try {
			com.itextpdf.text.Document document = new com.itextpdf.text.Document();
			pdfWriter = PdfWriter.getInstance(document, os);
			document.open();
			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
			worker.parseXHtml(pdfWriter, document, in_withcode, null,
					Charset.forName("UTF-8"), new AsianFontProvider());

			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in_withcode != null) {
				try {
					in_withcode.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (pdfWriter != null) {
				pdfWriter.close();
				try {
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
