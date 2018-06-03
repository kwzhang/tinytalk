package io.swagger.client.model;

import java.util.Objects;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * BillInformation
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-06-03T22:43:15.870Z")
public class BillInformation {
  @SerializedName("incallTime")
  private Integer incallTime = null;

  @SerializedName("outcallTime")
  private Integer outcallTime = null;

  @SerializedName("sendMsgBytes")
  private Integer sendMsgBytes = null;

  @SerializedName("receiveMsgBytes")
  private Integer receiveMsgBytes = null;

  @SerializedName("cost")
  private BigDecimal cost = null;

  public BillInformation incallTime(Integer incallTime) {
    this.incallTime = incallTime;
    return this;
  }

   /**
   * Get incallTime
   * @return incallTime
  **/
  @ApiModelProperty(value = "")
  public Integer getIncallTime() {
    return incallTime;
  }

  public void setIncallTime(Integer incallTime) {
    this.incallTime = incallTime;
  }

  public BillInformation outcallTime(Integer outcallTime) {
    this.outcallTime = outcallTime;
    return this;
  }

   /**
   * Get outcallTime
   * @return outcallTime
  **/
  @ApiModelProperty(value = "")
  public Integer getOutcallTime() {
    return outcallTime;
  }

  public void setOutcallTime(Integer outcallTime) {
    this.outcallTime = outcallTime;
  }

  public BillInformation sendMsgBytes(Integer sendMsgBytes) {
    this.sendMsgBytes = sendMsgBytes;
    return this;
  }

   /**
   * Get sendMsgBytes
   * @return sendMsgBytes
  **/
  @ApiModelProperty(value = "")
  public Integer getSendMsgBytes() {
    return sendMsgBytes;
  }

  public void setSendMsgBytes(Integer sendMsgBytes) {
    this.sendMsgBytes = sendMsgBytes;
  }

  public BillInformation receiveMsgBytes(Integer receiveMsgBytes) {
    this.receiveMsgBytes = receiveMsgBytes;
    return this;
  }

   /**
   * Get receiveMsgBytes
   * @return receiveMsgBytes
  **/
  @ApiModelProperty(value = "")
  public Integer getReceiveMsgBytes() {
    return receiveMsgBytes;
  }

  public void setReceiveMsgBytes(Integer receiveMsgBytes) {
    this.receiveMsgBytes = receiveMsgBytes;
  }

  public BillInformation cost(BigDecimal cost) {
    this.cost = cost;
    return this;
  }

   /**
   * Get cost
   * @return cost
  **/
  @ApiModelProperty(value = "")
  public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BillInformation billInformation = (BillInformation) o;
    return Objects.equals(this.incallTime, billInformation.incallTime) &&
        Objects.equals(this.outcallTime, billInformation.outcallTime) &&
        Objects.equals(this.sendMsgBytes, billInformation.sendMsgBytes) &&
        Objects.equals(this.receiveMsgBytes, billInformation.receiveMsgBytes) &&
        Objects.equals(this.cost, billInformation.cost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(incallTime, outcallTime, sendMsgBytes, receiveMsgBytes, cost);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BillInformation {\n");
    
    sb.append("    incallTime: ").append(toIndentedString(incallTime)).append("\n");
    sb.append("    outcallTime: ").append(toIndentedString(outcallTime)).append("\n");
    sb.append("    sendMsgBytes: ").append(toIndentedString(sendMsgBytes)).append("\n");
    sb.append("    receiveMsgBytes: ").append(toIndentedString(receiveMsgBytes)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

