package com.project.util;



import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.List;

/**
 * Created .
 */
public class Word2Html {

	public static String tempPath = System.getProperty("java.io.tmpdir");

	public static String getTempPath() {
		return tempPath;
	}

	public void setTempPath(String tempPath) {
		Word2Html.tempPath = tempPath;
	}

	public static void main(String argv[]) {
		try {
			String sourceFile = argv[0];
			String targetFile = argv[1];
			convert2Html(sourceFile, targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeFile(String content, String path) throws Exception {
		FileOutputStream fos = null;
		BufferedWriter bw = null;

		content = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
				+ content;
		org.jsoup.nodes.Document doc = Jsoup.parse(content);
		doc.outputSettings().prettyPrint(true);
		content = doc.toString();

		try {
			File file = new File(path);
			fos = new FileOutputStream(file);
			bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
			bw.write(content);
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fos != null)
					fos.close();
			} catch (IOException ie) {
			}
		}
	}

	// word to html
	public static int convert2Html(String fileName, String outPutFile)
			throws Exception {
		HWPFDocument wordDocument = null;
		ByteArrayOutputStream out = null;
		try {
			wordDocument = new HWPFDocument(new FileInputStream(fileName));
			// WordToHtmlUtils.loadDoc(new FileInputStream(inputFile));
			// 2007
			// XSSFWorkbook xssfwork=new XSSFWorkbook(new
			// FileInputStream(fileName));
			WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
					DocumentBuilderFactory.newInstance().newDocumentBuilder()
							.newDocument());
			wordToHtmlConverter.setPicturesManager(new PicturesManager() {
				public String savePicture(byte[] content,
						PictureType pictureType, String suggestedName,
						float widthInches, float heightInches) {
					return tempPath + File.pathSeparator + suggestedName;
				}
			});
			wordToHtmlConverter.processDocument(wordDocument);
			// save pictures
			List pics = wordDocument.getPicturesTable().getAllPictures();
			if (pics != null) {
				for (int i = 0; i < pics.size(); i++) {
					Picture pic = (Picture) pics.get(i);
					try {
						pic.writeImageContent(new FileOutputStream(tempPath
								+ File.pathSeparator
								+ pic.suggestFullFileName()));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
			Document htmlDocument = wordToHtmlConverter.getDocument();

			out = new ByteArrayOutputStream();
			DOMSource domSource = new DOMSource(htmlDocument);
			StreamResult streamResult = new StreamResult(out);

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer serializer = tf.newTransformer();
			serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty(OutputKeys.METHOD, "HTML");
			serializer.transform(domSource, streamResult);

			writeFile(new String(out.toByteArray(),"UTF-8"), outPutFile);
		} finally {
			if (wordDocument != null) {
				wordDocument.close();
				wordDocument=null;
			}
			if (out != null) {
				out.close();
				out=null;
			}
		}
		return 0;
	}
}
