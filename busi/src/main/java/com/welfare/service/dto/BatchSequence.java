package com.welfare.service.dto;

import com.welfare.persist.entity.Sequence;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 2/3/2021
 */
@Data
public class BatchSequence {
    private List<Sequence> sequences;

    public BatchSequence add(Sequence sequence){
        if(CollectionUtils.isEmpty(sequences)){
            sequences = new ArrayList<>();
        }
        Sequence newSequence = new Sequence();
        BeanUtils.copyProperties(sequence,newSequence);
        sequences.add(newSequence);
        return this;
    }
}
