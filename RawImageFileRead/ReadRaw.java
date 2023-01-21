package RawImageFileRead;

import java.io.File;
import java.io.FileInputStream;

class ReadRaw {
    public static void main(String[] args) {
        try {
            FileInputStream fis = new FileInputStream(new File("C:\\Code\\COMP4ST-ImageProcessing\\RawImageFileRead\\random.raw"));
            byte planes = (byte)fis.read();
            short height = (short)((fis.read() << 8) + fis.read());
            short width = (short)((fis.read() << 8) + fis.read());
            System.out.println(planes + " x " + height + " x " + width);
            for (int i = 0; i < 11; i++) {
                System.out.println(fis.read());
            }

            /* int[][][] image = new int[planes][height][width];
            for(short i = 0; i < height; i++){
                for(short j = 0; j < width; j++){
                    image[0][i][j] = fis.read();
                    image[1][i][j] = fis.read();
                    image[2][i][j] = fis.read();
                }
            }

            for(short i = 0; i < 1; i++){
                for(short j = 0; j < 10; j++){
                    System.out.print(i + "x" + j + ": ");
                    System.out.print(image[0][i][j] + " ");
                    System.out.print(image[1][i][j] + " ");
                    System.out.print(image[2][i][j] + " \n");
                }
            } */

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}