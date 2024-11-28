package com.sample.base.common.service;

public interface EmailService<R> {
    public R sendEmail(String to, String subject, String content);
}
