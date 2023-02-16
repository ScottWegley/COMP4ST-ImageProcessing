import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

class Otsu {
    // -- outline for computing the threshold of a single image plane
    public static int otsu(int[][] img) {
        int height = img.length;
        int width = img[0].length;

        // -- histogram
        int[] histogram = new int[256];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                histogram[img[i][j]]++;
            }
        }

        double pixeltotal = 0;
        // -- the total sum of all image pixel values
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixeltotal += img[i][j];
            }
        }

        double sumB = 0;
        int wB = 0;
        int wF = 0;

        // -- the resultant (selected) threshold
        int threshold = 0;

        double varMax = 0;

        // -- loop through all possible thresholds
        for (int t = 0; t < histogram.length; t++) {

            // -- set the background weight to number of pixels
            // in the histogram below the current threshold
            // Note: this sum accumulates over loop indices
            wB += histogram[t];

            // -- if wB == 0 then there are no pixels in the background class, try next
            // threshold
            if (wB == 0)
                continue;

            // -- set the foreground weight to the remaining
            // number of pixels (
            wF = (height * width) - wB;

            // -- if wF == 0 all pixels are in the foreground class, done
            if (wF == 0)
                break;

            // -- sum of pixel values at this threshold
            sumB += (double) (t * histogram[t]);

            // -- mean of the background for this threshold
            double mB = sumB / wB;

            // -- mean of the foreground for this threshold
            double mF = (pixeltotal - sumB) / wF;

            // -- Variance is a measure of dispersion, how far a set of numbers
            // is spread out from their average value.
            // Between class variance is a measure of how far one set mean
            // is from the other
            // p(0) * p(1) * (mean(0) - mean(1))^2
            double varBetween = (double) wB * (double) wF * (mB - mF) * (mB - mF);
            // -- look to maximize the variance between the two classes which
            // will give us the maximal distance between the means of the two classes
            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = t;
            }
        }

        return threshold;
    }

    public static void main(String[] args) {
        String filename = "C:\\Code\\CSC4ST-ImageProcessing\\OtsuAutoThreshold\\Space.png";
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(new File(filename));
            int img[][][] = ImageEditor.bi2int(bi);
            int[] thresholds = new int[3];
            for (int i = 0; i < 3; i++) {
                thresholds[i] = otsu(img[i]);
            }
            int output[][][] = ImageEditor.binarize(img, thresholds);
            int redOut[][][] = new int[3][bi.getHeight()][bi.getWidth()];
            int grnOut[][][] = new int[3][bi.getHeight()][bi.getWidth()];
            int bluOut[][][] = new int[3][bi.getHeight()][bi.getWidth()];
            for (int i = 0; i < bi.getHeight(); i++) {
                for (int j = 0; j < bi.getWidth(); j++) {
                    for (int c = 0; c < 3; c++) {
                        redOut[c][i][j] = output[0][i][j];
                        grnOut[c][i][j] = output[1][i][j];
                        bluOut[c][i][j] = output[2][i][j];
                    }
                }
            }
            BufferedImage rImg = ImageEditor.int2bi(redOut);
            BufferedImage gImg = ImageEditor.int2bi(grnOut);
            BufferedImage bImg = ImageEditor.int2bi(bluOut);
            ImageIO.write(rImg, "PNG", new File("C:\\Code\\CSC4ST-ImageProcessing\\OtsuAutoThreshold\\rOutput.png"));
            ImageIO.write(gImg, "PNG", new File("C:\\Code\\CSC4ST-ImageProcessing\\OtsuAutoThreshold\\gOutput.png"));
            ImageIO.write(bImg, "PNG", new File("C:\\Code\\CSC4ST-ImageProcessing\\OtsuAutoThreshold\\bOutput.png"));
            int[][] bwCount = new int[3][2];
            for (int c = 0; c < output.length; c++) {
                for (int i = 0; i < output[0].length; i++) {
                    for (int j = 0; j < output[0][0].length; j++) {
                        bwCount[c][(output[c][i][j] == 255 ? 0 : 1)]++;
                    }
                }
            }
            for (int i = 0; i < bwCount.length; i++) {
                System.out.println((i == 0 ? "Red " : (i == 1 ? "Green " : "Blue ")) + "Stats");
                System.out.println("Black: " + bwCount[i][0]);
                System.out.println("White: " + bwCount[i][1]);
                System.out.println("Sum: " + (bwCount[i][0] + bwCount[i][1]));
                System.out.println();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}