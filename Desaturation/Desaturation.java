package Desaturation;

import java.awt.image.BufferedImage;
import java.io.IOException;

import Library.ImageEditor;

public class Desaturation {

    private static final double wR = 0.299;
    private static final double wB = 0.114;

    public static void main(String[] args) {
        try {
            BufferedImage bi = ImageEditor.readImg("C:\\Code\\CSC4ST-ImageProcessing\\Desaturation\\Space.png");
            int[][][] img = ImageEditor.bi2int(bi);
            double[] sVals = new double[] { 0.0, 0.25, 0.50, 0.75, 1.0 };
            int[][][][] desatImgs = new int[sVals.length][3][img[0].length][img[0][0].length];
            BufferedImage tOut;

            for (int i = 0; i < sVals.length; i++) {
                for (int plane = 0; plane < img.length; plane++) {
                    for (int w = 0; w < img[0].length; w++) {
                        for (int h = 0; h < img[0][0].length; h++) {
                            double Y = (wR * img[0][w][h]) + img[1][w][h] * (1 - wB - wR) + (wB * img[2][w][h]);
                            desatImgs[i][plane][w][h] = (int) (Y + (sVals[i] * (img[plane][w][h] - Y)));
                        }
                    }
                }
                tOut = ImageEditor.int2bi(desatImgs[i]);
                ImageEditor.writePNG(tOut, "C:\\Code\\CSC4ST-ImageProcessing\\Desaturation\\Space" + sVals[i] + ".png");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
