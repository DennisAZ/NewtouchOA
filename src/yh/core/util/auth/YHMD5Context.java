package yh.core.util.auth;

public class YHMD5Context {
  int[] state = new int[4];        /* state (ABCD) */
  int[] count = new int[2];        /* number of bits, modulo 2^64 (lsb first) */
  byte[] buffer = new byte[64]; /* input buffer */
  public void set0() {
    for (int i = 0; i < 4; i++) {
      state[i] = 0;
    }
    for (int i = 0; i < 2; i++) {
      count[i] = 0;
    }
    for (int i = 0; i < 64; i++) {
      buffer[i] = 0;
    }
  }
}
