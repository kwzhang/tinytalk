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
 * TxtMsgRequest
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-06-02T15:11:48.569Z")
public class TxtMsgRequest {
  @SerializedName("sender")
  private String sender = null;

  @SerializedName("reciever")
  private String reciever = null;

  @SerializedName("msg")
  private String msg = null;

  public TxtMsgRequest sender(String sender) {
    this.sender = sender;
    return this;
  }

   /**
   * Get sender
   * @return sender
  **/
  @ApiModelProperty(value = "")
  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public TxtMsgRequest reciever(String reciever) {
    this.reciever = reciever;
    return this;
  }

   /**
   * Get reciever
   * @return reciever
  **/
  @ApiModelProperty(value = "")
  public String getReciever() {
    return reciever;
  }

  public void setReciever(String reciever) {
    this.reciever = reciever;
  }

  public TxtMsgRequest msg(String msg) {
    this.msg = msg;
    return this;
  }

   /**
   * Get msg
   * @return msg
  **/
  @ApiModelProperty(value = "")
  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TxtMsgRequest txtMsgRequest = (TxtMsgRequest) o;
    return Objects.equals(this.sender, txtMsgRequest.sender) &&
        Objects.equals(this.reciever, txtMsgRequest.reciever) &&
        Objects.equals(this.msg, txtMsgRequest.msg);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sender, reciever, msg);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TxtMsgRequest {\n");
    
    sb.append("    sender: ").append(toIndentedString(sender)).append("\n");
    sb.append("    reciever: ").append(toIndentedString(reciever)).append("\n");
    sb.append("    msg: ").append(toIndentedString(msg)).append("\n");
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

