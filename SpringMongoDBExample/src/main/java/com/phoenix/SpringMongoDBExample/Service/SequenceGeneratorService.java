package com.phoenix.SpringMongoDBExample.Service;

import com.phoenix.SpringMongoDBExample.model.MongoSequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class SequenceGeneratorService {

    @Autowired
    MongoOperations mongoOperations;

    public long sequenceGeneratorService(String seqNum)
    {
        MongoSequenceGenerator counter = mongoOperations
                                            .findAndModify(Query.query(where("_id").is(seqNum))
                                                            ,new Update().inc("sequence",1)
                                                            ,FindAndModifyOptions.options().returnNew(true).upsert(true)
                                                            ,MongoSequenceGenerator.class);
        return !Objects.isNull(counter)?counter.getSequence():1;
    }
}
