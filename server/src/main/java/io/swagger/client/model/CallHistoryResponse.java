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
 * CallHistoryResponse
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-06-03T22:43:15.870Z")
public class CallHistoryResponse {
  @SerializedName("inout")
  private String inout = null;

  @SerializedName("phoneNumber")
  private String phoneNumber = null;

  @SerializedName("duration")
  private Integer duration = null;

  @SerializedName("cost")
  private BigDecimal cost = null;

  public CallHistoryResponse inout(String inout) {
    this.inout = inout;
    return this;
  }

   /**
   * Get inout
   * @return inout
  **/
  @ApiModelProperty(value = "")
  public String getInout() {
    return inout;
  }

  public void setInout(String inout) {
    this.inout = inout;
  }

  public CallHistoryResponse phoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

   /**
   * Get phoneNumber
   * @return phoneNumber
  **/
  @ApiModelProperty(value = "")
  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public CallHistoryResponse duration(Integer duration) {
    this.duration = duration;
    return this;
  }

   /**
   * Get duration
   * @return duration
  **/
  @ApiModelProperty(value = "")
  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public CallHistoryResponse cost(BigDecimal cost) {
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
    CallHistoryResponse callHistoryResponse = (CallHistoryResponse) o;
    return Objects.equals(this.inout, callHistoryResponse.inout) &&
        Objects.equals(this.phoneNumber, callHistoryResponse.phoneNumber) &&
        Objects.equals(this.duration, callHistoryResponse.duration) &&
        Objects.equals(this.cost, callHistoryResponse.cost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inout, phoneNumber, duration, cost);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CallHistoryResponse {\n");
    
    sb.append("    inout: ").append(toIndentedString(inout)).append("\n");
    sb.append("    phoneNumber: ").append(toIndentedString(phoneNumber)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
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

