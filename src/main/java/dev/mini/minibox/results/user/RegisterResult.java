package dev.mini.minibox.results.user;

import dev.mini.minibox.results.Result;

public enum RegisterResult implements Result {
    FAILURE_DUPLICATE_CONTACT,
    FAILURE_DUPLICATE_EMAIL,
    FAILURE_DUPLICATE_NICKNAME,
    FAILURE_PASSWORD
}
