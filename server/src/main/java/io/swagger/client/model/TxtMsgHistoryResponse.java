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
 * TxtMsgHistoryResponse
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-06-03T22:43:15.870Z")
public class TxtMsgHistoryResponse {
  @SerializedName("sentBytes")
  private Integer sentBytes = null;

  @SerializedName("receiveBytes")
  private Integer receiveBytes = null;

  @SerializedName("cost")
  private BigDecimal cost = null;

  public TxtMsgHistoryResponse sentBytes(Integer sentBytes) {
    this.sentBytes = sentBytes;
    return this;
  }

   /**
   * Get sentBytes
   * @return sentBytes
  **/
  @ApiModelProperty(value = "")
  public Integer getSentBytes() {
    return sentBytes;
  }

  public void setSentBytes(Integer sentBytes) {
    this.sentBytes = sentBytes;
  }

  public TxtMsgHistoryResponse receiveBytes(Integer receiveBytes) {
    this.receiveBytes = receiveBytes;
    return this;
  }

   /**
   * Get receiveBytes
   * @return receiveBytes
  **/
  @ApiModelProperty(value = "")
  public Integer getReceiveBytes() {
    return receiveBytes;
  }

  public void setReceiveBytes(Integer receiveBytes) {
    this.receiveBytes = receiveBytes;
  }

  public TxtMsgHistoryResponse cost(BigDecimal cost) {
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
    TxtMsgHistoryResponse txtMsgHistoryResponse = (TxtMsgHistoryResponse) o;
    return Objects.equals(this.sentBytes, txtMsgHistoryResponse.sentBytes) &&
        Objects.equals(this.receiveBytes, txtMsgHistoryResponse.receiveBytes) &&
        Objects.equals(this.cost, txtMsgHistoryResponse.cost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sentBytes, receiveBytes, cost);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TxtMsgHistoryResponse {\n");
    
    sb.append("    sentBytes: ").append(toIndentedString(sentBytes)).append("\n");
    sb.append("    receiveBytes: ").append(toIndentedString(receiveBytes)).append("\n");
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

