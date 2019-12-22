package com.master.data.management.datamodel.config;

import com.mongodb.MongoClient;
import javax.annotation.PostConstruct;
import lombok.Data;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.stereotype.Component;

@Component
@Data
public class ManageCollections {

  private CodecRegistry pojoCodecRegistry;

  @PostConstruct
  public void configureCodec() {
    pojoCodecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
        CodecRegistries.fromProviders(
            PojoCodecProvider.builder()
                .register("com.master.data.management.datamodel.document").automatic(true)
                .build()));
  }
}
