/*
 * Swagger Petstore
 * This is a sample Petstore server.  You can find  out more about Swagger at  [http://swagger.io](http://swagger.io) or on  [irc.freenode.net, #swagger](http://swagger.io/irc/). 
 *
 * OpenAPI spec version: 1.0.0
 * Contact: apiteam@swagger.io
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * NewPasswordInfo
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-27T15:54:36.606Z")
public class NewPasswordInfo   {
  @JsonProperty("phonenumber")
  private String phonenumber = null;

  @JsonProperty("oldpassword")
  private String oldpassword = null;

  @JsonProperty("newpassword")
  private String newpassword = null;

  public NewPasswordInfo phonenumber(String phonenumber) {
    this.phonenumber = phonenumber;
    return this;
  }

  /**
   * Get phonenumber
   * @return phonenumber
   **/
  @JsonProperty("phonenumber")
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
  @JsonProperty("oldpassword")
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
  @JsonProperty("newpassword")
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

