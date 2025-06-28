package com.hotela.converter.reader

import com.hotela.model.domain.Email
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.stereotype.Component

@Component
@ReadingConverter
class EmailReadingConverter : Converter<String, Email> {
    override fun convert(source: String): Email = Email(source)
}
