public class TestForContent {
    private static final int LINE=1024;
    private  static final int COL=1024;

    public long L(){
        int[][] arrays = new int[LINE][COL];
        long timeMillis = System.currentTimeMillis();
        for (int i=0;i<LINE;i++){
            for (int j=0;j<COL;j++){
                arrays[i][j]=i*j>>2;
            }
        }
        long timeend = System.currentTimeMillis();
        long t = timeend-timeMillis;
        return t*100;
    }

    public long C(){
        int[][] arrays = new int[LINE][COL];
        long timeMillis = System.currentTimeMillis();
        for (int i=0;i<COL;i++){
            for (int j=0;j<LINE;j++){
                arrays[i][j]=i*j>>2;
            }
        }
        long timeend = System.currentTimeMillis();
        long t = timeend-timeMillis;
        return t*100;
    }

    public static void main(String[] args) {
        TestForContent test = new TestForContent();
        long c = test.C();
        System.out.println("c:"+c);
        System.out.println("L:"+test.L());
    }
}
