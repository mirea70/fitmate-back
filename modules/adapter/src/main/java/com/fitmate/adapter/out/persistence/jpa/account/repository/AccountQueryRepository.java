package com.fitmate.adapter.out.persistence.jpa.account.repository;

public interface AccountQueryRepository {
    boolean checkDuplicated(Long accountId, String nickName, String name, String email, String phone);
}
