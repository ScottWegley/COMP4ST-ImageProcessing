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

            short[][][] image = new short[planes][height][width];
            for(short i = 0; i < height; i++){
                for(short j = 0; j < width; j++){
                    image[0][i][j] = (short) fis.read();
                    image[1][i][j] = (short) fis.read();
                    image[2][i][j] = (short) fis.read();
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}