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

            String[] colors = new String[]{"red","green","blu"};

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

            for (int i = 0; i < histograms[0].length; i++) {
                //Checking for new maximum
                if(histograms[0][i] != 0){
                    redStat[1] = i;
                }
                if(histograms[0][255-i] != 0){
                    redStat[0] = 255-i;
                }
                if(histograms[1][i] != 0){
                    grnStat[1] = i;
                }
                //Checking for new minimum
                if(histograms[1][255-i] != 0){
                    grnStat[0] = 255-i;
                }
                if(histograms[2][i] != 0){
                    bluStat[1] = i;
                }
                if(histograms[2][255-i] != 0){
                    bluStat[0] = 255-i;
                }
                //Calculating the mean.
                redStat[2] += i * histograms[0][i];
                grnStat[2] += i * histograms[1][i];
                bluStat[2] += i * histograms[2][i];
                //Standard Deviation
                redStat[3] += i*i * histograms[0][i];
                grnStat[3] += i*i * histograms[1][i];
                bluStat[3] += i*i * histograms[2][i];
            }

            redStat[3] = (float) Math.sqrt((redStat[3] - ((redStat[2] * redStat[2]) / (bi.getHeight() * bi.getWidth())))
                    / (bi.getHeight() * bi.getWidth()));
            grnStat[3] = (float) Math.sqrt((grnStat[3] - ((grnStat[2] * grnStat[2]) / (bi.getHeight() * bi.getWidth())))
                    / (bi.getHeight() * bi.getWidth()));
            bluStat[3] = (float) Math.sqrt((bluStat[3] - ((bluStat[2] * bluStat[2]) / (bi.getHeight() * bi.getWidth())))
                    / (bi.getHeight() * bi.getWidth()));

            redStat[2] /= bi.getHeight() * bi.getWidth();
            grnStat[2] /= bi.getHeight() * bi.getWidth();
            bluStat[2] /= bi.getHeight() * bi.getWidth();


            System.out.println("       min:   max:    mean:   dev:   median:   mode:   ");
            System.out.print("red:   ");
            for (int i = 0; i < redStat.length; i++) {
                System.out.print(String.format("%.2f",redStat[i]) + "  ");
            }
            System.out.print("\ngrn:   ");
            for (int i = 0; i < grnStat.length; i++) {
                System.out.print(String.format("%.2f",grnStat[i]) + "  ");
            }
            System.out.print("\nblu:   ");
            for (int i = 0; i < bluStat.length; i++) {
                System.out.print(String.format("%.2f",bluStat[i]) + "  ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
