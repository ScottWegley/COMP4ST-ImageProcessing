package ImageFileFormats;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageIOFormats {

	public static BufferedImage int2bi(int[][][] intimg) {
		BufferedImage bi = new BufferedImage(intimg[0][0].length, intimg[0].length, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < bi.getHeight(); ++y) {
			for (int x = 0; x < bi.getWidth(); ++x) {
				int rgb = (intimg[0][y][x] << 16) |
						  (intimg[1][y][x] <<  8) |
						  (intimg[2][y][x] <<  0);
				bi.setRGB(x,  y,  rgb);
			}
		}
		return bi;
	}
	
	
	public static int[][][] bi2int(BufferedImage bi) {
		int intimg[][][] = new int[3][bi.getHeight()][bi.getWidth()];
		int h = bi.getHeight();
		int w = bi.getWidth();
		for (int y = 0; y < bi.getHeight(); ++y) {
			for (int x = 0; x < bi.getWidth(); ++x) {
				int argb = bi.getRGB(x, y);
				intimg[0][y][x]  = (argb >> 16) & 0xFF; // -- RED
				intimg[1][y][x]  = (argb >>  8) & 0xFF; // -- GREEN
				intimg[2][y][x]  = (argb >>  0) & 0xFF; // -- BLUE
			}
		}	
		return intimg;
	}
	
	
	public static void main (String[] args) 
	{
		String formats[] = ImageIO.getReaderFormatNames();
		for (int i = 0; i < formats.length; ++i) {
			System.out.println(formats[i]);
		}

		String filename = "C:\\Code\\COMP4ST-ImageProcessing\\ImageFileFormats\\Crystal_Project_computer.png";
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(new File(filename));
			System.out.println(bi.getHeight() + "x" + bi.getWidth() + " : " + bi.getType());
		
			// -- convert BufferedImage -> int[][]
			int img[][][] = bi2int(bi);
			
			// -- convert int[][] -> BufferedImage
			bi = int2bi(img);
			
			// -- draw a graphic overlay onto the image
			Graphics2D g2d = (Graphics2D)bi.getGraphics();
			g2d.setColor(Color.RED);
			g2d.drawLine(0,  0,  bi.getWidth(), bi.getHeight());
			g2d.drawLine(bi.getWidth(), 0, 0, bi.getHeight());
			
			// -- save as a PNG and TIF
			ImageIO.write(bi, "PNG", new File("selfie.png"));
			ImageIO.write(bi, "TIF", new File("selfie.tif"));
		} catch (IOException e) {
			System.out.println(e);
		}

	}
}