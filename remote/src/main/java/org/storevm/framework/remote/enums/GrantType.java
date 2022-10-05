package org.storevm.framework.remote.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author jack
 */
@Getter
public enum GrantType {
    AUTH_CODE("authorization_code"),

    PASSWORD("password"),

    CLIENT_CREDENTIALS("client_credentials"),

    REFRESH_TOKEN("refresh_token"),

    ;

    private String code;

    GrantType(String code) {
        this.code = code;
    }

    public static GrantType valuesOf(String code) {
        GrantType[] enums = values();
        if (enums != null) {
            for (GrantType item : enums) {
                if (StringUtils.equals(item.code, code)) {
                    return item;
                }
            }
        }
        return null;
    }
}
