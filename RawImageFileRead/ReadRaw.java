package RawImageFileRead;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

class ReadRaw {
    public static void main(String[] args) {
        try {
            FileInputStream fis = new FileInputStream(
                    new File("C:\\Code\\COMP4ST-ImageProcessing\\RawImageFileRead\\random.raw"));
            byte planes = (byte) fis.read();
            short height = (short) ((fis.read() << 8) + fis.read());
            short width = (short) ((fis.read() << 8) + fis.read());
            System.out.println(planes + " x " + height + " x " + width);

            short[][][] image = new short[planes][height][width];
            double[] mean = new double[] { 0, 0, 0 };
            double[] standardDeviation = new double[] { 0, 0, 0 };

            for (short i = 0; i < height; i++) {
                for (short j = 0; j < width; j++) {
                    for (short q = 0; q < planes; q++) {
                        image[q][i][j] = (short) fis.read();
                        mean[q] += image[q][i][j];
                    }
                }
            }

            for (int i = 0; i < planes; i++) {
                mean[i] /= (height * width);
            }

            for (short i = 0; i < height; i++) {
                for (short j = 0; j < width; j++) {
                    for (short q = 0; q < planes; q++) {
                        image[q][i][j] -= mean[q];
                        image[q][i][j] *= image[q][i][j];
                        standardDeviation[q] += image[q][i][j];
                    }
                }
            }

            for (int i = 0; i < planes; i++) {
                standardDeviation[i] /= (height * width);
                standardDeviation[i] = Math.sqrt(standardDeviation[i]);
            }

            DecimalFormat df = new DecimalFormat("#.#");

            System.out.println("====  RED   GREEN   BLUE");
            System.out.println("MEAN " + df.format(mean[0]) + "  " + df.format(mean[1]) + "   " + df.format(mean[2]));
            System.out.println("S.D. " + df.format(standardDeviation[0]) + "   " + df.format(standardDeviation[1]) + "    "
                    + df.format(standardDeviation[2]));

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}