package com.twoswap.stupidsteg;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class StupidSteganography {
	public static final String IMG1 = "res/1.png";
	public static final String IMG2 = "res/2.png";
	public static final String IMG = "res/encoded.png";

	public static void main(String[] args) {
		System.out.println("Enter 1 to combine res/1.png and res/2.png into res/encoded.png, enter 2 to invert res/encoded.png");
		Scanner sc = new Scanner(System.in);
		int i = sc.nextInt();
		if(i==1)encode();
		if(i==2)reverse();
		sc.close();
	}
	
	public static void reverse() {
		BufferedImage img;

		try {
			img = ImageIO.read(new File(IMG));

			int w = img.getWidth();
			int h = img.getHeight();

			int[] aggregate = new int[h * w];

			for (int x = 0; x < w; x++)
				for (int y = 0; y < h; y++) {
					int argb = img.getRGB(x, y);
					int r1 = argb / 0x100000;
					int r2 = argb / 0x10000 % 0x10;
					int g1 = argb / 0x1000 % 0x10;
					int g2 = argb / 0x100 % 0x10;
					int b1 = argb / 0x10 % 0x10;
					int b2 = argb % 0x10;
					aggregate[x + y * w] += r2 * 0x100000;
					aggregate[x + y * w] += r1 * 0x10000;
					aggregate[x + y * w] += g2 * 0x1000;
					aggregate[x + y * w] += g1 * 0x100;
					aggregate[x + y * w] += b2 * 0x10;
					aggregate[x + y * w] += b1;
				}

			BufferedImage endImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			endImage.setRGB(0, 0, w, h, aggregate, 0, w);

			File file = new File("res/reversed.png");
			try {
				ImageIO.write(endImage, "png", file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void encode() {
		BufferedImage img1;
		BufferedImage img2;

		try {
			img1 = ImageIO.read(new File(IMG1));
			img2 = ImageIO.read(new File(IMG2));

			int w = img1.getWidth();
			int h = img1.getHeight();

			int[] aggregate = new int[h * w];

			for (int x = 0; x < w; x++)
				for (int y = 0; y < h; y++) {
					int argb = img1.getRGB(x, y);
					int r = argb / 0x10000;
					int g = argb / 0x100 % 0x100;
					int b = argb % 0x100;
					int nr = r / 16;
					int ng = g / 16;
					int nb = b / 16;
					aggregate[x + y * w] += nr * 0x100000;
					aggregate[x + y * w] += ng * 0x1000;
					aggregate[x + y * w] += nb * 0x10;
				}

			for (int x = 0; x < w; x++)
				for (int y = 0; y < h; y++) {
					int argb = img2.getRGB(x, y);
					int r = argb / 0x10000;
					int g = argb / 0x100 % 0x100;
					int b = argb % 0x100;
					int nr = r / 16;
					int ng = g / 16;
					int nb = b / 16;
					aggregate[x + y * w] += nr * 0x10000;
					aggregate[x + y * w] += ng * 0x100;
					aggregate[x + y * w] += nb;
				}

			BufferedImage endImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			endImage.setRGB(0, 0, w, h, aggregate, 0, w);

			File file = new File("res/encoded.png");
			try {
				ImageIO.write(endImage, "png", file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
