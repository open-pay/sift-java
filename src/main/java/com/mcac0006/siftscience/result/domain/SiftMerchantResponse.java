/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package com.mcac0006.siftscience.result.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Setter;
import lombok.Getter;


/**
 * @author Oswaldo Martinez
 *
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE)
@JsonNaming(LowerCaseWithUnderscoresStrategy.class)
public class SiftMerchantResponse {
    
    private String accountId;
    
    private Access production;

    private Access sandbox;

}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE)
@JsonNaming(LowerCaseWithUnderscoresStrategy.class)
class Access {
    private List<KeyRecord> apiKeys;

    private List<KeyRecord> beaconKeys;

}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE)
@JsonNaming(LowerCaseWithUnderscoresStrategy.class)
class KeyRecord {
    private String id;
    private String state;
    private String key;
}
