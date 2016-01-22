/*
 * COPYRIGHT © 2012-2015. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package com.mcac0006.siftscience.types;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public enum SiftScienceErrorCodes {

    SUCCESS(0),
    INVALID_API_KEY(51),
    INVALID_CHARACTERS_IN_FIELD_NAME(52),
    INVALID_CHARACTERS_IN_FIELD_VALUE(53),
    MISSING_REQUIRED_FIELD(55),
    INVALID_JSON_REQUEST(56),
    INVALID_HTTP_BODY(57),
    RATE_LIMITED(60),
    INVALID_API_VERSION(104),
    NOT_VALID_RESERVED_FIELD(105),
    SYNCHRONOUS_ACTION_TIMEOUT(-3);

    private int errorCode;

    private SiftScienceErrorCodes(final int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

}
