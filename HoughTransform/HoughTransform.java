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

        int center  = (rows + cols) / 2;

        // For each color
        for (int c = 0; c < img.length; c++) {
            // For each row
            for (int i = 0; i < rows; i++) {
                // For each column
                for (int j = 0; j < cols; j++) {
                    // If pixel is an edge
                    if(img[c][i][j] > 0){
                        for (int theta = 0, counter = 0; theta < 180; theta += THETASTEP, counter++) {
                            double radians = Math.toRadians(theta);
                            double rho = j * Math.cos(radians) + i * Math.sin(radians);
                            ++accumulator[c][(int)(rho + center)][counter];
                        }
                    }
                }
            }
        }
        return accumulator;
    }


    public static void main(String[] args) {
        String filename = "C:\\Code\\CSC4ST-ImageProcessing\\HoughTransform\\IN.png";
        BufferedImage bi;
        try {
            bi = ImageIO.read(new File(filename));
            int[][][] RGB = ImageEditor.bi2int(bi);
            int[][][] houghTransform = houghTransform(RGB);
            BufferedImage out = ImageEditor.int2bi(houghTransform);
            ImageIO.write(out, "PNG", new File("C:\\Code\\CSC4ST-ImageProcessing\\HoughTransform\\OUT.png"));
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}