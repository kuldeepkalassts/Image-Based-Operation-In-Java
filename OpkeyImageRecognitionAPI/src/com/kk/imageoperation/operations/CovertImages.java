package com.kk.imageoperation.operations;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.kk.imageoperation.logs.Log;

public class CovertImages {
	public static void convertToPNG(File input) throws IOException {
		Log.debug("Correcting image");
		BufferedImage bufferedImage = ImageIO.read(input);
		ImageIO.write(bufferedImage, "png", input);
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "png", byteArrayOut);
		Log.debug("Correcting image done");
	}
}
