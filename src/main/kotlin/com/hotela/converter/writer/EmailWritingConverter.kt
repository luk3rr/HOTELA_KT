package com.hotela.converter.writer

import com.hotela.model.domain.Email
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter
import org.springframework.stereotype.Component

@Component
@WritingConverter
class EmailWritingConverter : Converter<Email, String> {
    override fun convert(source: Email): String = source.value
}
