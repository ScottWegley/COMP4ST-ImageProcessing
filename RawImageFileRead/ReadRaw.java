package RawImageFileRead;

import java.io.File;
import java.io.FileInputStream;

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
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}