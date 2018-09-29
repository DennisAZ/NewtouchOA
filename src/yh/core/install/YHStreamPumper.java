package yh.core.install;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import yh.core.global.YHConst;

public class YHStreamPumper extends Thread {
  private BufferedReader din;

  private int messageLevel;

  private boolean endOfStream = false;

  private int SLEEP_TIME = 5;

  private List msrgList = null;

  public YHStreamPumper(InputStream is, List msrgList) {
    this.din = new BufferedReader(new InputStreamReader(is));
    this.msrgList = msrgList;
  }

  public void pumpStream() throws IOException {
    byte[] buf = new byte[YHConst.DEFAULT_IO_BUFF_SIZE];
    if (!endOfStream) {
      String line = din.readLine();

      if (line != null) {
        msrgList.add(line);
      } else {
        endOfStream = true;
      }
    }
  }

  public void run() {
    try {
      try {
        while (!endOfStream) {
          pumpStream();
          sleep(SLEEP_TIME);
        }
      } catch (InterruptedException ie) {
      }
      din.close();
    } catch (IOException ioe) {
    }
  }
}
