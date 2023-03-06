package SobelEdgeDetection;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Library.ImageEditor;

class EdgeDetection {

    public static void main(String[] args) {
        String filename = "C:\\Code\\CSC4ST-ImageProcessing\\SobelEdgeDetection\\Space.png";
        BufferedImage bi;
        try {
            bi = ImageIO.read(new File(filename));
            int[][][] img = ImageEditor.bi2int(bi);
            double[][] horMask = new double[][] { { -1, -2, -1 },
                    { 0, 0, 0 },
                    { 1, 2, 1 } };
            double[][] verMask = new double[][] { { -1, 0, 1 },
                    { -2, 0, 2 },
                    { -1, 0, 1 } };
            int[][][] horImg = new int[3][0][0];
            int[][][] verImg = new int[3][0][0];
            for (int i = 0; i < 3; i++) {
                horImg[i] = ImageEditor.convolve(img[i], horMask, false);
                verImg[i] = ImageEditor.convolve(img[i], verMask, false);
            }
            horImg = ImageEditor.scale(horImg, 0, 255);
            verImg = ImageEditor.scale(verImg, 0, 255);
            BufferedImage outiH = ImageEditor.int2bi(horImg);
            BufferedImage outiV = ImageEditor.int2bi(verImg);
            // BufferedImage outMag = ImageEditor.int2bi(output);
            // BufferedImage outOr = ImageEditor.int2bi(output);
            ImageIO.write(outiH, "PNG", new File("C:\\Code\\CSC4ST-ImageProcessing\\SobelEdgeDetection\\outiH.png"));
            ImageIO.write(outiV, "PNG", new File("C:\\Code\\CSC4ST-ImageProcessing\\SobelEdgeDetection\\outiV.png"));
            // ImageIO.write(outMag, "PNG", new File("C:\\Code\\CSC4ST-ImageProcessing\\SobelEdgeDetection\\outMag.png"));
            // ImageIO.write(outOr, "PNG", new File("C:\\Code\\CSC4ST-ImageProcessing\\SobelEdgeDetection\\outOr.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}