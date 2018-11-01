package com.kk.ImageIO.test;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kk.imageoperation.operations.ImageScaling;

class ImageScallingTest {

	@BeforeEach
	void setUp() throws Exception {

	}

	@Test
	void test() {

		try {
			ImageScaling is = new ImageScaling("D:\\test\\object.png","D:\\test\\source.png", "D:\\test\\destination.png");
			is.scaleObjectImageForDiffrentResoultionImage("D:\\test\\kk.png");
			System.out.println("done");

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

}
