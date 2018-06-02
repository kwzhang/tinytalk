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
 * NewPasswordInfo
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-06-02T19:54:29.108Z")
public class NewPasswordInfo {
  @SerializedName("phonenumber")
  private String phonenumber = null;

  @SerializedName("oldpassword")
  private String oldpassword = null;

  @SerializedName("newpassword")
  private String newpassword = null;

  public NewPasswordInfo phonenumber(String phonenumber) {
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

  public NewPasswordInfo oldpassword(String oldpassword) {
    this.oldpassword = oldpassword;
    return this;
  }

   /**
   * Get oldpassword
   * @return oldpassword
  **/
  @ApiModelProperty(value = "")
  public String getOldpassword() {
    return oldpassword;
  }

  public void setOldpassword(String oldpassword) {
    this.oldpassword = oldpassword;
  }

  public NewPasswordInfo newpassword(String newpassword) {
    this.newpassword = newpassword;
    return this;
  }

   /**
   * Get newpassword
   * @return newpassword
  **/
  @ApiModelProperty(value = "")
  public String getNewpassword() {
    return newpassword;
  }

  public void setNewpassword(String newpassword) {
    this.newpassword = newpassword;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NewPasswordInfo newPasswordInfo = (NewPasswordInfo) o;
    return Objects.equals(this.phonenumber, newPasswordInfo.phonenumber) &&
        Objects.equals(this.oldpassword, newPasswordInfo.oldpassword) &&
        Objects.equals(this.newpassword, newPasswordInfo.newpassword);
  }

  @Override
  public int hashCode() {
    return Objects.hash(phonenumber, oldpassword, newpassword);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NewPasswordInfo {\n");
    
    sb.append("    phonenumber: ").append(toIndentedString(phonenumber)).append("\n");
    sb.append("    oldpassword: ").append(toIndentedString(oldpassword)).append("\n");
    sb.append("    newpassword: ").append(toIndentedString(newpassword)).append("\n");
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

