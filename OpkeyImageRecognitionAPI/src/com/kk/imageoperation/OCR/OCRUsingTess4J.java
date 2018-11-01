package com.kk.imageoperation.OCR;

import java.io.File;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class OCRUsingTess4J {

	private static ITesseract tess;

	public OCRUsingTess4J(String Tess4jPath) {
		tess = new Tesseract();
		tess.setLanguage("eng");
		tess.setDatapath(System.getProperty("user.dir") + "\\lib\\Tess4j");
	}

	public void getTextFromImages(String imageFile) throws InterruptedException {
		File img = new File(imageFile);
		tess.setTessVariable("tessedit_char_whitelist", "acekopxyABCEHKMOPTXY0123456789");
		try {
			String result = tess.doOCR(img);
			System.out.println(result);
		} catch (TesseractException e) {
			System.err.println(e.getMessage());
		}
	}
}
