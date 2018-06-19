package io.swagger.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public class CCDialResponse {
	  @JsonProperty("ip")
	  private String ip = null;

	  public CCDialResponse ip(String ip) {
	    this.ip = ip;
	    return this;
	  }

	  /**
	   * Get ip
	   * @return ip
	   **/
	  @JsonProperty("ip")
	  @ApiModelProperty(value = "")
	  public String getIp() {
	    return ip;
	  }

	  public void setIp(String ip) {
	    this.ip = ip;
	  }


	  @Override
	  public boolean equals(java.lang.Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    CCDialResponse ccDialResponse = (CCDialResponse) o;
	    return Objects.equals(this.ip, ccDialResponse.ip);
	  }

	  @Override
	  public int hashCode() {
	    return Objects.hash(ip);
	  }


	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class CCDialResponse {\n");
	    
	    sb.append("    ip: ").append(toIndentedString(ip)).append("\n");
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
