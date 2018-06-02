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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-06-02T19:54:29.108Z")
public class TxtMsgHistoryResponse {
  @SerializedName("sentbytes")
  private Integer sentbytes = null;

  @SerializedName("recievebytes")
  private Integer recievebytes = null;

  @SerializedName("cost")
  private BigDecimal cost = null;

  public TxtMsgHistoryResponse sentbytes(Integer sentbytes) {
    this.sentbytes = sentbytes;
    return this;
  }

   /**
   * Get sentbytes
   * @return sentbytes
  **/
  @ApiModelProperty(value = "")
  public Integer getSentbytes() {
    return sentbytes;
  }

  public void setSentbytes(Integer sentbytes) {
    this.sentbytes = sentbytes;
  }

  public TxtMsgHistoryResponse recievebytes(Integer recievebytes) {
    this.recievebytes = recievebytes;
    return this;
  }

   /**
   * Get recievebytes
   * @return recievebytes
  **/
  @ApiModelProperty(value = "")
  public Integer getRecievebytes() {
    return recievebytes;
  }

  public void setRecievebytes(Integer recievebytes) {
    this.recievebytes = recievebytes;
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
    return Objects.equals(this.sentbytes, txtMsgHistoryResponse.sentbytes) &&
        Objects.equals(this.recievebytes, txtMsgHistoryResponse.recievebytes) &&
        Objects.equals(this.cost, txtMsgHistoryResponse.cost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sentbytes, recievebytes, cost);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TxtMsgHistoryResponse {\n");
    
    sb.append("    sentbytes: ").append(toIndentedString(sentbytes)).append("\n");
    sb.append("    recievebytes: ").append(toIndentedString(recievebytes)).append("\n");
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

