package com.kk.imageoperation.operations;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageScaling {
	private double			WidthFactor;
	private double			HeightFactor;
	private int				scaledImageWidth;
	private int				scaledImageHeight;
	private BufferedImage	originalImage;

	public ImageScaling(String objectImagePath, String sourceImagePath, String targetImagePath) throws IOException {
		originalImage = ImageIO.read(new File(objectImagePath));
		BufferedImage sourceImage = ImageIO.read(new File(sourceImagePath));
		BufferedImage targetImage = ImageIO.read(new File(targetImagePath));
		WidthFactor = sourceImage.getWidth() / originalImage.getWidth();
		HeightFactor = sourceImage.getHeight() / originalImage.getHeight();
		scaledImageWidth = (int) Math.round((targetImage.getWidth() / WidthFactor));
		scaledImageHeight = (int) Math.round((targetImage.getHeight() / HeightFactor));
	}

	public void scaleObjectImageForDiffrentResoultionImage(String newScaledImage) throws IOException {
		int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
		BufferedImage resizeImagePng = scaleImageWithHint(originalImage, type);
		ImageIO.write(resizeImagePng, "png", new File(newScaledImage));
	}

	public BufferedImage resizeImage(BufferedImage originalImage, int type) {
		BufferedImage resizedImage = new BufferedImage(scaledImageWidth, scaledImageHeight, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, scaledImageWidth, scaledImageHeight, null);
		g.dispose();
		return resizedImage;
	}

	public BufferedImage scaleImageWithHint(BufferedImage originalImage, int type) {
		BufferedImage resizedImage = new BufferedImage(scaledImageWidth, scaledImageHeight, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, scaledImageWidth, scaledImageHeight, null);
		g.dispose();
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		return resizedImage;
	}

}