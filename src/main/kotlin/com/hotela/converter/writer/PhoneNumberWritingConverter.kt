package com.hotela.converter.writer

import com.hotela.model.domain.PhoneNumber
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter
import org.springframework.stereotype.Component

@Component
@WritingConverter
class PhoneNumberWritingConverter : Converter<PhoneNumber, String> {
    override fun convert(source: PhoneNumber): String = source.value
}
