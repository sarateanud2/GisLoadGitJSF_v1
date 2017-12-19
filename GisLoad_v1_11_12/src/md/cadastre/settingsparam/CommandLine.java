package md.cadastre.settingsparam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandLine {
	static final Logger logger = LoggerFactory.getLogger(CommandLine.class);

    public static int executeCommand(String command) {
        int result = -1;
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor(60, TimeUnit.SECONDS); 
            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line = "";
            bre.close();
            while ((line = reader.readLine()) != null) {
                result = p.exitValue();
            }
            System.out.println(result);
        } catch (IOException ex) {
            System.out.println(ex.getCause());
            logger.error(ex.getMessage() + ";" + Arrays.toString(ex.getStackTrace()));
        } catch (InterruptedException ex) {
            logger.error(ex.getMessage() + ";" + Arrays.toString(ex.getStackTrace()));
        }
        return result;
    }

    public static int executeCommandExec(String command) {
        int result = -1;
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            
            final BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            final BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        while (input.readLine() != null) {
                            System.out.println(input.readLine() + " Error");
                        }
                    } catch (IOException ex) {
                        logger.error(ex.getMessage() + ";" + Arrays.toString(ex.getStackTrace()));
                    }
                }
            }).start();
            
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        while (error.readLine() != null) {
                            System.out.println(error.readLine());
                        }
                    } catch (IOException ex) {
                        logger.error(ex.getMessage() + ";" + Arrays.toString(ex.getStackTrace()));
                    }
                }
            }).start();
            int i = 0;
            boolean finished = false;
            while (!finished) {
                try {
                    i = p.exitValue();
                    finished = true;
                } catch (IllegalThreadStateException e) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        logger.error(ex.getMessage() + ";" + Arrays.toString(ex.getStackTrace()));
                    }
                }
            }
            return i;

        } catch (Exception ex) {
            logger.error(ex.getMessage() + ";" + Arrays.toString(ex.getStackTrace()));
        }
        return result;
    }
}
