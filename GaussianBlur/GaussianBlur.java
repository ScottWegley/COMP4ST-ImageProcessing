package GaussianBlur;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Library.ImageEditor;

public class GaussianBlur {

    public static double[][] generateGaussian(double mux, double muy, double sigmax, double sigmay, double A,
            int sizex, int sizey) {
        if (sizex % 2 == 0 || sizey % 2 == 0) {
            throw new IllegalArgumentException("Filter sizes must be odd");
        }

        double gaussian[][] = new double[sizey][sizex];
        int midx = sizex / 2;
        int midy = sizey / 2;
        for (int y = -midy; y <= midy; y++) {
            for (int x = -midx; x <= midx; x++) {
                gaussian[y + midy][x + midx] = A * Math.exp(-((Math.pow(x - mux, 2) / (2 * sigmax * sigmax))
                        + (Math.pow(y - muy, 2) / (2 * sigmay * sigmay))));
            }
        }
        return gaussian;
    }

    public static int[][] convolve(int in[][], double[][] mask, boolean normalize) throws IllegalArgumentException {
        int mheight = mask.length;
        int mwidth = mask[0].length;
        if (mheight % 2 == 0 || mwidth % 2 == 0) {
            throw new IllegalArgumentException("Mask size must be odd");
        }

        if (normalize) {
            double sum = 0.0;
            for (int i = 0; i < mask.length; i++) {
                for (int j = 0; j < mask[0].length; j++) {
                    sum += mask[i][j];
                }
            }
            for (int i = 0; i < mask.length; i++) {
                for (int j = 0; j < mask[0].length; j++) {
                    mask[i][j] /= sum;
                }
            }
        }

        int height = in.length;
        int width = in[0].length;

        int out[][] = new int[height][width];

        // Two loops for getting every pixel;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double sum = 0;

                for (int k = -mheight / 2; k <= mheight / 2; k++) {
                    for (int l = -mwidth / 2; l <= mwidth / 2; l++) {
                        try {
                            sum += in[i + k][j + l] * mask[k + mheight / 2][l + mwidth / 2];
                        } catch (ArrayIndexOutOfBoundsException e) {
                        }
                    }
                }
                out[i][j] = (int) (sum < 0 ? 0 : (sum > 255 ? 255 : sum));
            }
        }

        return out;
    }

    public static void main(String[] args) {
        String filename = "C:\\Code\\CSC4ST-ImageProcessing\\GaussianBlur\\Space.png";
        BufferedImage bi;
        try {
            bi = ImageIO.read(new File(filename));
            int[][][] img = ImageEditor.bi2int(bi);
            int[][][] convimg = new int[3][0][0];
            int[][][] convimg2 = new int[3][0][0];
            double gaussian[][] = generateGaussian(0, 0, 1, 1, 1, 11, 11);
            double gaussian2[][] = generateGaussian(0, 0, 2, 2, 1, 11, 11) ;

            for (int c = 0; c < img.length; c++) {
                convimg[c] = convolve(img[c], gaussian, true);
                convimg2[c] = convolve(img[c], gaussian2, true);
            }
            bi = ImageEditor.int2bi(convimg);
            ImageIO.write(bi, "PNG", new File("C:\\Code\\CSC4ST-ImageProcessing\\GaussianBlur\\blur1.png"));
            bi = ImageEditor.int2bi(convimg2);
            ImageIO.write(bi, "PNG", new File("C:\\Code\\CSC4ST-ImageProcessing\\GaussianBlur\\blur2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
