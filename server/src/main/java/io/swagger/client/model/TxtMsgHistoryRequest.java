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

/**
 * TxtMsgHistoryRequest
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-06-02T15:11:48.569Z")
public class TxtMsgHistoryRequest {
  @SerializedName("phonenumber")
  private String phonenumber = null;

  @SerializedName("period")
  private String period = null;

  public TxtMsgHistoryRequest phonenumber(String phonenumber) {
    this.phonenumber = phonenumber;
    return this;
  }

   /**
   * Get phonenumber
   * @return phonenumber
  **/
  @ApiModelProperty(value = "")
  public String getPhonenumber() {
    return phonenumber;
  }

  public void setPhonenumber(String phonenumber) {
    this.phonenumber = phonenumber;
  }

  public TxtMsgHistoryRequest period(String period) {
    this.period = period;
    return this;
  }

   /**
   * Get period
   * @return period
  **/
  @ApiModelProperty(value = "")
  public String getPeriod() {
    return period;
  }

  public void setPeriod(String period) {
    this.period = period;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TxtMsgHistoryRequest txtMsgHistoryRequest = (TxtMsgHistoryRequest) o;
    return Objects.equals(this.phonenumber, txtMsgHistoryRequest.phonenumber) &&
        Objects.equals(this.period, txtMsgHistoryRequest.period);
  }

  @Override
  public int hashCode() {
    return Objects.hash(phonenumber, period);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TxtMsgHistoryRequest {\n");
    
    sb.append("    phonenumber: ").append(toIndentedString(phonenumber)).append("\n");
    sb.append("    period: ").append(toIndentedString(period)).append("\n");
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

