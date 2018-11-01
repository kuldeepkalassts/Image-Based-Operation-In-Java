package com.kk.ImageIO.test;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kk.imageoperation.Result.ImageOutput;
import com.kk.imageoperation.TemplateMatching.ObjectSearchUsingOpenCV;

class ImageSearchUsingOpenCVTest {

	@BeforeEach
	void setUp() throws Exception {

	}

	@Test
	void test() throws Exception {

		try {
			ObjectSearchUsingOpenCV oc = new ObjectSearchUsingOpenCV(System.getProperty("user.dir") + "\\lib");
			oc.setCustomCutoff(999, 410);
			ImageOutput iso = oc.getObjectImage("D:\\test\\destination.png", "D:\\test\\kk.png");
			System.out.println(iso.getResultImage());
			System.out.println(iso.getResultMessage());
			System.out.println(iso.getY());
			System.out.println(iso.getX());
			System.out.println(iso.getImageMatchingScore());
			System.out.println("done");

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

}
