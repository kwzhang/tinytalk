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
import java.math.BigDecimal;
import javax.validation.constraints.*;

/**
 * TxtMsgHistoryResponse
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-02T19:54:34.446Z")
public class TxtMsgHistoryResponse   {
  @JsonProperty("sentbytes")
  private Integer sentbytes = null;

  @JsonProperty("recievebytes")
  private Integer recievebytes = null;

  @JsonProperty("cost")
  private BigDecimal cost = null;

  public TxtMsgHistoryResponse sentbytes(Integer sentbytes) {
    this.sentbytes = sentbytes;
    return this;
  }

  /**
   * Get sentbytes
   * @return sentbytes
   **/
  @JsonProperty("sentbytes")
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
  @JsonProperty("recievebytes")
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
  @JsonProperty("cost")
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

