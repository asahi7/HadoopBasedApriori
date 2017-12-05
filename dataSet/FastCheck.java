import java.io.*;
import java.util.*;

class FastCheck {

    public static void main(String[] args) throws Exception {
       Scanner sc = new Scanner(new File("connect.dat"));
       int cnt = 0;
       while(sc.hasNextLine()) {
            String line = sc.nextLine();
            line = line.trim();
            String[] strVals = line.split(" ");
            int[] vals = new int[strVals.length];
            for(int i = 0; i < strVals.length; i++) {
               vals[i] = Integer.valueOf(strVals[i]);
            }
            int num = 0;
            for(int i = 0; i < vals.length; i++) {
                if(vals[i] == 34 || vals[i] == 53 || vals[i] == 86) num++;
                
            }
            if(num == 3) cnt++;
       }
       System.out.println(cnt);
    }

}
