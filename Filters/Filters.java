package Filters;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import Library.ImageEditor;

public class Filters {

    public static int[][] convolve(int in[][], double[][] mask) throws IllegalArgumentException {
        int mheight = mask.length;
        int mwidth = mask[0].length;
        if (mheight % 2 == 0 || mwidth % 2 == 0) {
            throw new IllegalArgumentException("Mask size must be odd");
        }

        int height = in.length;
        int width = in[0].length;

        int out[][] = new int[height][width];

        // Two loops for getting every pixel;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double sum = 0;

                for (int k = 0; k < mheight; k++) {
                    for (int l = 0; l < mwidth; l++) {
                        try {
                            sum += in[i + k - 1][j + l - 1] * mask[k][l];
                        } catch (ArrayIndexOutOfBoundsException e) {
                        }
                    }
                }
                out[i][j] = (int) (sum < 0 ? 0 : sum > 255 ? 255 : sum);
            }
        }

        return out;
    }

   /*  public static int[][][] convolve(int in[][][], double[][] mask) {
        int out[][][] = new int[in.length][in[0].length][in[0][0].length];
        for (int i = 0; i < in.length; i++) {
            out[i] = convolve(in[i], mask);
        }
        return out;
    } */

    public static void main(String[] args) {
        String filename = "C:\\Code\\CSC4ST-ImageProcessing\\Filters\\Space.png";
        BufferedImage bi;
        try {
            bi = ImageIO.read(new File(filename));
            int[][][] img = ImageEditor.bi2int(bi);
            int[][][] convimg = new int[3][0][0];
            double[][] mask = {{1/9, 1/9,1/9},{1/9, 1/9,1/9},{1/9, 1/9,1/9}};
            for (int c = 0; c < img.length; c++) {
                convimg[c] = convolve(img[c], mask);
            }
            bi = ImageEditor.int2bi(convimg);
            ImageIO.write(bi, "PNG", new File("C:\\Code\\CSC4ST-ImageProcessing\\Filters\\blur.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
