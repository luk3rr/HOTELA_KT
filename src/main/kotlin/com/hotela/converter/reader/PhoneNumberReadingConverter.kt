package com.hotela.converter.reader

import com.hotela.model.domain.PhoneNumber
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.stereotype.Component

@Component
@ReadingConverter
class PhoneNumberReadingConverter : Converter<String, PhoneNumber> {
    override fun convert(source: String): PhoneNumber = PhoneNumber(source)
}
