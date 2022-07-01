package learning.diplom.document.svc.config

import com.mongodb.client.MongoClient
import com.mongodb.client.gridfs.GridFSBucket
import com.mongodb.client.gridfs.GridFSBuckets
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeanConfig {
    @Bean
    fun getGridFSBucket(mongoClient: MongoClient): GridFSBucket? {
        val database = mongoClient.getDatabase("document-svc")
        return GridFSBuckets.create(database)
    }
}
