package yh.subsys.internationalOrg.logic;
/**
* Title:获得汉字的拼音首字母
* Description: GB 2312-80 把收录的汉字分成两级。第一级汉字是常用汉字，计 3755 个，
* 置于 16～55 区，按汉语拼音字母／笔形顺序排列；第二级汉字是次常用汉字，
* 计 3008 个，置于 56～87 区，按部首／笔画顺序排列，所以本程序只能查到
* 对一级汉字的声母。同时对符合声母（zh，ch，sh）只能取首字母（z，c，s） 
* Copyright: Copyright (c) 2004
* Company: 
* @author not attributable
* @version 1.0
*/
public class YHGetFirstLetter { 

// 国标码和区位码转换常量
private static final int GB_SP_DIFF = 160;

//存放国标一级汉字不同读音的起始区位码
private static final int[] secPosvalueList =  {1601,1637,1833,2078,2274,2302,2433,2594,2787,3106,3212,3472,3635,3722,3730,3858,4027,4086,4390,4558,4684,4925,5249   }; 


//存放国标一级汉字不同读音的起始区位码对应读音
private static final char[] firstLetter = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'w', 'x', 'y', 'z'} ;


//获取一个字符串的拼音码
public static String getFirstLetter(String oriStr) {
    String str = oriStr.toLowerCase();
    StringBuffer buffer = new StringBuffer();
    char ch;
    char[] temp;
    for (int i = 0; i < str.length(); i++) { //依次处理str中每个字符
      ch = str.charAt(i);
      temp = new char[] {ch};
      byte[] uniCode = new String(temp).getBytes();
      if (uniCode[0] < 128 && uniCode[0] > 0) { // 非汉字
        buffer.append(temp);
      }
      else {
        buffer.append(convert(uniCode));
      }
    }
    System.out.println(oriStr+":"+buffer.toString());
    return buffer.toString();
}

/** 获取一个汉字的拼音首字母。
   * GB码两个字节分别减去160，转换成10进制码组合就可以得到区位码
   * 例如汉字"你"的GB码是0xC4/0xE3，分别减去0xA0（160）就是0x24/0x43
   * 0x24转成10进制就是36，0x43是67，那么它的区位码就是3667，在对照表中读音为‘n'
   */

private static char convert(byte[] bytes) {

    char result = '-';
    int secPosvalue = 0;
    int i;
    for (i = 0; i < bytes.length; i++) {
      bytes[i] -= GB_SP_DIFF;
    }
    secPosvalue = bytes[0] * 100 + bytes[1];
    for (i = 0; i < 24; i++) {
      if (secPosvalue >= secPosvalueList[i] &&
          secPosvalue < secPosvalueList[i + 1]) {
        result = firstLetter[i];
        break;
      }
    }
    return result;
}

//public static void main(String[] args){
//    String str = getFirstLetter("中国");
//    System.out.println(str);
//}

}

