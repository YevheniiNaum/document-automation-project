package learning.diplom.administration.svc.config

import feign.codec.Decoder
import feign.codec.Encoder
import org.springframework.beans.factory.ObjectFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder
import org.springframework.cloud.openfeign.support.SpringDecoder
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter

@Configuration
class OpenFeignConfiguration {
    //Autowire the message converters.
    @Autowired
    private val messageConverters: ObjectFactory<HttpMessageConverters>? = null

    @Autowired
    private val messageCustomizer: ObjectProvider<HttpMessageConverterCustomizer>? = null

    //add the protobuf http message converter
    @Bean
    fun protobufHttpMessageConverter(): ProtobufHttpMessageConverter? {
        return ProtobufHttpMessageConverter()
    }

    //override the encoder
    @Bean
    fun springEncoder(): Encoder? {
        return SpringEncoder(messageConverters)
    }

    //override the encoder
    @Bean
    fun springDecoder(): Decoder? {
        return ResponseEntityDecoder(SpringDecoder(messageConverters, messageCustomizer))
    }
}
