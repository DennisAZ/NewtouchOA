package yh.subsys.oa.vote.data;

public class YHVoteItem {
  private int seqId;//  SEQ_ID  int 流水号
  private int voteId;//  VOTE_ID Int 投票记录表ID
  private String itemName;//  ITEM_NAME CLOB  项目名称
  private int voteCount;//  VOTE_COUNT  INT 投票记录数
  private String voteUser;//  VOTE_USER CLOB  投票的用户ID字符串
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getVoteId() {
    return voteId;
  }
  public void setVoteId(int voteId) {
    this.voteId = voteId;
  }
  public String getItemName() {
    return itemName;
  }
  public void setItemName(String itemName) {
    this.itemName = itemName;
  }
  public int getVoteCount() {
    return voteCount;
  }
  public void setVoteCount(int voteCount) {
    this.voteCount = voteCount;
  }
  public String getVoteUser() {
    return voteUser;
  }
  public void setVoteUser(String voteUser) {
    this.voteUser = voteUser;
  }
}
