package com.saltech.event_processor.batch;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

public class CustomItemWriter extends JdbcBatchItemWriter {

    @Override
    public void write(Chunk chunk) throws Exception {

    }
}
