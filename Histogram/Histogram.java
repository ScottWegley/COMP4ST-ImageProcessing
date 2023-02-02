package Histogram;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Histogram {

    public static int[][][] bi2int(BufferedImage bi) {
        int intimg[][][] = new int[3][bi.getHeight()][bi.getWidth()];
        int h = bi.getHeight();
        int w = bi.getWidth();
        for (int y = 0; y < bi.getHeight(); ++y) {
            for (int x = 0; x < bi.getWidth(); ++x) {
                int argb = bi.getRGB(x, y);
                intimg[0][y][x] = (argb >> 16) & 0xFF; // -- RED
                intimg[1][y][x] = (argb >> 8) & 0xFF; // -- GREEN
                intimg[2][y][x] = (argb >> 0) & 0xFF; // -- BLUE
            }
        }
        return intimg;
    }

    public static void main(String[] args) {
        String filename = "C:\\Code\\CSC4ST-ImageProcessing\\Histogram\\spheres.png";
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(new File(filename));
            System.out.println(bi.getHeight() + "x" + bi.getWidth() + " : " + bi.getType());
            int img[][][] = bi2int(bi);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
