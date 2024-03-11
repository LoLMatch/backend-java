package com.lolmatch.calendar.model;

import org.springframework.stereotype.Component;

import java.util.Set;

import static com.lolmatch.calendar.model.dictionary.MeetingIntervalDictionary.*;

@Component
public class MeetingDictionaryHelper {

    public Set<String> getWholeIntervalDictionaryAsSet() {
        return Set.of(
                DAY.name(),
                WEEK.name(),
                MONTH.name()
        );
    }
}
