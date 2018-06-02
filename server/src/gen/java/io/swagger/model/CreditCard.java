/*
 * Design craft
 * This is a Design craft server.  
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
 * CreditCard
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-02T19:54:34.446Z")
public class CreditCard   {
  @JsonProperty("number")
  private String number = null;

  @JsonProperty("expirationdate")
  private String expirationdate = null;

  @JsonProperty("validationcode")
  private String validationcode = null;

  public CreditCard number(String number) {
    this.number = number;
    return this;
  }

  /**
   * Get number
   * @return number
   **/
  @JsonProperty("number")
  @ApiModelProperty(value = "")
  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public CreditCard expirationdate(String expirationdate) {
    this.expirationdate = expirationdate;
    return this;
  }

  /**
   * Get expirationdate
   * @return expirationdate
   **/
  @JsonProperty("expirationdate")
  @ApiModelProperty(value = "")
  public String getExpirationdate() {
    return expirationdate;
  }

  public void setExpirationdate(String expirationdate) {
    this.expirationdate = expirationdate;
  }

  public CreditCard validationcode(String validationcode) {
    this.validationcode = validationcode;
    return this;
  }

  /**
   * Get validationcode
   * @return validationcode
   **/
  @JsonProperty("validationcode")
  @ApiModelProperty(value = "")
  public String getValidationcode() {
    return validationcode;
  }

  public void setValidationcode(String validationcode) {
    this.validationcode = validationcode;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreditCard creditCard = (CreditCard) o;
    return Objects.equals(this.number, creditCard.number) &&
        Objects.equals(this.expirationdate, creditCard.expirationdate) &&
        Objects.equals(this.validationcode, creditCard.validationcode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(number, expirationdate, validationcode);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreditCard {\n");
    
    sb.append("    number: ").append(toIndentedString(number)).append("\n");
    sb.append("    expirationdate: ").append(toIndentedString(expirationdate)).append("\n");
    sb.append("    validationcode: ").append(toIndentedString(validationcode)).append("\n");
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

