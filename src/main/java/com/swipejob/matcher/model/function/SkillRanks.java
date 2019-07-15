package com.swipejob.matcher.model.function;

import com.swipejob.matcher.model.Job;
import com.swipejob.matcher.model.Worker;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@Data
@NoArgsConstructor
public
class SkillRanks implements Function<Job, Long> {
    private Worker worker;

    public Long apply(Job job) {
        return worker.getCertificates().
                stream().
                filter(job.getRequiredCertificates()::contains).
                count();
    }
}
