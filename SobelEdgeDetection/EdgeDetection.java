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
            int[][][] magImg = new int[3][img[0].length][img[0][0].length];
            int[][][] orImg = new int[3][img[0].length][img[0][0].length];
            for (int i = 0; i < 3; i++) {
                horImg[i] = ImageEditor.convolve(img[i], horMask, false, false);
                verImg[i] = ImageEditor.convolve(img[i], verMask, false, false);
            }
            for (int c = 0; c < img.length; c++) {
                for (int i = 0; i < img[0].length; i++) {
                    for (int j = 0; j < img[0][0].length; j++) {
                        magImg[c][i][j] = (int) Math
                                .round(Math.sqrt(Math.pow(horImg[c][i][j], 2) + Math.pow(verImg[c][i][j], 2)));
                        orImg[c][i][j] = (int) Math.atan2(horImg[c][i][j], verImg[c][i][j]);
                        magImg[c][i][j] = magImg[c][i][j] > 255 ? 255 : (magImg[c][i][j] < 0 ? 0 : magImg[c][i][j]);
                        orImg[c][i][j] = orImg[c][i][j] > 255 ? 255 : (orImg[c][i][j] < 0 ? 0 : orImg[c][i][j]);
                    }
                }
            }
            magImg = ImageEditor.scale(magImg, 0, 255);
            orImg = ImageEditor.scale(orImg, 0, 255);
            BufferedImage outiH = ImageEditor.int2bi(ImageEditor.scale(horImg, 0, 255));
            BufferedImage outiV = ImageEditor.int2bi( ImageEditor.scale(verImg, 0, 255));
            BufferedImage outMag = ImageEditor.int2bi(magImg);
            BufferedImage outOr = ImageEditor.int2bi(orImg);
            ImageIO.write(outiH, "PNG", new File("C:\\Code\\CSC4ST-ImageProcessing\\SobelEdgeDetection\\outiH.png"));
            ImageIO.write(outiV, "PNG", new File("C:\\Code\\CSC4ST-ImageProcessing\\SobelEdgeDetection\\outiV.png"));
            ImageIO.write(outMag, "PNG", new File("C:\\Code\\CSC4ST-ImageProcessing\\SobelEdgeDetection\\outMag.png"));
            ImageIO.write(outOr, "PNG", new File("C:\\Code\\CSC4ST-ImageProcessing\\SobelEdgeDetection\\outOr.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}