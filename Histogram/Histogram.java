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

            int[][] histograms = new int[3][256];
            for (int i = 0; i < bi.getHeight(); i++) {
                for (int j = 0; j < bi.getWidth(); j++) {
                    int argb = bi.getRGB(j, i);
                    int red = (argb >> 16) & 0xFF; // -- RED
                    int green = (argb >> 8) & 0xFF; // -- GREEN
                    int blue = (argb >> 0) & 0xFF; // -- BLUE
                    histograms[0][red]++;
                    histograms[1][green]++;
                    histograms[2][blue]++;
                }
            }

            float[] redStat = new float[]{255,0,0,0,0,0};
            float[] grnStat = new float[]{255,0,0,0,0,0};
            float[] bluStat = new float[]{255,0,0,0,0,0};
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
