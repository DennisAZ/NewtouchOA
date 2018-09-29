package yh.core.util.auth;

import java.io.UnsupportedEncodingException;

import yh.core.global.YHConst;
import yh.core.global.YHSysProps;

public class YHPassEncrypt {
  private static final byte[] MD5_MAGIC = new byte[]{'$', '1', '$'};
  private static final int MD5_MAGIC_LEN = 3;
  private static final int PHP_MAX_SALT_LEN = 12;
  
  private static final int S11 = 7;
  private static final int S12 = 12;
  private static final int S13 = 17;
  private static final int S14 = 22;
  private static final int S21 = 5;
  private static final int S22 = 9;
  private static final int S23 = 14;
  private static final int S24 = 20;
  private static final int S31 = 4;
  private static final int S32 = 11;
  private static final int S33 = 16;
  private static final int S34 = 23;
  private static final int S41 = 6;
  private static final int S42 = 10;
  private static final int S43 = 15;
  private static final int S44 = 21;

  private static byte[] itoa16 = new byte[]{'0','1','2','3','4','5','6','7','8','9',
    'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O',
    'P','Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d',
    'e','f','g','h','i','j','k','l','m','n','o','p','q','r','s',
    't','u','v','w','x','y','z'};
  private static byte[] itoa64 = new byte[]{'.','/','0','1','2','3','4','5','6','7','8','9',
    'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O',
    'P','Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d',
    'e','f','g','h','i','j','k','l','m','n','o','p','q','r','s',
    't','u','v','w','x','y','z'};

  private static byte[] PADDING = new byte[] {
    (byte)0x80, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
  };
  
  public static void outputLong(long src) {
    byte[] array = new byte[16];
    long src2 = src;
    for (int i = 0; i < 16; i++) {
      //System.out.print(itoa16[(int)(src & 0xF000000000000000)]);
      array[15 - i] = (byte)(src2 & 0x000000000000000Fl);
      src2 = src2 >>> 4;
    }
    for (int i = 0; i < 16; i++) {
      System.out.print((char)itoa16[array[i]]);
    }
    //System.out.println();
  }
  public static void outputInt(int src) {
    byte[] array = new byte[8];
    int src2 = src;
    for (int i = 0; i < 8; i++) {
      //System.out.print(itoa16[(int)(src & 0xF000000000000000)]);
      array[7 - i] = (byte)(src2 & 0x0000000Fl);
      src2 = src2 >>> 4;
    }
    for (int i = 0; i < 8; i++) {
      System.out.print((char)itoa16[array[i]]);
    }
    //System.out.println();
  }
  public static long int2Long(int src) {
    long rtValue = (long)src & 0x00000000FFFFFFFFl;
    return rtValue;
  }
  public static int add(int i1, int i2) {
    long rtValue = int2Long(i1) + int2Long(i2);
    rtValue &= 0x00000000FFFFFFFFl;
    return (int)rtValue;
  }
  public static int byte2Int(byte src) {
    int rtValue = (int)src & 0x000000FF;
    //outputLong(rtValue);
    return rtValue;
  }
  
  /* F, G, H and I are basic MD5 functions.
   */
  //#define F(x, y, z) (((x) & (y)) | ((~x) & (z)))
  private static int F(int x, int y, int z) {
    return (((x) & (y)) | ((~x) & (z)));
  }
  //#define G(x, y, z) (((x) & (z)) | ((y) & (~z)))
  private static int G(int x, int y, int z) {
    return (((x) & (z)) | ((y) & (~z)));
  }
  //#define H(x, y, z) ((x) ^ (y) ^ (z))
  private static int H(int x, int y, int z) {
    return ((x) ^ (y) ^ (z));
  }
  //#define I(x, y, z) ((y) ^ ((x) | (~z)))
  private static int I(int x, int y, int z) {
    return ((y) ^ ((x) | (~z)));
  }

  /* ROTATE_LEFT rotates x left n bits.
   */
  //#define ROTATE_LEFT(x, n) (((x) << (n)) | ((x) >> (32-(n))))
  private static int ROTATE_LEFT(int x, int n) {
    return ((x) << (n)) | ((x) >>> (32-(n)));
  }

  /* FF, GG, HH, and II transformations for rounds 1, 2, 3, and 4.
     Rotation is separate from addition to prevent recomputation.
   */
//  #define FF(a, b, c, d, x, s, ac) { \
//   (a) += F ((b), (c), (d)) + (x) + (php_uint32)(ac); \
//   (a) = ROTATE_LEFT ((a), (s)); \
//   (a) += (b); \
//    }
  private static int FF(int a, int b, int c, int d, int x, int s, int ac) {
    int a0 = add(add(F((b), (c), (d)), (x)), ac);
    (a) = add(a, a0);    
    (a) = ROTATE_LEFT (a, s);
    (a) = add(a, (b));
    return (a);
  }
//  #define GG(a, b, c, d, x, s, ac) { \
//   (a) += G ((b), (c), (d)) + (x) + (php_uint32)(ac); \
//   (a) = ROTATE_LEFT ((a), (s)); \
//   (a) += (b); \
//    }
  private static int GG(int a, int b, int c, int d, int x, int s, int ac) {
    int a0 = add(add(G((b), (c), (d)), (x)), ac);
    (a) = add(a, a0);    
    (a) = ROTATE_LEFT (a, s);
    (a) = add(a, (b));
    return (a);
  }
//  #define HH(a, b, c, d, x, s, ac) { \
//   (a) += H ((b), (c), (d)) + (x) + (php_uint32)(ac); \
//   (a) = ROTATE_LEFT ((a), (s)); \
//   (a) += (b); \
//    }
  private static int HH(int a, int b, int c, int d, int x, int s, int ac) {
    int a0 = add(add(H((b), (c), (d)), (x)), ac);
    (a) = add(a, a0);    
    (a) = ROTATE_LEFT (a, s);
    (a) = add(a, (b));
    return (a);
  }
//  #define II(a, b, c, d, x, s, ac) { \
//   (a) += I ((b), (c), (d)) + (x) + (php_uint32)(ac); \
//   (a) = ROTATE_LEFT ((a), (s)); \
//   (a) += (b); \
//    }
  private static int II(int a, int b, int c, int d, int x, int s, int ac) {
    int a0 = add(add(I((b), (c), (d)), (x)), ac);
    (a) = add(a, a0);    
    (a) = ROTATE_LEFT (a, s);
    (a) = add(a, (b));
    return (a);
  }
  
  /* {{{ Encode
    Encodes input (php_uint32) into output (unsigned char). Assumes len is
    a multiple of 4.
  */
  private static void Encode(byte[] output, int[] input, int len) {
    int i, j;  
    for (i = 0, j = 0; j < len; i++, j += 4) {
      output[j] = (byte) (input[i] & 0x000000ff);
      output[j + 1] = (byte) ((input[i] >>> 8) & 0x000000ff);
      output[j + 2] = (byte) ((input[i] >>> 16) & 0x000000ff);
      output[j + 3] = (byte) ((input[i] >>> 24) & 0x000000ff);
    }
  }
  private static void Decode(int[] output, byte[] input, int len)
  {
    int i, j;
    for (i = 0, j = 0; j < len; i++, j += 4) {
      output[i] = byte2Int(input[j]) | (byte2Int(input[j + 1]) << 8) |
        (byte2Int(input[j + 2]) << 16) | (byte2Int(input[j + 3]) << 24);
    }
  }
  
  /* {{{ PHP_MD5Init
   * MD5 initialization. Begins an MD5 operation, writing a new context.
   */
  private static void PHP_MD5Init(YHMD5Context context)
  {
    context.count[0] = context.count[1] = 0;
    /* Load magic initialization constants.
     */
    context.state[0] = 0x67452301;
    context.state[1] = 0xefcdab89;
    context.state[2] = 0x98badcfe;
    context.state[3] = 0x10325476;
  }
  
  /**
   * 内存拷贝
   * @param dest
   * @param src
   * @param offset
   * @param length
   */
  private static void memcpy(byte[] dest, byte[] src, int offsetDest, int offesetSrc, int length) {
    for (int i = 0; i < length; i++) {
      dest[offsetDest + i] = src[offesetSrc + i];
    }
  }

  /* {{{ PHP_MD5Update
    MD5 block update operation. Continues an MD5 message-digest
    operation, processing another message block, and updating the
    context.
  */
  private static void PHP_MD5Update(YHMD5Context context, byte[] input, int inputLen) {
    int i, index, partLen;
  
    /* Compute number of bytes mod 64 */
    index = (int) ((context.count[0] >>> 3) & 0x3F);
  
    /* Update number of bits */
    if (int2Long(context.count[0] = add(context.count[0], (inputLen << 3))) < int2Long(inputLen << 3)) {
      context.count[1] = add(context.count[1], 1);
    }
    context.count[1] = add(context.count[1], (inputLen >>> 29));
  
    partLen = 64 - index;
  
    /* Transform as many times as possible.
     */
    if (inputLen >= partLen) {
      memcpy(context.buffer, input, index, 0, partLen);
      MD5Transform(context.state, context.buffer);
  
      for (i = partLen; i + 63 < inputLen; i += 64) {
        byte[] buff = new byte[64];
        memset(buff, (byte)0);
        int len = input.length - i;
        memcpy(buff, input, 0, i, len < 64 ? len : 64);
        MD5Transform(context.state, buff);
      }
      index = 0;
    } else {
      i = 0;
    }
    /* Buffer remaining input */
    memcpy(context.buffer, input, index, i, inputLen - i);
  }
  /* }}} */
  
  private static void memset(byte[] src, byte value) {
    for (int i = 0; i < src.length; i++) {
      src[i] = value;
    }
  }

  private static void memset(int[] src, int value) {
    for (int i = 0; i < src.length; i++) {
      src[i] = value;
    }
  }

  /* {{{ MD5Transform
   * state 长度为 4个元素，block长度为 64个元素

   * MD5 basic transformation. Transforms state based on block.
   */
  private static void MD5Transform(int[] state, byte[] block)
  {
    int a = state[0], b = state[1], c = state[2], d = state[3];
    int[] x = new int[16];

    Decode(x, block, 64);

    /* Round 1 */
    a = FF(a, b, c, d, x[0], S11, 0xd76aa478);  /* 1 */
    d = FF(d, a, b, c, x[1], S12, 0xe8c7b756);  /* 2 */
    c = FF(c, d, a, b, x[2], S13, 0x242070db);  /* 3 */
    b = FF(b, c, d, a, x[3], S14, 0xc1bdceee);  /* 4 */
    a = FF(a, b, c, d, x[4], S11, 0xf57c0faf);  /* 5 */
    d = FF(d, a, b, c, x[5], S12, 0x4787c62a);  /* 6 */
    c = FF(c, d, a, b, x[6], S13, 0xa8304613);  /* 7 */
    b = FF(b, c, d, a, x[7], S14, 0xfd469501);  /* 8 */
    a = FF(a, b, c, d, x[8], S11, 0x698098d8);  /* 9 */
    d = FF(d, a, b, c, x[9], S12, 0x8b44f7af);  /* 10 */
    c = FF(c, d, a, b, x[10], S13, 0xffff5bb1);   /* 11 */
    b = FF(b, c, d, a, x[11], S14, 0x895cd7be);   /* 12 */
    a = FF(a, b, c, d, x[12], S11, 0x6b901122);   /* 13 */
    d = FF(d, a, b, c, x[13], S12, 0xfd987193);   /* 14 */
    c = FF(c, d, a, b, x[14], S13, 0xa679438e);   /* 15 */
    b = FF(b, c, d, a, x[15], S14, 0x49b40821);   /* 16 */

    /* Round 2 */
    a = GG(a, b, c, d, x[1], S21, 0xf61e2562);  /* 17 */
    d = GG(d, a, b, c, x[6], S22, 0xc040b340);  /* 18 */    
    c = GG(c, d, a, b, x[11], S23, 0x265e5a51);   /* 19 */
    b = GG(b, c, d, a, x[0], S24, 0xe9b6c7aa);  /* 20 */
    a = GG(a, b, c, d, x[5], S21, 0xd62f105d);  /* 21 */
    d = GG(d, a, b, c, x[10], S22, 0x2441453);  /* 22 */
    c = GG(c, d, a, b, x[15], S23, 0xd8a1e681);   /* 23 */
    b = GG(b, c, d, a, x[4], S24, 0xe7d3fbc8);  /* 24 */
    a = GG(a, b, c, d, x[9], S21, 0x21e1cde6);  /* 25 */
    d = GG(d, a, b, c, x[14], S22, 0xc33707d6);   /* 26 */
    c = GG(c, d, a, b, x[3], S23, 0xf4d50d87);  /* 27 */
    b = GG(b, c, d, a, x[8], S24, 0x455a14ed);  /* 28 */
    a = GG(a, b, c, d, x[13], S21, 0xa9e3e905);   /* 29 */
    d = GG(d, a, b, c, x[2], S22, 0xfcefa3f8);  /* 30 */
    c = GG(c, d, a, b, x[7], S23, 0x676f02d9);  /* 31 */
    b = GG(b, c, d, a, x[12], S24, 0x8d2a4c8a);   /* 32 */

    /* Round 3 */
    a = HH(a, b, c, d, x[5], S31, 0xfffa3942);  /* 33 */
    d = HH(d, a, b, c, x[8], S32, 0x8771f681);  /* 34 */
    c = HH(c, d, a, b, x[11], S33, 0x6d9d6122);   /* 35 */
    b = HH(b, c, d, a, x[14], S34, 0xfde5380c);   /* 36 */
    a = HH(a, b, c, d, x[1], S31, 0xa4beea44);  /* 37 */
    d = HH(d, a, b, c, x[4], S32, 0x4bdecfa9);  /* 38 */
    c = HH(c, d, a, b, x[7], S33, 0xf6bb4b60);  /* 39 */
    b = HH(b, c, d, a, x[10], S34, 0xbebfbc70);   /* 40 */
    a = HH(a, b, c, d, x[13], S31, 0x289b7ec6);   /* 41 */
    d = HH(d, a, b, c, x[0], S32, 0xeaa127fa);  /* 42 */
    c = HH(c, d, a, b, x[3], S33, 0xd4ef3085);  /* 43 */
    b = HH(b, c, d, a, x[6], S34, 0x4881d05); /* 44 */
    a = HH(a, b, c, d, x[9], S31, 0xd9d4d039);  /* 45 */
    d = HH(d, a, b, c, x[12], S32, 0xe6db99e5);   /* 46 */
    c = HH(c, d, a, b, x[15], S33, 0x1fa27cf8);   /* 47 */
    b = HH(b, c, d, a, x[2], S34, 0xc4ac5665);  /* 48 */

    /* Round 4 */
    a = II(a, b, c, d, x[0], S41, 0xf4292244);  /* 49 */
    d = II(d, a, b, c, x[7], S42, 0x432aff97);  /* 50 */
    c = II(c, d, a, b, x[14], S43, 0xab9423a7);   /* 51 */
    b = II(b, c, d, a, x[5], S44, 0xfc93a039);  /* 52 */
    a = II(a, b, c, d, x[12], S41, 0x655b59c3);   /* 53 */
    d = II(d, a, b, c, x[3], S42, 0x8f0ccc92);  /* 54 */
    c = II(c, d, a, b, x[10], S43, 0xffeff47d);   /* 55 */
    b = II(b, c, d, a, x[1], S44, 0x85845dd1);  /* 56 */
    a = II(a, b, c, d, x[8], S41, 0x6fa87e4f);  /* 57 */
    d = II(d, a, b, c, x[15], S42, 0xfe2ce6e0);   /* 58 */
    c = II(c, d, a, b, x[6], S43, 0xa3014314);  /* 59 */
    b = II(b, c, d, a, x[13], S44, 0x4e0811a1);   /* 60 */
    a = II(a, b, c, d, x[4], S41, 0xf7537e82);  /* 61 */
    d = II(d, a, b, c, x[11], S42, 0xbd3af235);   /* 62 */
    c = II(c, d, a, b, x[2], S43, 0x2ad7d2bb);  /* 63 */
    b = II(b, c, d, a, x[9], S44, 0xeb86d391);  /* 64 */

    //outputInt(a);outputInt(b);outputInt(c);outputInt(d);
    state[0] = add(state[0], a);
    state[1] = add(state[1], b);
    state[2] = add(state[2], c);
    state[3] = add(state[3], d);

    /* Zeroize sensitive information. */
    memset(x, 0);
  }

  /* {{{ PHP_MD5Final
   * digest 长度是16，

    MD5 finalization. Ends an MD5 message-digest operation, writing the
    the message digest and zeroizing the context.
  */
  private static void PHP_MD5Final(byte[] digest, YHMD5Context context)
  {
    byte[] bits = new byte[8];
    int index = 0;
    int padLen = 0;

   /* Save number of bits */
   Encode(bits, context.count, 8);
  
   /* Pad out to 56 mod 64.
    */
   index = (int)((context.count[0] >>> 3) & 0x3f);
   padLen = (index < 56) ? (56 - index) : (120 - index);
   PHP_MD5Update(context, PADDING, padLen);

   /* Append length (before padding) */
   PHP_MD5Update(context, bits, 8);
  
   /* Store state in digest */
   Encode(digest, context.state, 16);
  
    /* Zeroize sensitive information.
     */
    //memset((unsigned char*) context, 0, sizeof(*context));
    context.set0();
  }
  /* }}} */
  
  private static void to64(byte[] s, int offset, int v, int n) {
    int i = 0;
    while (--n >= 0) {
      s[offset + i++] = itoa64[v & 0x3f];
      v >>>= 6;
    }
  }
  
  private static int strncmp(byte[] str1, byte[] str2, int from1, int from2, int len) {
    for (int i = 0; i < len; i++) {
      if (str1[from1 + i] > str2[from2 + i]) {
        return 1;
      }
      if (str1[from1 + i] < str2[from2 + i]) {
        return -1;
      }
      continue;
    }
    return 0;
  }

  private static byte[] cloneArray(byte[] src, int offset, int len) {
     byte[] rtArray = new byte[len];
     for (int i = 0; i < len; i++) {
       rtArray[i] = src[offset + i];
     }
     return rtArray;
  }
  /*
   * Copy src to string dst of size siz.  At most siz-1 characters
   * will be copied.  Always NUL terminates (unless siz == 0).
   * Returns strlen(src); if retval >= siz, truncation occurred.
   */
  private static int strlcpy(byte[] dst, byte[] src, int siz, int dstOffset, int srcOffset)
  {
    int d = 0;
    int s = 0;
    int n = siz;

    /* Copy as many bytes as will fit */
    if (n != 0 && --n != 0) {
      do {
        if ((dst[dstOffset + d++] = src[srcOffset + s++]) == 0)
          break;
      } while (--n != 0);
    }
    /* Not enough room in dst, add NUL and traverse rest of src */
    if (n == 0) {
      if (siz != 0) {
        dst[dstOffset + d] = 0;    /* NUL-terminate dst */
      }
      while (s < src.length && src[s] != 0) {
        s++;
      }
    }

    return(s - 1);  /* count does not include NUL */
  }
  /*
   * MD5 password encryption.
   */
  private static byte[] md5_crypt(byte[] pw, byte[] salt) {
    byte[] passwd = new byte[120];
    int p = 0;
    int sp = 0;
    int ep = 0;
    byte[] finalArray = new byte[16];
    int i, sl, pwl;
    YHMD5Context ctx = new YHMD5Context();
    YHMD5Context ctx1 = new YHMD5Context();
    int l;
    int pl;
    
    pwl = pw.length;
    
    /* Refine the salt first */
    sp = 0;

    /* If it starts with the magic string, then skip that */
    if (strncmp(salt, MD5_MAGIC, sp, 0, MD5_MAGIC_LEN) == 0) {
      sp += MD5_MAGIC_LEN;
    }

    /* It stops at the first '$', max 8 chars */
    for (ep = sp; salt[ep] != 0 && salt[ep] != '$' && ep < (sp + 8); ep++) {
      continue;
    }

    /* get the length of the true salt */
    sl = ep - sp;

    PHP_MD5Init(ctx);

    /* The password first, since that is what is most unknown */
    PHP_MD5Update(ctx, pw, pwl);

    /* Then our magic string */
    PHP_MD5Update(ctx, MD5_MAGIC, MD5_MAGIC_LEN);

    /* Then the raw salt */
    //PHP_MD5Update(ctx, salt, sl);
    PHP_MD5Update(ctx, cloneArray(salt, sp, sl), sl);

//    ctx.output();
    /* Then just as many characters of the MD5(pw,salt,pw) */
    PHP_MD5Init(ctx1);
    PHP_MD5Update(ctx1, pw, pwl);
    PHP_MD5Update(ctx1, cloneArray(salt, sp, sl), sl);
    PHP_MD5Update(ctx1, pw, pwl);
    PHP_MD5Final(finalArray, ctx1);
//    ctx1.output();

    for (pl = pwl; pl > 0; pl -= 16) {
      PHP_MD5Update(ctx, finalArray, pl > 16 ? 16 : pl);
    }
//    ctx.output();

    /* Don't leave anything around in vm they could use. */
    memset(finalArray, (byte)0);

    /* Then something really weird... */
    for (i = pwl; i != 0; i >>>= 1) {
      if ((i & 1) != 0) {
        PHP_MD5Update(ctx, finalArray, 1);
      }else {
        PHP_MD5Update(ctx, pw, 1);
      }
    }
//    ctx.output();

    /* Now make the output string */
    memcpy(passwd, MD5_MAGIC, 0, 0, MD5_MAGIC_LEN);
    strlcpy(passwd, salt, sl + 1, MD5_MAGIC_LEN, sp);
    passwd[MD5_MAGIC_LEN + sl] = '$';

    PHP_MD5Final(finalArray, ctx);
//    ctx.output();

    /*
     * And now, just to make sure things don't run too fast. On a 60 MHz
     * Pentium this takes 34 msec, so you would need 30 seconds to build
     * a 1000 entry dictionary...
     */
    for (i = 0; i < 1000; i++) {
      PHP_MD5Init(ctx1);

      if ((i & 1) != 0) {
        PHP_MD5Update(ctx1, pw, pwl);
      }else {
        PHP_MD5Update(ctx1, finalArray, 16);
      }

      if ((i % 3) != 0) {
        PHP_MD5Update(ctx1, cloneArray(salt, sp, sl), sl);
      }

      if ((i % 7) != 0) {
        PHP_MD5Update(ctx1, pw, pwl);
      }

      if ((i & 1) != 0) {
        PHP_MD5Update(ctx1, finalArray, 16);        
      }else {
        PHP_MD5Update(ctx1, pw, pwl);
      }

      PHP_MD5Final(finalArray, ctx1);
    }
//    ctx1.output();

    p = sl + MD5_MAGIC_LEN + 1;

    l = (byte2Int(finalArray[0]) << 16) | (byte2Int(finalArray[6])<<8) | byte2Int(finalArray[12]);
    to64(passwd, p, l, 4);
    p += 4;
    l = (byte2Int(finalArray[1])<<16) | (byte2Int(finalArray[7])<<8) | byte2Int(finalArray[13]);
    to64(passwd, p, l, 4); 
    p += 4;
    l = (byte2Int(finalArray[2])<<16) | (byte2Int(finalArray[8])<<8) | byte2Int(finalArray[14]);
    to64(passwd, p, l, 4); 
    p += 4;
    l = (byte2Int(finalArray[3])<<16) | (byte2Int(finalArray[9])<<8) | byte2Int(finalArray[15]);
    to64(passwd, p, l, 4); 
    p += 4;
    l = (byte2Int(finalArray[4])<<16) | (byte2Int(finalArray[10])<<8) | byte2Int(finalArray[ 5]);
    to64(passwd, p, l, 4); 
    p += 4;
    l = byte2Int(finalArray[11]);
    to64(passwd, p, l, 2);
    p += 2;
    passwd[p] = 0;
    
    byte[] rtArray = new byte[p];
    memcpy(rtArray, passwd, 0, 0, p);

    /* Don't leave anything around in vm they could use. */
    //memset(finalArray, 0);
    return (rtArray);
  }
  
  private static byte[] crypt(byte[] pw, byte[] salt)
  {
    if (salt.length > MD5_MAGIC_LEN && strncmp(salt, MD5_MAGIC, 0, 0, MD5_MAGIC_LEN)==0) {
      return md5_crypt(pw, salt);
    } 
    //return des_crypt(pw, salt);
    return null;
  }
  
  private static void php_to64(byte[] s, int v, int n, int sOffset) {
    while (--n >= 0) {
      s[sOffset++] = itoa64[v & 0x3f];    
      v >>>= 6;
    } 
  }
  
  private static byte[] crypt(byte[] pw) {
    byte[] salt = new byte[PHP_MAX_SALT_LEN];
    salt[0] = (byte)'$';
    salt[1] = (byte)'1';
    salt[2] = (byte)'$';
    
    int rand = 0;
    rand = (int)(Math.random() * 1000000000);
    php_to64(salt, rand, 4, 3);
    rand = (int)(Math.random() * 1000000000);
    php_to64(salt, rand, 4, 7);
    salt[11] = (byte)'$';
    return crypt (pw, salt);
  }
  
  /**
   * 密码加密
   * @param srcStr
   * @param salt
   * @return
   */
  public static String encrypt(String srcStr, String salt) {
    try {
      byte[] cryptPass = crypt(srcStr.getBytes("UTF-8"), salt.getBytes("UTF-8"));
      int passLen = cryptPass.length;
      char[] charPass = new char[passLen];
      for (int i = 0; i < passLen; i++) {
        charPass[i] = (char)cryptPass[i];
      }
      return new String(charPass);
    } catch (Exception ex) {
      ex.printStackTrace();
      return "";
    }
  }
  /**
   * 密码加密
   * @param pass
   * @return
   */
  public static String encryptPass(String pass) {
    if (YHSysProps.getString("encryptPassMethod").equalsIgnoreCase("md5")) {
      try {
        return YHDigestUtility.md5Hex(pass.getBytes("UTF-8"));
      } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return "";
    }else {
      byte[] passArray = YHUsbKey.str2Bytes(pass);
      byte[] cryptPass = crypt(passArray);
      int passLen = cryptPass.length;
      char[] charPass = new char[passLen];
      for (int i = 0; i < passLen; i++) {
        charPass[i] = (char)cryptPass[i];
      }
      
      return new String(charPass);
    }
  }
  
  /**
   * 验证密码
   * @param realPass
   * @param encryptPass
   * @return
   */
  public static boolean isValidPas(String realPass, String encryptPass) {

    if (YHSysProps.getString("encryptPassMethod").equalsIgnoreCase("md5")) {
      try {
        String tmpPass = YHDigestUtility.md5Hex(realPass.getBytes("UTF-8")).toUpperCase();
        return tmpPass.equalsIgnoreCase(encryptPass);
      } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return false;
    }else {
      int tmpInt = encryptPass.lastIndexOf("$");
      if (tmpInt < 0) {
        return false;
      }
      byte[] pw = YHUsbKey.str2Bytes(realPass);
      byte[] salt = YHUsbKey.str2Bytes(encryptPass.substring(0, tmpInt + 1));
      byte[] srcPassArray = YHUsbKey.str2Bytes(encryptPass);
      byte[] tmpPass = crypt (pw, salt);
      int len = tmpPass.length;
      if (len != srcPassArray.length) {
        return false;
      }
      for (int i = 0; i < len; i++) {
        if (tmpPass[i] != srcPassArray[i]) {
          return false;
        }
      }
      return true;
    }
  }
}
