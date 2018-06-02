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
 * CreditCard
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-06-02T15:11:48.569Z")
public class CreditCard {
  @SerializedName("number")
  private String number = null;

  @SerializedName("expirationdate")
  private String expirationdate = null;

  @SerializedName("validationcode")
  private String validationcode = null;

  public CreditCard number(String number) {
    this.number = number;
    return this;
  }

   /**
   * Get number
   * @return number
  **/
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

