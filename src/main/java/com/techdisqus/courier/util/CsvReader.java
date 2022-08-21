package com.techdisqus.courier.util;

import com.techdisqus.courier.exceptions.CsvParseException;
import com.opencsv.bean.CsvToBeanBuilder;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CsvReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvReader.class);

    @SneakyThrows
    public  <T >List<T> read(String fileName,Class<T> clazz) throws CsvParseException{
        try(FileReader reader = new FileReader(fileName)){
            return new CsvToBeanBuilder<T>(reader).withType(clazz).build().parse();
        }catch (IOException e){
          throw e;
        } catch (Exception e){
            LOGGER.error("error ",e);
            throw new CsvParseException("Error while parsing file "+fileName,e);
        }
    }

}
