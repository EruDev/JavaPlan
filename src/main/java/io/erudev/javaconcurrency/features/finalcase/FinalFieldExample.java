package io.erudev.javaconcurrency.features.finalcase;

/**
 * @author pengfei.zhao
 * @date 2023/11/4 13:43
 */
public class FinalFieldExample {
    static class Global{
        private static FinalFieldExample obj;

    }

    final int x;
    int y;
    static FinalFieldExample f;
    public FinalFieldExample() {
        x = 3;
        y = 4;
        // bad construction - allowing this to escape
        Global.obj = this;
    }

    public static void main(String[] args) {
        reader();
        System.out.println(Global.obj.x);
        writer();
    }

    static void writer() {
        f = new FinalFieldExample();
    }

    static void reader() {
        if (f != null) {
            int i = f.x;
            int j = f.y;
        }
    }
}
