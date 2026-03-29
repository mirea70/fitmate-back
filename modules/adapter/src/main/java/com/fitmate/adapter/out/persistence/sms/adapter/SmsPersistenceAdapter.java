package com.fitmate.adapter.out.persistence.sms.adapter;

import com.fitmate.adapter.PersistenceAdapter;
import com.fitmate.adapter.out.persistence.sms.util.SmsUtil;
import com.fitmate.adapter.out.persistence.redis.code.entity.ValidateCodeRedisEntity;
import com.fitmate.adapter.out.persistence.redis.code.repository.ValidateCodeRepository;
import com.fitmate.domain.error.exceptions.NotFoundException;
import com.fitmate.domain.error.results.NotFoundErrorResult;
import com.fitmate.port.out.sms.LoadSmsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@PersistenceAdapter
@RequiredArgsConstructor
@Transactional
public class SmsPersistenceAdapter implements LoadSmsPort {

    private final ValidateCodeRepository validateCodeRepository;
    private final SmsUtil smsUtil;

    @Override
    public void saveValidateCode(String phone, String code) {
        ValidateCodeRedisEntity validateCodeRedisEntity = new ValidateCodeRedisEntity(phone, code);
        validateCodeRepository.save(validateCodeRedisEntity);
    }

    @Override
    public void sendMessageOne(String to, String content) {
        smsUtil.sendOne(to, content);
    }

    @Override
    public void checkValidateCode(String phone, String code) {
        boolean isValid = validateCodeRepository.findById(phone)
                .map(entity -> entity.getCode().equals(code))
                .orElse(false);
        if (!isValid)
            throw new NotFoundException(NotFoundErrorResult.NOT_FOUND_VALIDATE_DATA);
    }
}
