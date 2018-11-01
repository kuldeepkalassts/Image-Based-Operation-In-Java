package com.kk.ImageIO.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kk.imageoperation.OCR.OCRUsingTess4J;

class OCRTestUsingTess4j {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void test() throws InterruptedException {
		OCRUsingTess4J ocrtest = new OCRUsingTess4J(System.getProperty("user.dir") + "\\lib");
		ocrtest.getTextFromImages("D:\\test\\destination.png");
	}

}
