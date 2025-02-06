package dev.mini.minibox.results.user;

import dev.mini.minibox.results.Result;

public enum LoginResult implements Result {
    FAILURE_NOT_VERIFIED,
    FAILURE_SUSPENDED,
    FAILURE_DELETED
}
