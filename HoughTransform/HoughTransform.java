package HoughTransform;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import Library.ImageEditor;

public class HoughTransform {

    public static final int THETASTEP = 1;

    /**
     * Assume input image is a 3-color binary edge map.
     */
    public static int[][][] houghTransform(int[][][] img) {

        int rows = img[0].length;
        int cols = img[0][0].length;

        int[][][] accumulator = new int[img.length][2 * (rows + cols)][(int) (180. / THETASTEP) + 1];

        int center = (rows + cols) / 2;

        // For each color
        for (int c = 0; c < img.length; c++) {
            // For each row
            for (int i = 0; i < rows; i++) {
                // For each column
                for (int j = 0; j < cols; j++) {
                    // If pixel is an edge
                    if (img[c][i][j] > 0) {
                        for (int theta = 0, counter = 0; theta < 180; theta += THETASTEP, counter++) {
                            double radians = Math.toRadians(theta);
                            double rho = j * Math.cos(radians) + i * Math.sin(radians);
                            ++accumulator[c][(int) (rho + center)][counter];
                        }
                    }
                }
            }
        }
        return accumulator;
    }

    public static void main(String[] args) {
        String filename = "C:\\Code\\CSC4ST-ImageProcessing\\HoughTransform\\KanizsaSquare.png";
        BufferedImage bi;
        try {
            bi = ImageIO.read(new File(filename));
            int[][][] RGB = ImageEditor.bi2int(bi);
            // Sobel Transform
            double[][] horMask = new double[][] { { -1, -2, -1 },
                    { 0, 0, 0 },
                    { 1, 2, 1 } };
            double[][] verMask = new double[][] { { -1, 0, 1 },
                    { -2, 0, 2 },
                    { -1, 0, 1 } };
            int[][][] horImg = new int[3][0][0];
            int[][][] verImg = new int[3][0][0];
            int[][][] magImg = new int[3][RGB[0].length][RGB[0][0].length];
            for (int i = 0; i < 3; i++) {
                horImg[i] = ImageEditor.convolve(RGB[i], horMask, false, false);
                verImg[i] = ImageEditor.convolve(RGB[i], verMask, false, false);
            }
            for (int c = 0; c < RGB.length; c++) {
                for (int i = 0; i < RGB[0].length; i++) {
                    for (int j = 0; j < RGB[0][0].length; j++) {
                        magImg[c][i][j] = (int) Math
                                .round(Math.sqrt(Math.pow(horImg[c][i][j], 2) + Math.pow(verImg[c][i][j], 2)));
                    }
                }
            }
            // Scaling
            magImg = ImageEditor.scale(magImg, 0, 255);
            ImageIO.write(ImageEditor.int2bi(magImg), "PNG", new File("C:\\Code\\CSC4ST-ImageProcessing\\HoughTransform\\scaled.png"));
            //Binarize
            int[] thresholds = new int[3];
            for (int i = 0; i < 3; i++) {
                thresholds[i] = ImageEditor.otsu(magImg[i]);
            }
            magImg = ImageEditor.binarize(magImg, thresholds);
            ImageIO.write(ImageEditor.int2bi(magImg), "PNG", new File("C:\\Code\\CSC4ST-ImageProcessing\\HoughTransform\\binarized.png"));
            int[][][] houghTransform = houghTransform(magImg);
            houghTransform = ImageEditor.scale(houghTransform, 0,255);
            BufferedImage out = ImageEditor.int2bi(houghTransform);
            ImageIO.write(out, "PNG", new File("C:\\Code\\CSC4ST-ImageProcessing\\HoughTransform\\OUT.png"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}