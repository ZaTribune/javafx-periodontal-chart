package shadow.dental_chart;

import java.io.IOException;

/**
 *
 * @author Muhammad
 */
public class MainJar {

    public static void main(String[] args) throws IOException {
        //just to fix the problem of the fat jar can't find/load main class
        App.main(args);

    }

}
